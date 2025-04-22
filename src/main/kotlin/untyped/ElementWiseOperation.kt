package com.zenmo.timeseries.untyped

import java.time.Instant

class ElementWiseMultiplicationUntypedTimeSeries(
    timeSeriesOne: UntypedTimeSeries,
    timeSeriesTwo: UntypedTimeSeries,
): ElementWiseOperationUntypedTimeSeries(timeSeriesOne, timeSeriesTwo, Double::times)

class ElementWiseAdditionUntypedTimeSeries(
    timeSeriesOne: UntypedTimeSeries,
    timeSeriesTwo: UntypedTimeSeries,
): ElementWiseOperationUntypedTimeSeries(timeSeriesOne, timeSeriesTwo, Double::plus)

open class ElementWiseOperationUntypedTimeSeries(
    val timeSeriesOne: UntypedTimeSeries,
    val timeSeriesTwo: UntypedTimeSeries,
    val operation: (Double, Double) -> Double,
): UntypedTimeSeries {
    override fun getValueAt(instant: Instant): Double {
        return operation(timeSeriesOne.getValueAt(instant), timeSeriesTwo.getValueAt(instant))
    }
}
