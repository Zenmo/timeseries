package com.zenmo.timeseries.untyped

import java.time.Instant

class ConstantMultiplicationUntypedTimeSeries(
    timeSeries: UntypedTimeSeries,
    constant: Double,
): ConstantOperationUntypedTimeSeries(timeSeries, constant, Double::times)

class ConstantAdditionUntypedTimeSeries(
    timeSeries: UntypedTimeSeries,
    constant: Double,
): ConstantOperationUntypedTimeSeries(timeSeries, constant, Double::plus)

open class ConstantOperationUntypedTimeSeries(
    val timeSeries: UntypedTimeSeries,
    val constant: Double,
    val operation: (Double, Double) -> Double,
): UntypedTimeSeries {
    override fun getValueAt(instant: Instant): Double {
        return operation(timeSeries.getValueAt(instant), constant)
    }
}
