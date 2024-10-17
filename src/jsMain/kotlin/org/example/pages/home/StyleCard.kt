package org.example.pages.home

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.p
import org.example.framework.interop.css

fun FlowContent.styleCard() {
    div("w-full") {
        css {
            height = 100.pct
            borderRadius = 10.px
            overflow = Overflow.hidden
            boxShadow += BoxShadow(rgb(0, 0, 0, 0.1), 0.px, 4.px, 8.px)
            backgroundImage = linearGradient(45.deg, true) {
                colorStop(Color("#FF1E1E"), 0.px)  // Brighter red
                colorStop(Color("#FF1E1E"), 30.px)
                colorStop(Color.cyan, 30.px)
                colorStop(Color.cyan, 60.px)
                colorStop(Color.magenta, 60.px)
                colorStop(Color.magenta, 90.px)
            }
            backgroundSize = "200% 200%"
            animation += Animation(
                name = "gradient",
                duration = 10.s,
                timing = Timing.linear,
                iterationCount = IterationCount.infinite
            )
            KeyframesBuilder().apply {
                from {
                    backgroundPosition = RelativePosition.top
                }
                to {
                    backgroundPosition = RelativePosition.bottom
                }
            }.toString()
        }
        div {
            css {
                height = 100.pct
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
            }
            p {
                css {
                    color = Color.black
                    width = 140.pct
                    backgroundColor = Color.white
                    fontSize = 24.px
                    fontWeight = FontWeight.bold
                    textAlign = TextAlign.center
                    transform {
                        rotate((-10).deg)
                        skew((-10).deg)
                    }
                    boxShadow += BoxShadow(rgb(0, 0, 0, 0.2), 0.px, 10.px, 15.px)
                }
                p {
                    +"To achieve style, begin by"
                }
                p {
                    +"affecting none."
                }
            }
        }
    }
}