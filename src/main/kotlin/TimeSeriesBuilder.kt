package com.zenmo.timeseries

import com.zenmo.timeseries.untyped.ArrayTimeSeriesImpl
import com.zenmo.timeseries.untyped.TimeSeries
import com.zenmo.timeseries.untyped.WraparoundArrayTimeSeries
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

/**
 * Builder for Java ergonomics.
 */
class TimeSeriesBuilder {
    private var start: Temporal? = null
    private var step: TemporalAmount? = null
    private var values: DoubleArray? = null
    private var wraparound: Boolean = false

    /**
     * Create the TimeSeries.
     * The builder will choose an implementation based on the parameters.
     */
    fun build(): TimeSeries {
        val start = this.start ?: throw IllegalStateException("start must be set")
        val step = this.step ?: throw IllegalStateException("step must be set")
        val values = this.values ?: throw IllegalStateException("values must be set")

        val timeSeries = ArrayTimeSeriesImpl(start, step, values)

        return if (wraparound) {
            WraparoundArrayTimeSeries(timeSeries)
        } else {
            timeSeries
        }
    }

    /**
     * Start of the first interval of the [values].
     * Must support arithmetic using the [step].
     */
    fun start(start: Temporal): TimeSeriesBuilder {
        this.start = start
        return this
    }

    /**
     * Length of each interval.
     * Must support arithmetic using [start].
     */
    fun step(step: TemporalAmount): TimeSeriesBuilder {
        this.step = step
        return this
    }

    /**
     * Array of values of each interval.
     */
    fun values(values: DoubleArray): TimeSeriesBuilder {
        this.values = values
        return this
    }

    /**
     * Whether to wrap around when request values after the end or before the start of the time series.
     */
    fun wraparound(wraparound: Boolean): TimeSeriesBuilder {
        this.wraparound = wraparound
        return this
    }
}
