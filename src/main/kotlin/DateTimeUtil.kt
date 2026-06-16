package com.zenmo.timeseries

import java.time.Duration
import java.time.Instant
import java.time.temporal.Temporal

internal fun Temporal.isAfter(other: Temporal): Boolean =
    Instant.from(this).isAfter(Instant.from(other))

internal fun Temporal.isBefore(other: Temporal): Boolean =
    Instant.from(this).isBefore(Instant.from(other))

/**
 * Works just for seconds.
 */
internal operator fun Duration.div(other: Duration): Double =
    this.toSeconds().toDouble() / other.toSeconds().toDouble()
