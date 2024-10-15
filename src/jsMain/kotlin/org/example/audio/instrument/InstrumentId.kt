package org.example.audio.instrument

enum class InstrumentType {
    TONAL,
    RHYTHM
}

enum class InstrumentId(val label: String, val type: InstrumentType) {
    BASSDRUM("Bass Drum", InstrumentType.RHYTHM),
    HIHAT("Hi Hat", InstrumentType.RHYTHM),
    SYNTH("Synth", InstrumentType.TONAL);
}