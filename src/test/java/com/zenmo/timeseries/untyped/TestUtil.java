package com.zenmo.timeseries.untyped;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestUtil {
    public static Instant startOf2025 = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Amsterdam")).toInstant();

    public static Duration quarterHour = Duration.ofMinutes(15);

    public static Duration hour = Duration.ofHours(1);
}
