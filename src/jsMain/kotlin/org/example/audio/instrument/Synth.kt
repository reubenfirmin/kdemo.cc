package org.example.audio.instrument

import js.typedarrays.Float32Array
import org.example.audio.Note
import web.audio.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * [osc1] [osc1Mix] \                                                                  [output]
 *                  |                                                                     |
 *                  [preFilterGain] [preFilterDistortion] [envelope] [filter] [delay1] [delay2] [delayFeedback]
 *                  |                                                            |___________________|
 * [osc2] [osc1Mix] /
 */
class Synth(private val audioContext: AudioContext, analyser: AnalyserNode): TonalInstrument {

    private val scheduledEvents = ArrayDeque<Double>()

    private val oscillator1: OscillatorNode = audioContext.createOscillator().apply {
        type = OscillatorType.sawtooth
        start()
    }

    private val osc1Mix: GainNode = audioContext.createGain().apply {
        gain.value = 1f
    }

    private val oscillator2: OscillatorNode = audioContext.createOscillator().apply {
        type = OscillatorType.sine
        start()
    }

    private val osc2Mix: GainNode = audioContext.createGain().apply {
        gain.value = 0.8f
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

    private val filter: BiquadFilterNode = audioContext.createBiquadFilter().apply {
        type = BiquadFilterType.lowpass
        frequency.value = 1000f
        Q.value = 18f
    }

    private val delay1: DelayNode = audioContext.createDelay(1.0).apply {
        delayTime.value = 0.0f
    }

    private val delay2: DelayNode = audioContext.createDelay(1.0).apply {
        delayTime.value = 0.0f
    }

    private val delayFeedback: GainNode = audioContext.createGain().apply {
        gain.value = 0.3f
    }

    private val output: GainNode = audioContext.createGain().apply {
        gain.value = 0.4f
    }

    init {
        oscillator1.connect(osc1Mix)
        oscillator2.connect(osc2Mix)
        osc1Mix.connect(envelope)
        osc2Mix.connect(envelope)

//        preFilterDistortion.connect(prefilterGain)
//        prefilterGain.connect(envelope)
        envelope.connect(filter)

        filter.connect(delay1)
        // could mix wet/dry here
        filter.connect(output)

        delay1.connect(output)
        delay1.connect(delayFeedback)
        // could mix wet/dry here
//        delay2.connect(delayFeedback)
//        delay2.connect(output)
        delayFeedback.connect(delay1)

        output.connect(audioContext.destination)
        output.connect(analyser)
    }

    override fun availableParameters() = listOf(
        AvailableParameter("Cutoff", Parameter.A),
        AvailableParameter("Delay Time", Parameter.B)
    )

    override fun setParameterValue(parameter: Parameter, value: Double) {
        when (parameter) {
            Parameter.A -> setFilterCutoff(parameterValueInRange(value, 20.0, 20000.0, Slope.EASE_IN).toInt())
            Parameter.B -> setDelayTime(parameterValueInRange(value, 0.0, 1000.0, Slope.EASE_OUT).toInt())
            else -> {
                throw Exception("Parameter $parameter not supported")
            }
        }
    }

    // TODO wire up velocity
    override fun play(time: Double, note: Note, octave: Int, velocity: Double) {
        val frequency = frequency(note, octave)

        oscillator1.frequency.setValueAtTime(frequency.toFloat(), time)
        // detune
        oscillator2.frequency.setValueAtTime((frequency + 2).toFloat(), time)

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

        scheduledEvents.addLast(time)
    }

    fun setFilterCutoff(frequency: Int) {
//        console.log(frequency)
        val safeFrequency = frequency.coerceIn(20, 20000)
        filter.frequency.value = safeFrequency.toFloat()

        filter.Q.exponentialRampToValueAtTime(
            (8 * (safeFrequency / 20000f)).coerceIn(0.01f, 8f),
            audioContext.currentTime + 0.01
        )
    }

    fun setDelayTime(delayMs: Int) {
        val converted = delayMs.toFloat() / 1000.0F
        console.log(converted)
        delay1.delayTime.value = converted
        delay2.delayTime.value = converted
    }

    override fun disconnect() {
        oscillator1.disconnect()
        oscillator2.disconnect()
        prefilterGain.disconnect()
        preFilterDistortion.disconnect()
        filter.disconnect()
        envelope.disconnect()
        delay1.disconnect()
        delay2.disconnect()
        delayFeedback.disconnect()
        output.disconnect()
    }

    override fun scheduledEventTimes(): List<Double> {
        cleanupPastEvents()
        return scheduledEvents
    }

    private fun cleanupPastEvents() {
        val currentTime = this.audioContext.currentTime
        while (scheduledEvents.isNotEmpty() && scheduledEvents.first() < currentTime) {
            scheduledEvents.removeFirst()
        }
    }
}

