package org.example.audio.instrument

import kotlin.math.*

interface Instrument {

    fun scheduledEventTimes(): List<Double>

    fun availableParameters(): List<AvailableParameter>

    fun setParameterValue(parameter: Parameter, value: Double)

    fun disconnect()

    fun parameterValueInRange(value: Double, min: Double, max: Double, slope: Slope): Double {
        // Ensure value is between 0 and 1
        val clampedValue = value.coerceIn(0.0, 1.0)

        val mappedValue = when (slope) {
            Slope.LINEAR -> clampedValue
            Slope.LOG -> logarithmicMapping(clampedValue)
            Slope.EXPONENTIAL -> exponentialMapping(clampedValue)
            Slope.SINE -> sineMapping(clampedValue)
            Slope.COSINE -> cosineMapping(clampedValue)
            Slope.SMOOTHSTEP -> smoothstepMapping(clampedValue)
            Slope.EASE_IN -> easeInMapping(clampedValue)
            Slope.EASE_OUT -> easeOutMapping(clampedValue)
        }

        // Map the result to the desired range
        return min + (max - min) * mappedValue
    }

    // Helper functions for different slope types
    private fun logarithmicMapping(value: Double): Double =
        ln(1 + value * (E - 1)) / ln(E)

    private fun exponentialMapping(value: Double): Double =
        (exp(value) - 1) / (E - 1)

    private fun sineMapping(value: Double): Double =
        sin(value * PI / 2)

    private fun cosineMapping(value: Double): Double =
        1 - cos(value * PI / 2)

    private fun smoothstepMapping(value: Double): Double =
        value * value * (3 - 2 * value)

    private fun easeInMapping(value: Double): Double =
        value * value

    private fun easeOutMapping(value: Double): Double =
        1 - (1 - value) * (1 - value)
}

enum class Slope {
    LINEAR,
    LOG,
    EXPONENTIAL,
    SINE,
    COSINE,
    SMOOTHSTEP,
    EASE_IN,
    EASE_OUT
}

