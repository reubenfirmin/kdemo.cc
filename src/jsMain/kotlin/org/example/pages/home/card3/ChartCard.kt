package org.example.pages.home.card3

import js.array.JsArray
import kotlinx.html.*
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.framework.tags.svg
import org.example.pages.home.components.card
import web.dom.document
import kotlin.random.Random
import kotlinx.browser.window
import org.example.framework.dom.onInput
import web.html.HTMLInputElement
import kotlin.math.log10
import kotlin.math.pow

class ChartCard {

    private var audioState: AudioState? = null
    private var animationId: Int? = null
    private var rhythmIntervalId: Int? = null
    private var beatCount = 0

    private var tb303Cutoff = logToLinear(1000)
    private var tb303Delay = 70

    private val colors = listOf("#FF00FF", "#00FF00", "#00FFFF", "#FF9900", "#FF0066")
    private val initialHeights = listOf(20, 40, 60, 80, 60, 50, 10, 10)

    fun FlowContent.render() {
        card {
            id = "chart-card"
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
                }
            }
            // TB-303 Control Sliders
            div("mt-4") {
                // Cutoff Frequency Slider
                div("mb-2") {
                    label("block text-sm font-medium text-gray-300") {
                        +"Filter Cutoff"
                    }
                    input(type = InputType.range, classes = "w-full") {
                        id = "cutoff"
                        min = "0"
                        max = "1000"
                        value = tb303Cutoff.toString()
                        step = "5"
                        onInput { event ->
                            val linearValue = (event.target as HTMLInputElement).value.toInt()
                            val logValue = linearToLog(linearValue).toInt()
                            tb303Cutoff = logValue
                        }
                    }
                }

                // Delay Time Slider
                div {
                    label("block text-sm font-medium text-gray-300") {
                        +"Delay Time"
                    }
                    input(type = InputType.range, classes = "w-full") {
                        id = "delay"
                        min = "1"
                        max = "100"
                        value = tb303Delay.toString()
                        step = "1"
                        onInput { event ->
                            val delayTime = (event.target as HTMLInputElement).value.toInt()
                            tb303Delay = delayTime
                        }
                    }
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

    private fun startTechno() {
        try {
            if (audioState == null) {
                audioState = AudioState()
            } else {
                audioState!!.resume()
            }

            with (audioState!!) {

                val interval = (60.0 / 130 * 1000 / 4).toInt() // 130bpm 16th notes...I think

                val song = HitTheClub(kickDrum, hiHat, tb303)

                // TODO is there a better clock? surely
                rhythmIntervalId = window.setInterval({
                    // adjust these each tick just for state simplicity
                    tb303.setDelayTime(this@ChartCard.tb303Delay)
                    tb303.setFilterCutoff(this@ChartCard.tb303Cutoff)
                    song.grid(beatCount++, currentTime())
                }, interval)
            }
        } catch (e: Exception) {
            console.error("Error starting audio: ${e.message}")
        }
    }

    private fun stopTechno() {
        try {
            if (audioState != null) {
                audioState!!.disconnect()
            }
            rhythmIntervalId?.let { window.clearInterval(it) }
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

    private fun linearToLog(value: Int): Float {
        val minFreq = 20f
        val maxFreq = 20000f
        return minFreq * 10.0.pow((value / 1000.0) * log10(maxFreq / minFreq)).toFloat()
    }

    private fun logToLinear(freq: Int): Int {
        val minFreq = 20f
        val maxFreq = 20000f
        return ((log10(freq / minFreq) / log10(maxFreq / minFreq)) * 1000).toInt().coerceIn(0, 1000)
    }
}

fun FlowContent.chartCard() {
    with (ChartCard()) {
        render()
    }
}