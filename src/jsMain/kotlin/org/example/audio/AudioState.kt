package org.example.audio

import org.example.audio.grid.GridEvent
import org.example.audio.instrument.HiHat
import org.example.audio.instrument.InstrumentId
import org.example.audio.instrument.KickDrum
import org.example.audio.instrument.Synth
import web.audio.*
import kotlin.math.abs

class AudioState {
    private var audioContext = AudioContext()
    private var isClosing = false

    var analyser = audioContext.createAnalyser().apply {
        fftSize = 256
        smoothingTimeConstant = 0.4
    }

    private var kickDrum = KickDrum(audioContext, analyser)
    private var hiHat = HiHat(audioContext, analyser)
    private var synth = Synth(audioContext, analyser)

    fun currentTime() = audioContext.currentTime

    fun disconnect() {
        if (audioContext.state != AudioContextState.closed && !isClosing) {
            isClosing = true
            kickDrum.disconnect()
            hiHat.disconnect()
            synth.disconnect()
            audioContext.closeAsync().then { isClosing = false }
        }
    }

    fun sequence(instrument: InstrumentId, time: Double, event: GridEvent): Boolean {
        val instrumentInstance = when (instrument) {
            InstrumentId.BASSDRUM -> kickDrum
            InstrumentId.HIHAT -> hiHat
            InstrumentId.SYNTH -> synth
        }

        // Check if the proposed time clashes with any scheduled notes
        if (instrumentInstance.scheduledEventTimes().none { abs(it - time) < 0.001 }) { // 1ms tolerance
            when (instrument) {
                // TODO velocity in all
                InstrumentId.BASSDRUM -> kickDrum.play(time)
                InstrumentId.HIHAT -> hiHat.play(time)
                InstrumentId.SYNTH -> synth.play(time, event.note, event.octave)//, event.velocity)
            }
            return true
        }
        return false
    }

    fun resume() {
        if (audioContext.state == AudioContextState.closed || isClosing) {
            audioContext = AudioContext()
            isClosing = false
            analyser = audioContext.createAnalyser().apply {
                fftSize = 256
                smoothingTimeConstant = 0.4
            }
            kickDrum = KickDrum(audioContext, analyser)
            hiHat = HiHat(audioContext, analyser)
            synth = Synth(audioContext, analyser)
        } else {
            audioContext.resumeAsync()
        }
    }
}