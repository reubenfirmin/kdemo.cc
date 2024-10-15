package org.example.audio.instrument

import org.example.audio.Note
import kotlin.math.pow

interface TonalInstrument: Instrument {

    fun play(time: Double, note: Note, octave: Int, velocity: Double)

    fun frequency(note: Note, octave: Int): Double {
        return 440 * 2.0.pow((note.value - 10 + (octave - 4) * 12) / 12.0)
    }
}