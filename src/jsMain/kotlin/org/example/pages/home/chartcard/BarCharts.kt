package org.example.pages.home.chartcard

import js.typedarrays.Uint8Array
import kotlinx.browser.window
import kotlinx.html.FlowContent
import kotlinx.html.div
import org.example.framework.tags.svg
import web.dom.document
import web.audio.AnalyserNode
import kotlin.math.pow

class BarCharts {

    private val barCount = 20
    private var animationId: Int? = null


    fun FlowContent.renderBars() {
        div("w-full") {
            svg {
                id = "frequency-chart"
                attributes["width"] = "100%"
                height = "100"
                viewBox = "0 0 100 100"
                preserveAspectRatio = "none"

                // 80% total width for bars, 20% for gaps
                val barWidthPercent = 80.0 / barCount
                val gapWidthPercent = 20.0 / (barCount + 1)

                repeat(barCount) { index ->
                    val xPosition = gapWidthPercent * (index + 1) + barWidthPercent * index
                    rect {
                        x = "${xPosition}%"
                        y = "100"
                        width = "${barWidthPercent}%"
                        height = "0%"
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

                // emphasize upper range
                val scaledValue = normalizedValue.pow(0.5)

                val height = (scaledValue * 100).toInt()
                bar.setAttribute("height", height.toString())
                bar.setAttribute("y", (100 - height).toString())

                // color based on frequency intensity
                val hue = (240 - value * 240 / 255)
                bar.setAttribute("fill", "hsl($hue, 100%, 50%)")
            }
            animationId = window.requestAnimationFrame { animate() }
        }

        animate()
    }

    fun stopAnimation() {
        animationId?.let { window.cancelAnimationFrame(it) }
        animationId = null

        // decolor bars
        document.querySelectorAll("#frequency-chart rect").forEach { bar ->
            bar.setAttribute("fill", "#6299e1")
        }
    }
}