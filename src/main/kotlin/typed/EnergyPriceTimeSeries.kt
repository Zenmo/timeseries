package com.zenmo.timeseries.typed

import java.time.temporal.Temporal

interface EnergyPriceTimeSeries {
    fun getEurPerKwh(intervalStart: Temporal): Double
}
