package org.example.pages.home.card3

import js.typedarrays.Uint8Array
import kotlinx.browser.window
import kotlinx.html.FlowContent
import kotlinx.html.div
import org.example.framework.tags.svg
import web.dom.document
import web.audio.AnalyserNode
import kotlin.math.pow

class BarCharts {

    private val barCount = 20 // Number of frequency bars to display
    private var animationId: Int? = null

    fun FlowContent.renderBars() {
        div {
            val svgWidth = (barCount * 20).toString()
            svg {
                id = "frequency-chart"
                width = svgWidth
                height = "100"
                viewBox = "0 0 $svgWidth 100"

                // Initial frequency bars
                repeat(barCount) { index ->
                    rect {
                        x = "${index * 20 + 2}"
                        y = "100"
                        this.width = "16"
                        this.height = "0"
                        fill = "#4299e1"
                        attributes["data-bar-index"] = index.toString()
                    }
                }
            }
        }
    }

    fun startAnimation(analyser: AnalyserNode) {
        val dataArray = Uint8Array(analyser.frequencyBinCount)

        fun animate() {
            analyser.getByteFrequencyData(dataArray)

            val bars = document.querySelectorAll("#frequency-chart rect")
            bars.forEach { bar ->
                val index = bar.getAttribute("data-bar-index")?.toInt() ?: return@forEach
                val value = dataArray[index]
                val normalizedValue = value / 255.0 // Normalize to 0-1 range

                // Apply exponential scaling to emphasize upper range
                val scaledValue = normalizedValue.pow(0.5)
                val height = (scaledValue * 100).toInt() // Scale to 0-100

                bar.setAttribute("height", height.toString())
                bar.setAttribute("y", (100 - height).toString())

                // Color based on frequency intensity
                val hue = (240 - value * 240 / 255) // Blue to Red
                bar.setAttribute("fill", "hsl($hue, 100%, 50%)")
            }
            animationId = window.requestAnimationFrame { animate() }
        }

        animate()
    }

    fun stopAnimation() {
        animationId?.let { window.cancelAnimationFrame(it) }
        animationId = null

        // Reset bars to initial state
        document.querySelectorAll("#frequency-chart rect").forEach { bar ->
            bar.setAttribute("height", "0")
            bar.setAttribute("y", "100")
            bar.setAttribute("fill", "#4299e1")
        }
    }
}