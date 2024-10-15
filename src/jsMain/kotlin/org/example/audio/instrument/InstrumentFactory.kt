package org.example.audio.instrument

import org.example.audio.instrument.InstrumentId.*
import web.audio.AnalyserNode
import web.audio.AudioContext

object InstrumentFactory {

    fun rhythm(instrumentId: InstrumentId, audioContext: AudioContext, analyserNode: AnalyserNode): RhythmInstrument {
        return when (instrumentId) {
            HIHAT -> HiHat(audioContext, analyserNode)
            BASSDRUM -> KickDrum(audioContext, analyserNode)
            else -> throw Exception("Unknown rhythm instrument: $instrumentId")
        }
    }

    fun tonal(instrumentId: InstrumentId, audioContext: AudioContext, analyserNode: AnalyserNode): TonalInstrument {
        return when (instrumentId) {
            SYNTH -> Synth(audioContext, analyserNode)
            else -> throw Exception("Unknown rhythm instrument: $instrumentId")
        }
    }
}