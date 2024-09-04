package org.example.pages.home.card3

import js.array.JsArray
import kotlinx.browser.window
import kotlinx.html.FlowContent
import kotlinx.html.div
import org.example.framework.tags.svg
import web.dom.document
import kotlin.random.Random

class BarCharts {

    private val colors = listOf("#FF00FF", "#00FF00", "#00FFFF", "#FF9900", "#FF0066")
    private val initialHeights = listOf(20, 40, 60, 80, 60, 50, 10, 10)
    private var animationId: Int? = null

    fun FlowContent.renderBars() {
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
    }

    fun startBarAnimation() {
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

    fun stopBarAnimation() {
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