package com.zenmo.timeseries.untyped

import java.time.Instant
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * This a hacky class to work around missing data points at the start or end
 * due to leap years or time zones.
 *
 * Do not use this for a repeating schedule.
 */
internal data class WraparoundArrayTimeSeries(
    private val timeSeries: ArrayTimeSeriesImpl,
): ArrayTimeSeries, TimeSeriesWriter {
    init {
        if (timeSeries.size() == 0) {
            throw IndexOutOfBoundsException("Can't wraparound empty time series")
        }
    }

    /**
     * Writes to the underlying non-wraparound time series.
     */
    override operator fun set(intervalStart: Temporal, value: Double) {
        timeSeries.values[getOffset(intervalStart)] = value
    }

    override operator fun get(intervalStart: Temporal): Double {
        return timeSeries.values[getOffset(intervalStart)]
    }

    fun getOffset(intervalStart: Temporal): Int {
        val originalOffset = timeSeries.getOffset(intervalStart)
        return Math.floorMod(originalOffset, timeSeries.size())
    }

    override val step: TemporalAmount
        get() = timeSeries.step

    override val start: Temporal get() = Instant.MIN
    override val end: Temporal get() = Instant.MAX

    override fun toBuilder() = timeSeries.toBuilder().wraparound(true)

    override fun convertStep(newStep: TemporalAmount) = copy(
        timeSeries = timeSeries.convertStep(newStep)
    )

    override fun mapValues(transform: (Double) -> Double) = copy(
        timeSeries = timeSeries.mapValues(transform)
    )

    override fun copyValuesArray() = timeSeries.values
}
