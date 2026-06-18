package com.zenmo.timeseries.untyped;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WraparoundTest {
    private final Temporal start = Instant.parse("2023-01-01T00:00:00Z");

    private ArrayTimeSeriesBuilder builder() {
        return ArrayTimeSeries.builder()
                .step(Duration.ofMinutes(15))
                .start(start)
                .wraparound(true);
    }

    @Test
    void testWraparoundSingleValue() {
        var timeSeries = builder().values(new double[]{4.2}).build();

        assertEquals(4.2, timeSeries.get(Instant.parse("1999-01-01T12:00:00Z")));
        assertEquals(4.2, timeSeries.get(Instant.parse("2099-01-01T12:00:00Z")));
    }

    @Test
    void testWraparoundTwoValues() {
        var timeSeries = builder().values(new double[]{4.2, 2.1}).build();

        assertEquals(2.1, timeSeries.get(start.minus(Duration.ofMinutes(45))));
        assertEquals(4.2, timeSeries.get(start.minus(Duration.ofMinutes(30))));
        assertEquals(2.1, timeSeries.get(start.minus(Duration.ofMinutes(15))));
        assertEquals(4.2, timeSeries.get(start));
        assertEquals(2.1, timeSeries.get(start.plus(Duration.ofMinutes(15))));
        assertEquals(4.2, timeSeries.get(start.plus(Duration.ofMinutes(30))));
        assertEquals(2.1, timeSeries.get(start.plus(Duration.ofMinutes(45))));
    }
    
    @Test
    void testWraparoundYear() {
        var zonedStart = ZonedDateTime.parse("2023-01-01T00:00:00Z");
        var timeSeries = ArrayTimeSeries.builder()
                .step(Period.ofMonths(1))
                .start(zonedStart)
                .wraparound(true)
                .values(DoubleStream.iterate(1.0, (a) -> a + 1.0).limit(12).toArray())
                .build();

        // same year
        assertEquals(1.0, timeSeries.get(zonedStart));
        assertEquals(2.0, timeSeries.get(zonedStart.plusMonths(1)));
        assertEquals(12.0, timeSeries.get(zonedStart.plusMonths(11)));

        // next year
        assertEquals(1.0, timeSeries.get(zonedStart.plusMonths(12)));
        assertEquals(2.0, timeSeries.get(zonedStart.plusMonths(13)));

        // previous year
        assertEquals(12.0, timeSeries.get(zonedStart.minusMonths(1)));
        assertEquals(11.0, timeSeries.get(zonedStart.minusMonths(2)));
    }
}
