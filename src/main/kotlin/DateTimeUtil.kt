package com.zenmo.timeseries

import java.time.Duration
import java.time.temporal.TemporalAmount

private const val SECONDS_IN_HOUR = 3600.0

internal fun TemporalAmount.hours(): Double = Duration.from(this).toSeconds() / SECONDS_IN_HOUR

internal fun TemporalAmount.multiplicateInverseHours(): Double {
    return 1 / (this.hours())
}
