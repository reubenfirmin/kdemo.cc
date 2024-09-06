package org.example.pages.home.chartcard

import web.audio.AnalyserNode
import web.audio.AudioContext
import web.audio.OscillatorType

class KickDrum(audioContext: AudioContext, analyser: AnalyserNode) {

    private val kickOscillator= audioContext.createOscillator().apply {
        type = OscillatorType.sine
        start()
    }

    private val kickGain = audioContext.createGain().apply {
        gain.value = 0f
    }

    init {
        kickOscillator.connect(kickGain)
        kickGain.connect(audioContext.destination)
        kickGain.connect(analyser)
    }

    fun play(time: Double) {
        kickOscillator.frequency.cancelScheduledValues(time)
        kickOscillator.frequency.setValueAtTime(150f, time)
        kickOscillator.frequency.exponentialRampToValueAtTime(30f, time + 0.05)

        kickGain.gain.cancelScheduledValues(time)
        kickGain.gain.setValueAtTime(0f, time)
        kickGain.gain.linearRampToValueAtTime(1f, time + 0.005)
        kickGain.gain.exponentialRampToValueAtTime(0.01f, time + 0.5)
    }

    fun disconnect() {
        kickOscillator.disconnect()
        kickGain.disconnect()
        kickOscillator.stop()
    }
}