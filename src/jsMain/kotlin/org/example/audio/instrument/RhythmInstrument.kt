package org.example.audio.instrument

interface RhythmInstrument: Instrument {

    fun play(time: Double, velocity: Double)
}