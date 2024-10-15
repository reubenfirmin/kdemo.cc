package org.example.audio.instrument

enum class Parameter {
    A, B, C, D, E, F, G, H, I, J
}

data class AvailableParameter(val label: String, val parameter: Parameter)

