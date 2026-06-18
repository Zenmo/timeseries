package com.zenmo.timeseries.untyped

import java.time.temporal.Temporal

/**
 * Provide a copy of the internal backing array.
 * This is for interoperability with other formats.
 */
interface ArrayTimeSeries : TimeSeries<ArrayTimeSeries>, TimeSeriesWriter {
    companion object {
        @JvmStatic
        fun builder() = ArrayTimeSeriesBuilder()
    }

    fun copyValuesArray(): DoubleArray

    /**
     * The start of the first interval.
     */
    val start: Temporal

    /**
     * The end of the last interval.
     */
    val end: Temporal

    fun toBuilder(): ArrayTimeSeriesBuilder
}
