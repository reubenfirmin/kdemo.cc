package org.example.pages.frooty

import kotlinx.html.*
import org.example.audio.TechnoPlayer
import org.example.audio.instrument.InstrumentId
import org.example.audio.grid.Grid
import org.example.audio.grid.GridRow
import org.example.audio.grid.GridEvent
import org.example.framework.dom.onClick
import org.example.framework.dom.onInput
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

    private var grid = Grid(1, List(TOTAL_ROWS) { GridRow(InstrumentId.BASSDRUM, List(4) { null }) })
    private var activeRow: Int? = null

    private lateinit var canvas: HTMLCanvasElement
    private var animationId: FrameRequestId? = null

    private val technoPlayer = TechnoPlayer()

    private var lastTickTime = 0.0

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
                        value = technoPlayer.bpm().toString()
                        onInput { event ->
                            technoPlayer.updateBPM((event.target as HTMLInputElement).value.toInt())
                        }
                    }
                    span {
                        +"${technoPlayer.bpm()} BPM"
                    }
                }
                div("mb-4") {
                    button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        +if (technoPlayer.isPlaying()) "Pause" else "Play"
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
                                +"${grid.rows[rowIndex].divisions.size}"
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
                            InstrumentId.values().forEach { instrument ->
                                option {
                                    value = instrument.name
                                    selected = instrument == grid.rows[rowIndex].instrument
                                    +instrument.name.replace("_", " ")
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
                                +"${(grid.rows[rowIndex].divisions.firstOrNull { it != null }?.note ?: 48)}"
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
        if (technoPlayer.isPlaying()) {
            technoPlayer.stopTechno()
            stopAnimation()
        } else {
            technoPlayer.startTechno(grid)
            startAnimation()
        }
    }

    private fun startAnimation() {
        technoPlayer.onTick = { rowIndex, time ->
            lastTickTime = time
            drawSequencer()
        }
        animationId = requestAnimationFrame { animate() }
    }

    private fun animate() {
        drawSequencer()
        animationId = requestAnimationFrame { animate() }
    }

    private fun stopAnimation() {
        technoPlayer.onTick = null
        animationId?.let { cancelAnimationFrame(it) }
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
        grid.rows.forEachIndexed { rowIndex, row ->
            val rowRadius = (radius / TOTAL_ROWS) * (rowIndex + 1)
            val isActive = rowIndex == activeRow

            ctx.beginPath()
            ctx.arc(centerX, centerY, rowRadius, 0.0, 2 * PI)
            ctx.strokeStyle = if (isActive) "yellow" else "white"
            ctx.lineWidth = if (isActive) 2.0 else 1.0
            ctx.stroke()

            row.divisions.forEachIndexed { i, event ->
                val angle = (i.toDouble() / row.divisions.size) * 2 * PI
                val x1 = centerX + rowRadius * cos(angle)
                val y1 = centerY + rowRadius * sin(angle)
                val x2 = centerX + (rowRadius - radius / TOTAL_ROWS) * cos(angle)
                val y2 = centerY + (rowRadius - radius / TOTAL_ROWS) * sin(angle)

                ctx.beginPath()
                ctx.moveTo(x1, y1)
                ctx.lineTo(x2, y2)
                ctx.strokeStyle = if (event != null) "green" else "gray"
                ctx.lineWidth = 1.0
                ctx.stroke()
            }
        }

        // Draw playhead
        val secondsPerRevolution = 60.0 / technoPlayer.bpm() * 4 // Assuming 4/4 time
        val playheadAngle = (lastTickTime % secondsPerRevolution) / secondsPerRevolution * 2 * PI
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

        val row = grid.rows[clickedRow]
        val division = ((angle + PI) / (2 * PI / row.divisions.size)).toInt() % row.divisions.size

        val newDivisions = row.divisions.toMutableList()
        if (newDivisions[division] != null) {
            newDivisions[division] = null
        } else {
            newDivisions[division] = GridEvent(48, 127, 3) // Default note, velocity, and octave
        }

        grid = Grid(1, grid.rows.toMutableList().apply {
            set(clickedRow, row.copy(divisions = newDivisions))
        })

        technoPlayer.updateGrid(grid)
        drawSequencer()
    }

    private fun handleDivisionChange(change: Int) {
        activeRow?.let { rowIndex ->
            val row = grid.rows[rowIndex]
            val newSize = (row.divisions.size + change).coerceIn(1, MAX_DIVISIONS)
            val newDivisions = if (newSize > row.divisions.size) {
                row.divisions + List(newSize - row.divisions.size) { null }
            } else {
                row.divisions.take(newSize)
            }

            grid = Grid(1, grid.rows.toMutableList().apply {
                set(rowIndex, row.copy(divisions = newDivisions))
            })

            technoPlayer.updateGrid(grid)
            drawSequencer()
        }
    }

    private fun handleInstrumentChange(instrumentName: String) {
        activeRow?.let { rowIndex ->
            val newInstrument = InstrumentId.valueOf(instrumentName)
            grid = Grid(1, grid.rows.toMutableList().apply {
                set(rowIndex, this[rowIndex].copy(instrument = newInstrument))
            })
            technoPlayer.updateGrid(grid)
        }
    }

    private fun handleNoteChange(change: Int) {
        activeRow?.let { rowIndex ->
            val row = grid.rows[rowIndex]
            val newDivisions = row.divisions.map { event ->
                event?.let { it.copy(note = (it.note + change).coerceIn(0, 127)) } ?: event
            }
            grid = Grid(1, grid.rows.toMutableList().apply {
                set(rowIndex, row.copy(divisions = newDivisions))
            })
            technoPlayer.updateGrid(grid)
            drawSequencer()
        }
    }
}

fun FlowContent.circularSequencer() {
    with(Frooty()) {
        render()
    }
}