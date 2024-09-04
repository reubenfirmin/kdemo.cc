package org.example.pages.home.card3

import web.audio.*
import kotlin.random.Random

class HiHat(private val audioContext: AudioContext, private val analyser: AnalyserNode) {

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

    fun play(time: Double) {
        hihatGain.gain.cancelScheduledValues(time)
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
    }

    fun disconnect() {
        hihatGain.disconnect()
        hihatFilter.disconnect()
    }

}