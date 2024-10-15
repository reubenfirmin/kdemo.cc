package org.example.audio.instrument

import web.audio.*
import kotlin.random.Random

class HiHat(private val audioContext: AudioContext, analyser: AnalyserNode): RhythmInstrument {

    private val scheduledEvents = ArrayDeque<Double>()

    private val hihatGain = audioContext.createGain().apply {
        gain.setValueAtTime(0F, audioContext.currentTime)
    }

    private val hihatFilter = audioContext.createBiquadFilter().apply {
        type = BiquadFilterType.bandpass
        frequency.value = 10000F  // Center frequency
        Q.value = 1F  // Quality factor
    }

    init {
        hihatFilter.connect(hihatGain)
        hihatGain.connect(audioContext.destination)
        hihatGain.connect(analyser)
    }

    private val noiseBuffer = audioContext.let { ctx ->
        val bufferSize = ctx.sampleRate.toInt() * 2 // 2 seconds of noise
        ctx.createBuffer(1, bufferSize, ctx.sampleRate).let { buffer ->
            val channelData = buffer.getChannelData(0)
            for (i in 0 until bufferSize) {
                channelData[i] = Random.nextFloat() * 2 - 1
            }
            buffer
        }
    }

    override fun play(time: Double, velocity: Double) {
        hihatGain.gain.setValueAtTime(0F, time)
        hihatGain.gain.linearRampToValueAtTime(0.3F, time + 0.001) // Sharper attack
        hihatGain.gain.exponentialRampToValueAtTime(0.01F, time + 0.1) // Longer decay

        audioContext.createBufferSource().apply {
            buffer = noiseBuffer
            connect(hihatFilter)
            start(time)
            stop(time + 0.1)
        }

        // Add some variation to the hi-hat
        hihatFilter.frequency.setValueAtTime(10000F + Random.nextFloat() * 2000F, time)

        scheduledEvents.addLast(time)
    }

    override fun disconnect() {
        hihatGain.disconnect()
        hihatFilter.disconnect()
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

    override fun availableParameters(): List<AvailableParameter> {
        TODO("Not yet implemented")
    }

    override fun setParameterValue(parameter: Parameter, value: Double) {
        TODO("Not yet implemented")
    }
}