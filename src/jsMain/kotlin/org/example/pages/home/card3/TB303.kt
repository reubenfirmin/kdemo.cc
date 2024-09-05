package org.example.pages.home.card3

import js.typedarrays.Float32Array
import web.audio.*
import kotlin.math.abs
import kotlin.math.pow

class TB303(val audioContext: AudioContext, analyser: AnalyserNode) {

    private val oscillator: OscillatorNode = audioContext.createOscillator().apply {
        type = OscillatorType.sawtooth
        start()
    }

    private val filter: BiquadFilterNode = audioContext.createBiquadFilter().apply {
        type = BiquadFilterType.lowpass
        frequency.value = 1000f
        Q.value = 18f
    }

    private val prefilterGain = audioContext.createGain().apply {
        gain.value = 7f
    }

    private val preFilterDistortion: WaveShaperNode = audioContext.createWaveShaper().apply {
        curve = makeDistortionCurve(15.0f)
    }

    private fun makeDistortionCurve(amount: Float): Float32Array {
        val samples = 44100
        val curve = Float32Array(samples)
        val deg = 3.14f / 180
        for (i in 0 until samples) {
            val x = i * 2 / samples - 1
            curve[i] = ((3 + amount) * x * 20 * deg / (3.14f + amount * abs(x)))
        }
        return curve
    }

    private val envelope: GainNode = audioContext.createGain().apply {
        gain.value = 0f
    }

    private val delay1: DelayNode = audioContext.createDelay().apply {
        delayTime.value = 0.3f
    }

    private val delay2: DelayNode = audioContext.createDelay().apply {
        delayTime.value = 0.3f
    }

    private val delayFeedback: GainNode = audioContext.createGain().apply {
        gain.value = 0.4f
    }

    private val output: GainNode = audioContext.createGain().apply {
        gain.value = 0.4f
    }

    init {
        oscillator.connect(preFilterDistortion)
        preFilterDistortion.connect(prefilterGain)
        prefilterGain.connect(envelope)
        envelope.connect(filter)

        filter.connect(delay1)
        // could mix wet/dry here
        filter.connect(output)

        delay1.connect(delay2)
        delay2.connect(delayFeedback)
        delayFeedback.connect(delay1)

        // could mix wet/dry here
        delayFeedback.connect(output)

        output.connect(audioContext.destination)
        output.connect(analyser)
    }

    fun play(time: Double, note: Int, octave: Int) {
        val frequency = 440 * 2.0.pow((note - 69 + (octave - 4) * 12) / 12.0)

        oscillator.frequency.setValueAtTime(frequency.toFloat(), time)

        envelope.gain.cancelScheduledValues(time)
        envelope.gain.setValueAtTime(0f, time)
        envelope.gain.linearRampToValueAtTime(1f, time + 0.003) // Attack
        envelope.gain.exponentialRampToValueAtTime(0.2f, time + 0.1) // Decay
        envelope.gain.exponentialRampToValueAtTime(0.00001f, time + 2.0) // Release

        val currentCutoff = filter.frequency.value
        val peakCutoff = minOf(currentCutoff * 5, 20000f)  // Peak at 5x current cutoff or 20kHz, whichever is lower

        filter.frequency.cancelScheduledValues(time)
        filter.frequency.setValueAtTime(currentCutoff, time)
        filter.frequency.linearRampToValueAtTime(peakCutoff, time + 0.05) // Quick sweep up
        filter.frequency.exponentialRampToValueAtTime(currentCutoff, time + 0.1) // Quick sweep down
    }

    fun setFilterCutoff(frequency: Int) {
        val safeFrequency = frequency.coerceIn(20, 20000)
        filter.frequency.value = safeFrequency.toFloat()

        filter.Q.exponentialRampToValueAtTime(
            (8 * (safeFrequency / 20000f)).coerceIn(0.01f, 8f),
            audioContext.currentTime + 0.01
        )
        console.log(filter.frequency.value)
    }

    fun setDelayTime(delayPercent: Int) {
        delay1.delayTime.value = (delayPercent / 100.0).toFloat()
        delay2.delayTime.value = (delayPercent / 100.0).toFloat()
    }

    fun disconnect() {
        oscillator.disconnect()
        prefilterGain.disconnect()
        preFilterDistortion.disconnect()
        filter.disconnect()
        envelope.disconnect()
        delay1.disconnect()
        delay2.disconnect()
        delayFeedback.disconnect()
        output.disconnect()
    }
}
