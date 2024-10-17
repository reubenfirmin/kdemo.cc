package org.example.pages.home.chartcard

import kotlinx.html.*
import org.example.audio.songs.HitTheClub
import org.example.audio.Sequencer
import org.example.audio.instrument.InstrumentId
import org.example.audio.instrument.Parameter
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.pages.home.components.card
import web.dom.document
import org.example.framework.dom.onClick
import org.example.framework.dom.onInput
import web.html.HTMLInputElement

class ChartCard {

    private val barCharts = BarCharts()
    private val sequencer = Sequencer()
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
                        if (!sequencer.isPlaying()) {
                            val analyser = sequencer.start(grid)
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

            sliders(sequencer)

            onMouseEnter { _ ->
                if (document.getElementById("overlay") == null && !sequencer.isPlaying()) {
                    val analyser = sequencer.start(grid)
                    barCharts.startAnimation(analyser)
                }
            }

            onMouseLeave { _ ->
                sequencer.stop()
                barCharts.stopAnimation()
            }
        }
    }

}

fun FlowContent.sliders(sequencer: Sequencer) {
    // TB-303 Control Sliders
    div("mt-4") {
        // TODO genericize based on options offered by the sequencer

        // Cutoff Frequency Slider
        div("mb-2") {
            label("block text-sm font-medium text-gray-300") {
                +"Filter Cutoff"
            }
            input(type = InputType.range, classes = "w-full") {
                id = "cutoff"
                min = "0"
                max = "1000"
                value = "100" // TODO init from sequencer
                step = "1"
                onInput { event ->
                    sequencer.setParameter(InstrumentId.SYNTH, Parameter.A, (event.target as HTMLInputElement).value.toDouble() / 1000)
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
                min = "0"
                max = "1000"
                value = "0" // TODO init from sequencer
                step = "1"
                onInput { event ->
                    sequencer.setParameter(InstrumentId.SYNTH, Parameter.B, (event.target as HTMLInputElement).value.toDouble())
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