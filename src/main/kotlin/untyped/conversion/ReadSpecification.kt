package com.zenmo.timeseries.untyped.conversion

import kotlin.math.roundToInt

/**
 * Says which indexes to read from the source array
 * when converting a timer series to a different step.
 */
data class ReadSpecification(
    val startIndex: Double,
    val endIndex: Double,
) {
    init {
        if (startIndex >= endIndex) {
            throw Exception("Wrong readSpecification order: $this")
        }

        if (endIndex - startIndex > 1.0) {
            throw Exception("ReadSpecification is longer than 1: $this")
        }
    }

    fun isWholeSlot() =
        startIndex.isWholeNumber()
                && endIndex.isWholeNumber()

    fun read(values: DoubleArray): Double {
        // This would be a place to do interpolation
        return values[startIndex.toInt()]
    }
}

fun Double.isWholeNumber(): Boolean =
    roundToInt().toDouble() == this

fun List<ReadSpecification>.readAll(
    values: DoubleArray,
    // Take care only to use this for rates, not absolute values like m3 and kWh
    aggregatorFn: Iterable<Double>.() -> Double = Iterable<Double>::average,
) =
    this.map {
        it.read(values)
    }.aggregatorFn()
