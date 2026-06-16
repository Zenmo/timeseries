package com.zenmo.timeseries.untyped.conversion

import com.zenmo.timeseries.div
import com.zenmo.timeseries.isAfter
import com.zenmo.timeseries.isBefore
import java.time.Duration
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * Specifies which source slot to read
 * when converting a time series to a different step.
 */
data class SourceSlot(
    val start: Temporal,
    val end: Temporal,
    val index: Int,
) {
    fun next(step: TemporalAmount) = SourceSlot(
        start = end,
        end = end.plus(step),
        index = index + 1,
    )

    val duration get() = Duration.between(start, end)

    fun toReadSpecification(destinationSlot: DestinationSlot): ReadSpecification {
        val startIndex = if (destinationSlot.start.isAfter(this.start)) {
            val offset = Duration.between(this.start, destinationSlot.start)
            val fraction: Double = offset / this.duration
            this.index + fraction
        } else {
            this.index.toDouble()
        }

        val endIndex = if (destinationSlot.end.isBefore(this.end)) {
            val offset = Duration.between(this.start, destinationSlot.end)
            val fraction: Double = offset / this.duration
            this.index + fraction
        } else {
            this.index + 1.0
        }

        return ReadSpecification(startIndex, endIndex)
    }
}

