package com.zenmo.timeseries.untyped.conversion

import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * Specifies a time slot to write
 * when converting a time series to a different step
 */
data class DestinationSlot(
    val start: Temporal,
    val end: Temporal,
) {
    fun next(step: TemporalAmount) = DestinationSlot(
        start = end,
        end = end.plus(step),
    )
}
