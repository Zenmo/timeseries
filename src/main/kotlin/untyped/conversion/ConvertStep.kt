package com.zenmo.timeseries.untyped.conversion

import com.zenmo.timeseries.isAfter
import com.zenmo.timeseries.untyped.ArrayTimeSeries
import com.zenmo.timeseries.untyped.TimeSeries
import java.time.temporal.TemporalAmount

/**
 * Convert a TimeSeries from one step to another.
 *
 * This is the most generic implementation which can deal
 * with any implementation of Temporal and TemporalAmount.
 *
 * Flow:
 *
 * - TimeSeries -> SourceSlots
 * - DestinationSlot + SourceSlots -> ReadSpecifications
 * - ReadSpecifications + values -> new values
 *
 * This function is in a separate package for clarity
 */
internal fun convertStepImpl(timeSeries: ArrayTimeSeries, newStep: TemporalAmount): ArrayTimeSeries {
    val start = timeSeries.start
    val end = timeSeries.end
    val oldStep = timeSeries.step

    val sourceValues = timeSeries.copyValuesArray()
    val derivedValues = mutableListOf<Double>()

    // fill one slot per loop iteration
    var destinationSlot = DestinationSlot(
        start = start,
        end = start.plus(newStep),
    )

    var sourceSlots = listOf(
        SourceSlot(
            start = start,
            end = start.plus(oldStep),
            index = 0,
        )
    )

    while (!destinationSlot.end.isAfter(end)) {
        sourceSlots = sourceSlots.adjust(destinationSlot, oldStep)
        val readSpecifications = sourceSlots.toReadSpecifications(destinationSlot)
        val result = readSpecifications.readAll(sourceValues)
        derivedValues.add(result)

        destinationSlot = destinationSlot.next(newStep);
    }

    return timeSeries.toBuilder()
        .step(newStep)
        .values(derivedValues.toDoubleArray())
        .build() as ArrayTimeSeries
}


