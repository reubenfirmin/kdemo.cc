package org.example.pages.home

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.h2
import org.example.framework.dom.onMouseEnter
import org.example.framework.tags.svg
import org.example.pages.home.components.card
import web.dom.document
import kotlin.random.Random

fun FlowContent.chartCard() {
    card {
        h2("text-xl font-semibold mb-4 text-gray-100") {
            +"Chart Demo"
        }
        div {
            svg {
                id = "chart"
                width = "200"
                height = "100"
                viewBox = "0 0 200 100"

                // Initial bar chart
                listOf(20, 40, 60, 80).forEachIndexed { index, height ->
                    rect {
                        x = "${index * 50 + 10}"
                        y = "${100 - height}"
                        this.width = "40"
                        this.height = "$height"
                        fill = "#4299e1"
                    }
                }

                onMouseEnter { _ ->
                    // Change bar heights on hover
                    val chart = document.getElementById("chart")
                    chart?.querySelectorAll("rect")?.forEach { rect ->
                        val newHeight = Random.nextInt(20, 100)
                        rect.setAttribute("height", newHeight.toString())
                        rect.setAttribute("y", (100 - newHeight).toString())
                    }
                }
            }
        }
    }
}