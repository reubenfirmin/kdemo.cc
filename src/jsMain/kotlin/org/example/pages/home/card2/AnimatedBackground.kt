package org.example.pages.home.card2

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import org.example.framework.tags.svg
import kotlin.random.Random

fun FlowContent.animatedBackground() {
    div("fixed inset-0 pointer-events-none z-0") {
        id = "animated-bg"
        svg {
            attributes["class"] = "w-full h-full"
            attributes["viewBox"] = "0 0 100 100"
            attributes["preserveAspectRatio"] = "none"

            // Static grid
            g("opacity-20") {
                for (i in 0..40) {
                    line {
                        x1 = (i * 4).toString()
                        y1 = "0"
                        x2 = (i * 4).toString()
                        y2 = "100"
                        stroke = "#3300FF"
                        strokeWidth = "0.3"
                    }
                }
                for (i in 0..40) {
                    line {
                        x1 = "0"
                        y1 = (i * 4).toString()
                        x2 = "100"
                        y2 = (i * 4).toString()
                        stroke = "#3300FF"
                        strokeWidth = "0.3"
                    }
                }
            }

            // Animated circles
            g {
                for (i in 0..100) {
                    circle {
                        val neonColors = listOf("#ff00ff", "#00ff00", "#00ffff", "#ff9900", "#ff0066")

                        cx = Random.nextInt(200).toString()
                        cy = Random.nextInt(200).toString()
                        r = Random.nextInt(20).toString()
                        fill = neonColors[Random.nextInt(neonColors.size)]
                        attributes["class"] = "opacity-50 animate-spin"
                    }
                }
            }
        }
    }
}