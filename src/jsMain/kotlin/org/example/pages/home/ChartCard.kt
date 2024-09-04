package org.example.pages.home

import js.array.JsArray
import kotlinx.html.*
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.framework.tags.svg
import org.example.pages.home.components.card
import web.dom.document
import kotlin.random.Random
import kotlinx.browser.window
import web.audio.*

class ChartCard {
    private var audioContext: AudioContext? = null
    private var kickOscillator: web.audio.OscillatorNode? = null
    private var hihatOscillator: web.audio.OscillatorNode? = null
    private var kickGain: web.audio.GainNode? = null
    private var hihatGain: web.audio.GainNode? = null
    private var hihatFilter: BiquadFilterNode? = null
    private var animationId: Int? = null
    private var rhythmIntervalId: Int? = null
    private var beatCount = 0
    private var noiseBuffer: AudioBuffer? = null


    private val colors = listOf("#FF00FF", "#00FF00", "#00FFFF", "#FF9900", "#FF0066")
    private val initialHeights = listOf(20, 40, 60, 80, 60, 50, 10, 10)

    fun FlowContent.render() {
        card {
            h2("text-xl font-semibold mb-4 text-gray-100") {
                +"Chart Demo"
            }
            div {
                val svgWidth = (initialHeights.size * 50).toString()
                svg {
                    id = "chart"
                    width = svgWidth
                    height = "100"
                    viewBox = "0 0 $svgWidth 100"

                    // Initial bar chart
                    initialHeights.forEachIndexed { index, height ->
                        rect {
                            x = "${index * 50 + 10}"
                            y = "${100 - height}"
                            this.width = "40"
                            this.height = "$height"
                            fill = "#4299e1"
                            attributes["data-bar-index"] = index.toString()
                        }
                    }

                    onMouseEnter { _ ->
                        startTechno()
                        startBarAnimation()
                    }

                    onMouseLeave { _ ->
                        stopTechno()
                        stopBarAnimation()
                    }
                }
            }
        }
    }

    private fun startTechno() {
        try {
            if (audioContext == null) {
                audioContext = AudioContext()
                createNoiseBuffer()
            }

            with(audioContext!!) {
                // Kick drum setup
                kickOscillator = createOscillator().apply {
                    type = OscillatorType.sine
                    frequency.setValueAtTime(70F, currentTime)
                }
                kickGain = createGain().apply {
                    gain.setValueAtTime(0F, currentTime)
                }
                kickOscillator?.connect(kickGain!!)
                kickGain?.connect(destination)
                kickOscillator?.start()

                // Hi-hat setup
                hihatFilter = createBiquadFilter().apply {
                    type = BiquadFilterType.bandpass
                    frequency.value = 10000F  // Center frequency
                    Q.value = 1F  // Quality factor
                }
                hihatGain = createGain().apply {
                    gain.setValueAtTime(0F, currentTime)
                }
                hihatFilter?.connect(hihatGain!!)
                hihatGain?.connect(destination)
                hihatOscillator?.connect(hihatGain!!)
                hihatGain?.connect(destination)
                hihatOscillator?.start()

                val interval = (60.0 / 130 * 1000 / 4).toInt()

                rhythmIntervalId = window.setInterval({
                    val time = currentTime
                    when (beatCount % 16) {
                        0, 3, 9 -> {
                            playKick(time)
                        }
                        6, 12 -> {
                            playKick(time)
                            playHiHat(time)
                        }
                        2, 10, 14 -> playHiHat(time)
                    }
                    beatCount++
                }, interval)
            }
        } catch (e: Exception) {
            console.error("Error starting audio: ${e.message}")
        }
    }

    private fun createNoiseBuffer() {
        audioContext?.let { ctx ->
            val bufferSize = ctx.sampleRate.toInt() * 2 // 2 seconds of noise
            noiseBuffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate)
            val channelData = noiseBuffer?.getChannelData(0)
            for (i in 0 until bufferSize) {
                channelData?.set(i, Random.nextFloat() * 2 - 1)
            }
        }
    }


    private fun playKick(time: Double) {
        kickOscillator?.frequency?.cancelScheduledValues(time)
        kickOscillator?.frequency?.setValueAtTime(150F, time) // Start at 150 Hz
        kickOscillator?.frequency?.exponentialRampToValueAtTime(30F, time + 0.05) // Sweep down to 30 Hz

        kickGain?.gain?.cancelScheduledValues(time)
        kickGain?.gain?.setValueAtTime(0F, time)
        kickGain?.gain?.linearRampToValueAtTime(1F, time + 0.005) // Quick attack
        kickGain?.gain?.exponentialRampToValueAtTime(0.01F, time + 0.4) // Longer decay
    }

    private fun AudioContext.playHiHat(time: Double) {
        val hihatSource = createBufferSource()
        hihatSource.buffer = noiseBuffer
        hihatSource.connect(hihatFilter!!)

        hihatGain?.gain?.cancelScheduledValues(time)
        hihatGain?.gain?.setValueAtTime(0F, time)
        hihatGain?.gain?.linearRampToValueAtTime(0.3F, time + 0.001) // Sharper attack
        hihatGain?.gain?.exponentialRampToValueAtTime(0.01F, time + 0.1) // Longer decay

        hihatSource.start(time)
        hihatSource.stop(time + 0.1) // Slightly longer sound

        // Add some variation to the hi-hat
        hihatFilter?.frequency?.setValueAtTime(10000F + Random.nextFloat() * 2000F, time)
    }

    private fun stopTechno() {
        try {
            rhythmIntervalId?.let { window.clearInterval(it) }
            kickOscillator?.stop()
            hihatOscillator?.stop()
            kickOscillator?.disconnect()
            hihatOscillator?.disconnect()
            kickGain?.disconnect()
            hihatGain?.disconnect()

            beatCount = 0

            // We're not closing the AudioContext here to allow for quick restart
            // If you want to fully close it, you'd need to handle it asynchronously
        } catch (e: Exception) {
            console.error("Error stopping audio: ${e.message}")
        }
    }

    private fun startBarAnimation() {
        fun animate() {
            val bars = document.querySelectorAll("#chart rect")
            bars.forEach { bar ->
                val newHeight = Random.nextInt(20, 100)
                bar.setAttribute("height", newHeight.toString())
                bar.setAttribute("y", (100 - newHeight).toString())
                bar.setAttribute("fill", colors[Random.nextInt(colors.size)])
            }
            animationId = window.requestAnimationFrame { animate() }
        }

        animate()
    }

    private fun stopBarAnimation() {
        animationId?.let { window.cancelAnimationFrame(it) }
        animationId = null

        // Reset bars to initial state
        JsArray.from(document.querySelectorAll("#chart rect")).forEachIndexed { index, bar ->
            bar.setAttribute("height", initialHeights[index].toString())
            bar.setAttribute("y", (100 - initialHeights[index]).toString())
            bar.setAttribute("fill", "#4299e1")
        }
    }
}

fun FlowContent.chartCard() {
    with (ChartCard()) {
        render()
    }
}