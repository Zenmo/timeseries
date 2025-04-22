package com.zenmo.timeseries

import com.zenmo.timeseries.untyped.SteppedUntypedTimeSeries
import com.zenmo.timeseries.untyped.UntypedTimeSeries
import java.time.Instant
import java.time.temporal.TemporalAmount

interface TypedTimeSeries<UnderlyingTypeSeries: UntypedTimeSeries> {
    fun getUntypedTimeSeries(): UnderlyingTypeSeries
    fun getUnit(): UnitType
}

enum class UnitType {
    KW,
    M3,
    UnitLess,
}

abstract class AbstractSteppedTimeSeries(
    protected val untypedTimeSeries: SteppedUntypedTimeSeries,
): Iterable<Double> {
    fun getStart(): Instant = untypedTimeSeries.getStart()
    fun getEnd(): Instant = untypedTimeSeries.getEnd()
    fun getStep(): TemporalAmount = untypedTimeSeries.getStep()
    override fun iterator(): Iterator<Double> = untypedTimeSeries.iterator()
}



class KwTimeSeries(
    untypedTimeSeries: SteppedUntypedTimeSeries,
): AbstractSteppedTimeSeries(untypedTimeSeries) {

    fun getKwAt(offset: Int): Double {
        return untypedTimeSeries.getValueAt(offset)
    }

    fun getKwAt(instant: Instant): Double {
        return untypedTimeSeries.getValueAt(instant)
    }
}
