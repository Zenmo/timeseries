package com.zenmo.timeseries.untyped

import java.time.Instant
import java.time.temporal.TemporalAmount

/**
 * The most basic time series type.
 * An implementation only needs to implement [getValueAt]
 */
interface UntypedTimeSeries {
    fun getValueAt(instant: Instant): Double

    /**
     * Element-wise addition
     */
    fun add(timeSeries: UntypedTimeSeries): UntypedTimeSeries {
        return ElementWiseAdditionUntypedTimeSeries(timeSeries, this)
    }

    fun add(constant: Double): UntypedTimeSeries {
        return ConstantAdditionUntypedTimeSeries(this, constant)
    }

    /** or multiply? */

    fun scale(constant: Double): UntypedTimeSeries {
        return ConstantMultiplicationUntypedTimeSeries(this, constant)
    }

    fun sliceStart() {
        TODO("Not yet implemented")
    }
    // what other operations would be useful?
}
