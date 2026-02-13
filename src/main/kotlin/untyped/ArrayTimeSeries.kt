package com.zenmo.timeseries.untyped

import com.zenmo.timeseries.TimeSeriesBuilder
import java.time.Duration
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * A time series data structure backed by an array.
 * 
 * This class has a few assumptions that simplify the implementation:
 * - The data points are continuous.
 * - It cannot be resized.
 * - It can only be accessed with a step equal to the step of the underlying data structure.
 * - It can only be accessed at intervals which align with the step.
 */
internal class ArrayTimeSeries(
    /**
     * Start of the first interval.
     *
     * The implementation of [Temporal] should support arithmetic
     * using the implementation of the given [step].
     *
     * In practice this means that if [start] is a [java.time.Instant],
     * step should be a [java.time.Duration].
     * This works up to interval lengths expressed in weeks.
     *
     * For month intervals, it is recommended to use
     * [java.time.ZonedDateTime], and for [step] [java.time.Period].
     */

    override val start: Temporal,

    /**
     * Length of each interval.
     */
    override val step: TemporalAmount,

    /**
     * Raw values.
     */
    private val values: DoubleArray,
) : TimeSeriesAccessor {
    /**
     * Get the value at the interval starting at [intervalStart] using the step of this data structure.
     */
    override operator fun get(intervalStart: Temporal): Double {
        val offset = getOffset(intervalStart)

        // unsure if there is a relevant performance difference between try-catch
        // and doing our own bounds check
        return try {
            values[offset]
        } catch (_: ArrayIndexOutOfBoundsException) {
            throw IndexOutOfBoundsException("Requested interval starting at $intervalStart is not in the timeseries of $start until $end")
        }
    }

    private fun getOffset(intervalStart: Temporal): Int {
        // optimization for Duration
        if (step is Duration) {
            // doesn't work if unaligned
            return Duration.between(start, intervalStart).dividedBy(step).toInt()
        }

        // generic case
        var result = 0
        var current = start
        while (Duration.between(current, intervalStart).seconds > 0) {
            current = current.plus(step)
            result++
        }
        return result
    }

    /**
     * The end of the last interval.
     */
    override val end: Temporal get() {
        // optimization for Duration
        if (step is Duration) {
            return start.plus(step.multipliedBy(values.size.toLong()))
        }

        // generic case
        var result = start
        repeat(values.size) {
            result = result.plus(step)
        }
        return result
    }

    fun toBuilder() = TimeSeriesBuilder().start(start).step(step).values(values)
}
