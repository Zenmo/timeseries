package com.zenmo.timeseries.typed

import java.time.temporal.Temporal

interface EnergyTimeSeries {
    fun getKwh(intervalStart: Temporal): Double
}
