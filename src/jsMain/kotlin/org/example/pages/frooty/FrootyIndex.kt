package org.example.pages.frooty

import kotlinx.html.*
import org.example.framework.dom.onClick
import org.example.framework.dom.onInput
import org.example.audio.TechnoPlayer
import web.animations.FrameRequestId
import web.animations.requestAnimationFrame
import web.animations.cancelAnimationFrame
import web.canvas.CanvasRenderingContext2D
import web.dom.document
import web.html.HTMLCanvasElement
import web.html.HTMLInputElement
import web.uievents.MouseEvent
import kotlin.math.*

class Frooty {
    private val TOTAL_ROWS = 9
    private val MAX_DIVISIONS = 32
    private val INSTRUMENTS = listOf("Bass Drum", "Hi-Hat", "Synth")

    private var rows = List(TOTAL_ROWS) { RowData(4, Instrument.BASSDRUM, 48, mutableSetOf()) }
    private var bpm = 120
    private var isPlaying = false
    private var activeRow: Int? = null
    private var rotation = 0.0

    private lateinit var canvas: HTMLCanvasElement
    private var animationId: FrameRequestId? = null

    private val technoPlayer = TechnoPlayer()

    private enum class Instrument(val label: String) {
        BASSDRUM("Bass Drum"),
        HIHAT("Hi Hat"),
        SYNTH("Synth");

        companion object {
            fun from(str: String): Instrument {
                return entries.first { it.label == str }
            }
        }
    }

    private data class RowData(
        var divisions: Int,
        var instrument: Instrument,
        var note: Int,
        var activeDivisions: MutableSet<Int>
    )

    fun FlowContent.render() {
        div("flex h-screen bg-gray-900 text-white") {
            div("flex-1 flex items-center justify-center") {
                canvas {
                    width = "600"
                    height = "600"
                    classes = setOf("border", "border-gray-700")
                    onClick { event ->
                        handleDivisionToggle(event)
                    }
                }
            }
            div("w-64 p-4 bg-gray-800 flex flex-col") {
                h2("text-xl font-bold mb-4") {
                    +"Controls"
                }
                div("mb-4") {
                    label("block mb-2") {
                        +"BPM"
                    }
                    input(type = InputType.range, classes = "w-full") {
                        min = "60"
                        max = "240"
                        value = bpm.toString()
                        onInput { event ->
                            bpm = (event.target as HTMLInputElement).value.toInt()
                        }
                    }
                    span {
                        +"$bpm BPM"
                    }
                }
                div("mb-4") {
                    button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        +if (isPlaying) "Pause" else "Play"
                        onClick { _ ->
                            togglePlay()
                        }
                    }
                }
                activeRow?.let { rowIndex ->
                    div("mb-4") {
                        label("block mb-2") {
                            +"Divisions"
                        }
                        div("flex items-center") {
                            button(classes = "mr-2") {
                                +"-"
                                onClick { _ ->
                                    handleDivisionChange(-1)
                                }
                            }
                            span {
                                +"${rows[rowIndex].divisions}"
                            }
                            button(classes = "ml-2") {
                                +"+"
                                onClick { _ ->
                                    handleDivisionChange(1)
                                }
                            }
                        }
                    }
                    div("mb-4") {
                        label("block mb-2") {
                            +"Instrument"
                        }
                        select(classes = "w-full bg-gray-700 text-white") {
                            INSTRUMENTS.forEach { instrument ->
                                option {
                                    value = instrument
                                    selected = instrument == rows[rowIndex].instrument.label
                                    +instrument
                                }
                            }
                            onInput { event ->
                                handleInstrumentChange((event.target as HTMLInputElement).value)
                            }
                        }
                    }
                    div("mb-4") {
                        label("block mb-2") {
                            +"Note"
                        }
                        div("flex items-center") {
                            button(classes = "mr-2") {
                                +"-"
                                onClick { _ ->
                                    handleNoteChange(-1)
                                }
                            }
                            span {
                                +"${rows[rowIndex].note}"
                            }
                            button(classes = "ml-2") {
                                +"+"
                                onClick { _ ->
                                    handleNoteChange(1)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Initialize canvas after rendering
        canvas = document.querySelector("canvas") as HTMLCanvasElement
        drawSequencer()
    }

    private fun togglePlay() {
        isPlaying = !isPlaying
        if (isPlaying) {
            startAnimation()
            technoPlayer.startTechno()
        } else {
            stopAnimation()
            technoPlayer.stopTechno()
        }
    }

    private fun startAnimation() {
        val interval = (60.0 / bpm) * 1000 / MAX_DIVISIONS
        var lastTime = 0.0

        fun animate(time: Double) {
            if (time - lastTime >= interval) {
                rotation = (rotation + (360.0 / MAX_DIVISIONS)) % 360.0
                lastTime = time
                drawSequencer()
                playActiveNotes()
            }
            animationId = requestAnimationFrame { animate(it) }
        }

        animationId = requestAnimationFrame { animate(it) }
    }

    private fun stopAnimation() {
        animationId?.let { cancelAnimationFrame(it) }
    }

    private fun playActiveNotes() {
        val currentDivision = (rotation / (360.0 / MAX_DIVISIONS)).toInt()
        rows.forEachIndexed { index, row ->
            if (row.activeDivisions.contains(currentDivision % row.divisions)) {
                when (row.instrument) {
                    "Bass Drum" -> technoPlayer.audioState?.kickDrum?.play(technoPlayer.audioState?.currentTime() ?: 0.0)
                    "Hi-Hat" -> technoPlayer.audioState?.hiHat?.play(technoPlayer.audioState?.currentTime() ?: 0.0)
                    "Synth" -> technoPlayer.audioState?.synth?.play(technoPlayer.audioState?.currentTime() ?: 0.0, row.note, 3)
                }
            }
        }
    }

    private fun drawSequencer() {
        val ctx = canvas.getContext(CanvasRenderingContext2D.ID)!!

        val centerX = canvas.width / 2.0
        val centerY = canvas.height / 2.0
        val radius = min(centerX, centerY) - 10.0

        ctx.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

        // Draw outer circle
        ctx.beginPath()
        ctx.arc(centerX, centerY, radius, 0.0, 2 * PI)
        ctx.strokeStyle = "white"
        ctx.lineWidth = 2.0
        ctx.stroke()

        // Draw rows and divisions
        rows.forEachIndexed { rowIndex, row ->
            val rowRadius = (radius / TOTAL_ROWS) * (rowIndex + 1)
            val isActive = rowIndex == activeRow

            ctx.beginPath()
            ctx.arc(centerX, centerY, rowRadius, 0.0, 2 * PI)
            ctx.strokeStyle = if (isActive) "yellow" else "white"
            ctx.lineWidth = if (isActive) 2.0 else 1.0
            ctx.stroke()

            if (row.divisions > 0) {
                for (i in 0 until row.divisions) {
                    val angle = (i.toDouble() / row.divisions) * 2 * PI
                    val x1 = centerX + rowRadius * cos(angle)
                    val y1 = centerY + rowRadius * sin(angle)
                    val x2 = centerX + (rowRadius - radius / TOTAL_ROWS) * cos(angle)
                    val y2 = centerY + (rowRadius - radius / TOTAL_ROWS) * sin(angle)

                    ctx.beginPath()
                    ctx.moveTo(x1, y1)
                    ctx.lineTo(x2, y2)
                    ctx.strokeStyle = if (row.activeDivisions.contains(i)) "green" else "gray"
                    ctx.lineWidth = 1.0
                    ctx.stroke()
                }
            }
        }

        // Draw playhead
        val playheadAngle = rotation * PI / 180
        ctx.beginPath()
        ctx.moveTo(centerX, centerY)
        ctx.lineTo(centerX + radius * cos(playheadAngle), centerY + radius * sin(playheadAngle))
        ctx.strokeStyle = "red"
        ctx.lineWidth = 2.0
        ctx.stroke()
    }

    private fun handleDivisionToggle(event: MouseEvent) {
        val rect = canvas.getBoundingClientRect()
        val x = event.clientX - rect.left
        val y = event.clientY - rect.top
        val centerX = canvas.width / 2
        val centerY = canvas.height / 2
        val radius = min(centerX, centerY) - 10

        val angle = atan2(y - centerY, x - centerX)
        val distance = sqrt((x - centerX).pow(2.0) + (y - centerY).pow(2.0))

        val clickedRow = ((distance / radius) * TOTAL_ROWS).toInt().coerceIn(0, TOTAL_ROWS - 1)
        activeRow = clickedRow

        val division = ((angle + PI) / (2 * PI / rows[clickedRow].divisions)).toInt() % rows[clickedRow].divisions

        if (rows[clickedRow].activeDivisions.contains(division)) {
            rows[clickedRow].activeDivisions.remove(division)
        } else {
            rows[clickedRow].activeDivisions.add(division)
        }

        drawSequencer()
    }

    private fun handleDivisionChange(change: Int) {
        activeRow?.let { rowIndex ->
            rows[rowIndex].divisions = (rows[rowIndex].divisions + change).coerceIn(0, MAX_DIVISIONS)
            rows[rowIndex].activeDivisions.clear()
            drawSequencer()
        }
    }

    private fun handleInstrumentChange(instrument: String) {
        activeRow?.let { rowIndex ->
            rows[rowIndex].instrument = Instrument.from(instrument)
        }
    }

    private fun handleNoteChange(change: Int) {
        activeRow?.let { rowIndex ->
            rows[rowIndex].note = (rows[rowIndex].note + change).coerceIn(0, 127)
        }
    }
}

fun FlowContent.circularSequencer() {
    with(Frooty()) {
        render()
    }
}