package com.zenmo.timeseries.typed;

import com.zenmo.timeseries.untyped.TimeSeries;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElectricityTimeSeriesTest {
    @Test
    void test() {
        var start = Instant.parse("2025-01-01T00:00:00Z");
        var electricityTimeSeries = TimeSeries.builder()
                .values(new double[]{2.0, 3.0, 4.0})
                .start(start)
                .step(Duration.ofMinutes(15))
                .buildElectricityKwhTimeSeries();

        assertEquals(2.0, electricityTimeSeries.getKwh(start));
        assertEquals(8.0, electricityTimeSeries.getKw(start));
        assertEquals(3.0, electricityTimeSeries.getKwh(start.plus(Duration.ofMinutes(15))));
        assertEquals(12.0, electricityTimeSeries.getKw(start.plus(Duration.ofMinutes(15))));
        assertEquals(4.0, electricityTimeSeries.getKwh(start.plus(Duration.ofMinutes(30))));
        assertEquals(16.0, electricityTimeSeries.getKw(start.plus(Duration.ofMinutes(30))));
        assertThrows(IndexOutOfBoundsException.class, () -> electricityTimeSeries.getKwh(start.plus(Duration.ofMinutes(45))));
        assertThrows(IndexOutOfBoundsException.class, () -> electricityTimeSeries.getKw(start.plus(Duration.ofMinutes(45))));
    }
}
