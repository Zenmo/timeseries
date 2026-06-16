package com.zenmo.timeseries.untyped

/**
 * Provide a copy of the internal backing array.
 * This is for interoperability with other formats.
 */
interface ArrayTimeSeries : TimeSeriesAccessor {
    fun copyValuesArray(): DoubleArray
}
