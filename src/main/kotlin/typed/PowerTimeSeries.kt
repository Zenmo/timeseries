package com.zenmo.timeseries.typed

import java.time.temporal.Temporal

interface PowerTimeSeries {
    fun getKw(intervalStart: Temporal): Double
}
