package com.zenmo.timeseries;

import com.zenmo.timeseries.untyped.UntypedTimeSeries;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;

public class OfficeHoursTimeSeries implements UntypedTimeSeries {
    @Override
    public double getValueAt(Instant instant) {
        if (isOfficeHours(instant)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    private boolean isOfficeHours(Instant instant) {
        var zoned = instant.atZone(ZoneId.of("Europe/Amsterdam"));
        var dayOfWeek = zoned.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return false;
        }

        var hour = zoned.getHour();
        return hour >= 9 && hour <= 17;
    }
}
