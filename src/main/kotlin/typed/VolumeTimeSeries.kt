package com.zenmo.timeseries.typed

import java.time.temporal.Temporal

interface VolumeTimeSeries {
    fun getM3(intervalStart: Temporal): Double
}
