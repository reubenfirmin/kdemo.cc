package org.example.audio.instrument

enum class InstrumentId(val label: String) {
    BASSDRUM("Bass Drum"),
    HIHAT("Hi Hat"),
    SYNTH("Synth");

    companion object {
        fun from(str: String): InstrumentId {
            return entries.first { it.label == str }
        }
    }
}