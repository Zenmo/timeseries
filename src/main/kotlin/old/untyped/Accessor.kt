package com.zenmo.timeseries.old.untyped

import java.time.Duration
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

interface TimeSeriesAccessor {
    fun getValueAt(
        intervalStart: Temporal,
        step: TemporalAmount
    ): Double
}

class ForwardIterationOptimizedArrayBackedTimeSeries(
    private val start: Temporal,
    private val step: TemporalAmount,
    private val values: DoubleArray,
): TimeSeriesAccessor {
    private var currentOffset: Temporal = start
    private var currentIndex: Int = 0

    override fun getValueAt(intervalStart: Temporal, step: TemporalAmount): Double {
//        if (intervalStart > currentOffset) {
//            reset()
//        }
//
//        while (intervalStart < currentOffset) {
//            currentOffset += step
//            currentIndex++
//        }

        return values[currentIndex]
    }

    private fun reset() {
        currentOffset = start
        currentIndex = 0
    }
}

class ArrayBackedTimeSeries(
    private val start: Temporal,
    private var step: TemporalAmount,
    private var values: DoubleArray = DoubleArray(0),
) {
    fun put(
        intervalStart: Temporal,
        step: TemporalAmount,
        value: Double,
    ): Unit {
        if (values.isEmpty()) {
            this.step = step
        }

        if (step != this.step) {
            throw Exception("When writing to a timeseries, all values must have the same step")
        }

        val offset = Duration.between(start, intervalStart)
            .dividedBy(Duration.from(step))
            .toInt()

        if (offset >= values.size) {
            resize()
        }

        values[offset] = value
    }

    fun resize() {
        values = values.copyOf(values.size * 2 + 1)
    }
}
