package com.zenmo.timeseries.untyped;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayTimeSeriesTest {
    Instant start = TestUtil.startOf2025;

    Duration quarterHour = Duration.ofMinutes(15);

    ArrayTimeSeriesBuilder builder = ArrayTimeSeries.builder().step(quarterHour).start(start);

    @Test
    public void testReadEmptyTimeSeries() {
        var emptyTimeSeries = builder.values(new double[]{}).build();

        assertEquals(start, emptyTimeSeries.getEnd());
        var exception = assertThrows(IndexOutOfBoundsException.class, () -> emptyTimeSeries.get(start));
        assertEquals(
                "Requested interval starting at 2024-12-31T23:00:00Z is not in the timeseries of 2024-12-31T23:00:00Z until 2024-12-31T23:00:00Z",
                exception.getMessage()
        );
    }

    @Test
    public void testReadNonEmptyTimeSeries() {
        var timeSeries = builder
                .values(new double[]{2.0, 3.0})
                .build();

        var end = start.plus(Duration.ofMinutes(30));

        assertEquals(end, timeSeries.getEnd());
        assertEquals(2.0, timeSeries.get(start));
        assertEquals(3.0, timeSeries.get(start.plus(quarterHour)));
        var exception = assertThrows(IndexOutOfBoundsException.class, () -> timeSeries.get(end));
        assertEquals(
                "Requested interval starting at 2024-12-31T23:30:00Z is not in the timeseries of 2024-12-31T23:00:00Z until 2024-12-31T23:30:00Z",
                exception.getMessage()
        );
    }

    @Test
    public void testReadMonthlyTimeSeries() {
        var values = DoubleStream.iterate(2.0, (a) -> a + 2.0).limit(12).toArray();
        var month = Period.ofMonths(1);
        var start = this.start.atZone(ZoneId.of("Europe/Amsterdam"));

        var timeSeries = ArrayTimeSeries.builder()
                .step(month)
                .start(start)
                .values(values)
                .build();

        assertEquals(2.0, timeSeries.get(start));
        assertEquals(4.0, timeSeries.get(start.plus(month)));
        assertEquals(24.0, timeSeries.get(start.plus(Period.ofMonths(11))));
        assertEquals(start.plus(Period.ofYears(1)), timeSeries.getEnd());
        var exception = assertThrows(IndexOutOfBoundsException.class, () -> timeSeries.get(start.plus(Period.ofYears(1))));
        assertEquals(
                "Requested interval starting at 2026-01-01T00:00+01:00[Europe/Amsterdam] is not in the timeseries of 2025-01-01T00:00+01:00[Europe/Amsterdam] until 2026-01-01T00:00+01:00[Europe/Amsterdam]",
                exception.getMessage()
        );
    }

    @Test
    public void testWrite() {
        var timeSeries = builder.values(new double[24 * 4]).build();
        var midday = start.plus(Duration.ofHours(12));
        timeSeries.set(midday, 3.0);

        assertEquals(3.0, timeSeries.get(midday));
        assertEquals(0.0, timeSeries.get(midday.minus(quarterHour)));
        assertEquals(0.0, timeSeries.get(midday.plus(quarterHour)));

        assertThrows(IndexOutOfBoundsException.class, () -> timeSeries.set(start.plus(Duration.ofDays(1)), 4.0));
    }
}
