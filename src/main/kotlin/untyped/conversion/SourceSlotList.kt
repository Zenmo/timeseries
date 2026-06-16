package com.zenmo.timeseries.untyped.conversion

import com.zenmo.timeseries.isAfter
import com.zenmo.timeseries.isBefore
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount

typealias SourceSlotList = List<SourceSlot>

fun SourceSlotList.toReadSpecifications(destinationSlot: DestinationSlot): List<ReadSpecification> =
    this.map { it.toReadSpecification(destinationSlot) }

/**
 * Drop slots before the destination start
 * and add slots until the destination end
 */
fun SourceSlotList.adjust(destinationSlot: DestinationSlot, step: TemporalAmount): SourceSlotList =
    this.appendUntil(destinationSlot.end, step).keepAfter(destinationSlot.start)

fun SourceSlotList.keepAfter(dateTime: Temporal): SourceSlotList =
    this.filter {
        it.end.isAfter(dateTime)
    }

fun SourceSlotList.appendNext(step: TemporalAmount) =
    this + this.last().next(step)

fun SourceSlotList.appendUntil(dateTime: Temporal, step: TemporalAmount): SourceSlotList {
    var resultList = this
    while (resultList.last().end.isBefore(dateTime)) {
        resultList = resultList.appendNext(step)
    }
    return resultList
}
