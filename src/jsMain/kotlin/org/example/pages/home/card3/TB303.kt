package org.example.pages.home.card3

import web.audio.*
import kotlin.math.pow

class TB303(audioContext: AudioContext) {

    private val oscillator: OscillatorNode = audioContext.createOscillator().apply {
        type = OscillatorType.sawtooth
        start()
    }

    private val filter: BiquadFilterNode = audioContext.createBiquadFilter().apply {
        type = BiquadFilterType.lowpass
        frequency.value = 1000f
        Q.value = 8f
    }

    private val envelope: GainNode = audioContext.createGain().apply {
        gain.value = 0f
    }

    private val delay: DelayNode = audioContext.createDelay().apply {
        delayTime.value = 0.7f
    }

    private val delayFeedback: GainNode = audioContext.createGain().apply {
        gain.value = 0.4f
    }

    private val output: GainNode = audioContext.createGain().apply {
        gain.value = 0.4f
    }

    init {
        oscillator.connect(filter)
        filter.connect(envelope)
        envelope.connect(output)
        envelope.connect(delay)
        delay.connect(delayFeedback)
        delayFeedback.connect(delay)
        delayFeedback.connect(output)
        output.connect(audioContext.destination)
    }

    fun play(time: Double, note: Int, octave: Int) {
        val frequency = 440 * 2.0.pow((note - 69 + (octave - 4) * 12) / 12.0)

        oscillator.frequency.setValueAtTime(frequency.toFloat(), time)

        envelope.gain.cancelScheduledValues(time)
        envelope.gain.setValueAtTime(0f, time)
        envelope.gain.linearRampToValueAtTime(1f, time + 0.003) // Attack
        envelope.gain.exponentialRampToValueAtTime(0.2f, time + 0.1) // Decay
        envelope.gain.exponentialRampToValueAtTime(0.00001f, time + 0.5) // Release

        filter.frequency.cancelScheduledValues(time)
        filter.frequency.setValueAtTime(100f, time)
        filter.frequency.linearRampToValueAtTime(5000f, time + 0.05) // Quick sweep up
        filter.frequency.exponentialRampToValueAtTime(filter.frequency.value, time + 0.1) // Quick sweep down
    }

    fun setFilterCutoff(frequency: Int) {
        val safeFrequency = frequency.coerceIn(20, 20000)
        filter.frequency.value = safeFrequency.toFloat()
    }

    fun setDelayTime(delayPercent: Int) {
        delay.delayTime.value = (delayPercent / 100.0).toFloat()
    }

    fun disconnect() {
        oscillator.disconnect()
        filter.disconnect()
        envelope.disconnect()
        delay.disconnect()
        delayFeedback.disconnect()
        output.disconnect()
    }
}