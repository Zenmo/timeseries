package com.zenmo.timeseries.untyped.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.zenmo.timeseries.untyped.ArrayTimeSeriesImpl;
import com.zenmo.timeseries.untyped.TestUtil;
import com.zenmo.timeseries.untyped.TimeSeries;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;

public class ConvertStepTest {
    Instant start = TestUtil.startOf2025;

    Duration quarterHour = Duration.ofMinutes(15);

    Duration hour = Duration.ofHours(1);

    @Test
    public void testConvertHourlyToQuarterHourly() {
        var hourlyTimeSeries = TimeSeries.builder()
                .start(start)
                .step(hour)
                .values(new double[]{2.0, 3.0})
                .build();

        var quarterHourlyTimeSeries = hourlyTimeSeries.convertStep(quarterHour);

        assertEquals(8, ((ArrayTimeSeriesImpl) quarterHourlyTimeSeries).size$com_zenmo_timeseries());

        assertEquals(2.0, quarterHourlyTimeSeries.get(start));
        assertEquals(2.0, quarterHourlyTimeSeries.get(start.plus(quarterHour)));
        assertEquals(3.0, quarterHourlyTimeSeries.get(start.plus(hour)));
        // last value
        assertEquals(3.0, quarterHourlyTimeSeries.get(start.plus(hour.plus(quarterHour.multipliedBy(3)))));
        assertThrows(IndexOutOfBoundsException.class, () -> quarterHourlyTimeSeries.get(start.plus(Duration.ofHours(2))));
    }

    @Test
    public void testConvertQuarterHourlyToHourly() {
        var quarterHourlyTimeSeries = TimeSeries.builder()
                .start(start)
                .step(quarterHour)
                .values(new double[]{2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0})
                .build();

        var hourlyTimeSeries = quarterHourlyTimeSeries.convertStep(hour);

        assertEquals(2, ((ArrayTimeSeriesImpl) hourlyTimeSeries).size$com_zenmo_timeseries());

        assertEquals(3.5, hourlyTimeSeries.get(start));
        assertEquals(7.5, hourlyTimeSeries.get(start.plus(hour)));
        assertThrows(IndexOutOfBoundsException.class, () -> hourlyTimeSeries.get(start.plus(Duration.ofHours(2))));
    }

    @Test
    public void testIncompleteSlotIsDiscarded() {
        var quarterHourlyTimeSeries = TimeSeries.builder()
                .start(start)
                .step(quarterHour)
                .values(new double[]{2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0})
                .build();

        var hourlyTimeSeries = quarterHourlyTimeSeries.convertStep(hour);

        assertEquals(1, ((ArrayTimeSeriesImpl) hourlyTimeSeries).size$com_zenmo_timeseries());

        assertEquals(3.5, hourlyTimeSeries.get(start));
        assertThrows(IndexOutOfBoundsException.class, () -> hourlyTimeSeries.get(start.plus(Duration.ofHours(1))));
    }

    @Test
    public void testConvertJanuary() {
        var jan1st = start.atZone(ZoneId.of("Europe/Amsterdam"));
        var monthlyTimeSeries = TimeSeries.builder()
                .start(jan1st)
                .step(Period.ofMonths(1))
                .values(new double[]{2.0})
                .build();

        var dailyTimeSeries = monthlyTimeSeries.convertStep(Duration.ofDays(1));

        assertEquals(31, ((ArrayTimeSeriesImpl) dailyTimeSeries).size$com_zenmo_timeseries());

        assertEquals(2.0, dailyTimeSeries.get(jan1st));
        assertEquals(2.0, dailyTimeSeries.get(jan1st.plus(Duration.ofDays(14))));
        assertEquals(2.0, dailyTimeSeries.get(jan1st.plus(Duration.ofDays(30))));
    }

    @Test
    public void testConvertFebruary() {
        var feb1st = start.atZone(ZoneId.of("Europe/Amsterdam")).plus(Period.ofMonths(1));

        var monthlyTimeSeries = TimeSeries.builder()
                .start(feb1st)
                .step(Period.ofMonths(1))
                .values(new double[]{2.0})
                .build();

        var dailyTimeSeries = monthlyTimeSeries.convertStep(Duration.ofDays(1));

        assertEquals(28, ((ArrayTimeSeriesImpl) dailyTimeSeries).size$com_zenmo_timeseries());

        assertEquals(2.0, dailyTimeSeries.get(feb1st));
        assertEquals(2.0, dailyTimeSeries.get(feb1st.plus(Duration.ofDays(14))));
        assertEquals(2.0, dailyTimeSeries.get(feb1st.plus(Duration.ofDays(27))));
    }
}
