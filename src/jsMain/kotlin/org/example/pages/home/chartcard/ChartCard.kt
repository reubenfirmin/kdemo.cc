package org.example.pages.home.chartcard

import kotlinx.html.*
import org.example.audio.HitTheClub
import org.example.audio.TechnoPlayer
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.pages.home.components.card
import web.dom.document
import org.example.framework.dom.onClick
import org.example.framework.dom.onInput

class ChartCard {

    private val barCharts = BarCharts()
    private val technoPlayer = TechnoPlayer()
    private val grid = HitTheClub.createGrid()

    fun FlowContent.render() {
        card {
            id = "chart-card"
            style = "position: relative;" // Needed for absolute positioning of overlay

            // Overlay div
            div("absolute inset-0 w-full h-full bg-black bg-opacity-70 flex justify-center items-center z-10") {
                id = "overlay"

                button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                    id = "play_button"
                    +"Play"
                    onClick { event ->
                        event.preventDefault()
                        document.getElementById(this@div.id)!!.remove()
                        if (!technoPlayer.isPlaying()) {
                            val analyser = technoPlayer.startTechno(grid)
                            barCharts.startAnimation(analyser)
                        }
                    }
                }
            }

            h2("text-xl font-semibold mb-4 text-gray-100") {
                +"Chart Demo"
            }

            with(barCharts) {
                renderBars()
            }

            sliders(technoPlayer)

            onMouseEnter { _ ->
                if (document.getElementById("overlay") == null && !technoPlayer.isPlaying()) {
                    val analyser = technoPlayer.startTechno(grid)
                    barCharts.startAnimation(analyser)
                }
            }

            onMouseLeave { _ ->
                technoPlayer.stopTechno()
                barCharts.stopAnimation()
            }
        }
    }

}

fun FlowContent.sliders(technoPlayer: TechnoPlayer) {
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
                // TODO
                value = "10"//technoPlayer.getCutoffDisplay().toString()
                step = "1"
                onInput { event ->
                    //technoPlayer.setCutoff((event.target as HTMLInputElement).value.toInt())
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
                // TODO
                value = "10"//technoPlayer.getDelayDisplay().toString()
                step = "1"
                onInput { event ->
                    //technoPlayer.setDelay((event.target as HTMLInputElement).value.toInt())
                }
            }
        }
    }

}

fun FlowContent.chartCard() {
    with (ChartCard()) {
        render()
    }
}