package com.zenmo.timeseries.untyped

import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * Time series accessor which has an innate step.
 *
 * This is an untyped version, intended to be wrapped by a typed accessor.
 * The typed accessor will provide clarity on the unit of the values, such as kW or kWh.
 */
interface TimeSeriesAccessor {
    /**
     * Get the value at the interval starting at [intervalStart] with length [step].
     */
    operator fun get(intervalStart: Temporal): Double

    /**
     * Length of each interval.
     */
    val step: TemporalAmount

    /**
     * The start of the first interval.
     */
    val start: Temporal

    /**
     * The end of the last interval.
     */
    val end: Temporal
}
