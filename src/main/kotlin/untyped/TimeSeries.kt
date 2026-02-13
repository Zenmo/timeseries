package com.zenmo.timeseries.untyped

import com.zenmo.timeseries.TimeSeriesBuilder

interface TimeSeries : TimeSeriesAccessor, TimeSeriesWriter {
    companion object {
        @JvmStatic
        fun builder(): TimeSeriesBuilder = TimeSeriesBuilder()
    }
}
