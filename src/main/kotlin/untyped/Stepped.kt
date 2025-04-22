package com.zenmo.timeseries.untyped

import java.lang.IndexOutOfBoundsException
import java.time.Duration
import java.time.Instant
import java.time.Period
import java.time.temporal.TemporalAmount

/**
 * A time series backed by a discrete step, like an array.
 */
interface SteppedUntypedTimeSeries: UntypedTimeSeries, Iterable<Double> {
    fun getStart(): Instant
    fun getEnd(): Instant
    /**
     * TemporalAmount should be backed by [Period] when dealing with months or years,
     * and by [Duration] when dealing with weeks or less.
     */
    fun getStep(): TemporalAmount
    fun getValueAt(offset: Int): Double
    fun getOffsetOf(instant: Instant): Int

    /**
     * If you have verified that you have the desired start time and step (and unit),
     * you can use this iterator as an optimized way to use the timeseries in a simulation.
     */
    override fun iterator(): Iterator<Double>
}

data class DoubleArrayBackedTimeSeries(
    private val start: Instant,
    private val step: TemporalAmount,
    private val values: DoubleArray,
): SteppedUntypedTimeSeries, Iterable<Double> {
    init {
        if (values.isEmpty()) {
            throw IndexOutOfBoundsException("Time series is empty")
        }
    }

    override fun getStep() = step

    override fun getValueAt(offset: Int): Double = values[offset]

    override fun getValueAt(instant: Instant): Double {
        return getValueAt(getOffsetOf(instant))
    }

    override fun getOffsetOf(instant: Instant): Int {
        if (instant.isBefore(start)) {
            throw IndexOutOfBoundsException("$instant is before $start")
        }

        // optimization case
        if (step is Duration) {
            val duration = Duration.between(start, instant)
            return step.dividedBy(duration).toInt()
        }

        // generic case, usually for months, quarters and years
        var offset = 0
        var dateTime = this.start
        while (true) {
            dateTime = dateTime.plus(step)
            if (dateTime.isAfter(instant)) {
                return offset
            }
            offset++
        }
    }

    fun getOffsetOf(duration: Duration): Int {
        return getOffsetOf(start.plus(duration))
    }

    override fun getStart() = start

    override fun getEnd(): Instant {
        if (step is Duration) {
            return start.plus(step.multipliedBy(values.size.toLong() + 1))
        }

        if (step is Period) {
            return start.plus(step.multipliedBy(values.size + 1))
        }

        TODO("Not implemented generic getEnd()")
    }

    override fun iterator(): Iterator<Double> {
        return values.iterator()
    }
}
