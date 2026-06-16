package com.zenmo.timeseries.typed

import java.time.temporal.Temporal

/**
 * For duty cycle or power factor
 */
interface UnitlessTimeSeries {
    fun get(intervalStart: Temporal): Double
}
