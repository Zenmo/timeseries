package com.zenmo.timeseries.untyped

import java.time.Instant
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

internal class WraparoundArrayTimeSeries(
    private val timeSeries: ArrayTimeSeries,
): TimeSeriesAccessor {
    init {
        if (timeSeries.size() == 0) {
            throw IndexOutOfBoundsException("Can't wraparound empty time series")
        }
    }

    override fun get(intervalStart: Temporal): Double {
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
}
