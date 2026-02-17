package com.zenmo.timeseries.untyped

import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

interface TimeSeriesWriter {
    operator fun set(
        intervalStart: Temporal,
        value: Double,
    ): Unit

    val step: TemporalAmount
}
