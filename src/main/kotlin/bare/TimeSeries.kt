package com.zenmo.timeseries.untyped

import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * Minimal functionality a TimeSeries implementation should support.
 *
 * The TimeSeries has an innate step.
 *
 * The TimeSeries has bare values. It is intended to be wrapped to provide
 * clarity on the unit of the values, such as kW or kWh.
 *
 * This is a self-referential generic type so that users do not have to cast the return types.
 */
interface TimeSeries<T: TimeSeries<T>> {
    /**
     * Get the value at the interval starting at [intervalStart] with length [step].
     */
    operator fun get(intervalStart: Temporal): Double

    /**
     * Length of each interval.
     */
    val step: TemporalAmount

    fun convertStep(newStep: TemporalAmount): T

    /**
     * Do a simple value-by-value transform.
     * Useful when converting from one unit to another.
     * The implementation is eager if possible.
     */
    fun mapValues(transform: (Double) -> Double): T
}
