package com.zenmo.timeseries;

import com.zenmo.timeseries.untyped.DoubleArrayBackedTimeSeries;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IteratorTest {
    @Test
    public void testIterator() {
        var start = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Amsterdam"));
        var kwTimeSeries = new KwTimeSeries(
                new DoubleArrayBackedTimeSeries(
                        start.toInstant(),
                        Duration.ofMinutes(15),
                        new double[] {3.0, 2.0, 1.0}
                )
        );

        var iterator = kwTimeSeries.iterator();

        assertEquals(3.0, iterator.next());
        assertEquals(2.0, iterator.next());
        assertEquals(1.0, iterator.next());
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
