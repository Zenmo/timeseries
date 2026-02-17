package com.zenmo.timeseries.typed

import com.zenmo.timeseries.multiplicateInverseHours
import com.zenmo.timeseries.untyped.TimeSeries
import java.time.temporal.Temporal

/**
 * A time series of electric energy backed by a raw time series of kWh.
 * Can directly take the quarter-hourly values given by an electricity supplier.
 */
internal class ElectricityKwhTimeSeries(
    internal val kwhTimeSeries: TimeSeries,
) : ElectricityTimeSeries {
    override fun getKwh(intervalStart: Temporal): Double = kwhTimeSeries[intervalStart]

    private val multiplicateInverseHours: Double = kwhTimeSeries.step.multiplicateInverseHours()

    override fun getKw(intervalStart: Temporal): Double =
        getKwh(intervalStart) * multiplicateInverseHours
}
