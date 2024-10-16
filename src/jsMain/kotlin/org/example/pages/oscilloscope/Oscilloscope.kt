package org.example.pages.oscilloscope
import kotlinx.html.*
import org.example.framework.dom.onClick
import web.animations.requestAnimationFrame
import web.canvas.CanvasRenderingContext2D
import web.dom.document
import web.events.EventHandler
import web.html.HTMLCanvasElement
import web.html.HTMLElement
import web.html.HTMLInputElement
import web.window.window
import kotlin.math.*

class Oscilloscope {
    private var canvasContext: CanvasRenderingContext2D? = null
    private val functions = mutableMapOf<String, (Double) -> Double>()
    private val activeFunctions = mutableSetOf<String>()
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var xRange = 20.0  // The range of x values to display

    fun TagConsumer<*>.oscilloscopeDemo() {
        div("w-full h-screen flex flex-col") {
            canvas {
                id = "oscilloscope"
                style = "width: 100%; background-color: black;"
            }
            div("h-20 bg-gray-800 flex items-center justify-between px-4") {
                div("flex space-x-2") {
                    button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        id = "chirpButton"
                        +"Chirp"
                        onClick { toggleFunction("chirp") }
                    }
                    button(classes = "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded") {
                        id = "gaussianButton"
                        +"Gaussian"
                        onClick { toggleFunction("gaussian") }
                    }
                    button(classes = "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded") {
                        id = "logisticButton"
                        +"Logistic"
                        onClick { toggleFunction("logistic") }
                    }
                    button(classes = "bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded") {
                        id = "riemannButton"
                        +"Riemann Zeta"
                        onClick { toggleFunction("riemann") }
                    }
                    button(classes = "bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded") {
                        id = "agnesiButton"
                        +"Witch of Agnesi"
                        onClick { toggleFunction("agnesi") }
                    }
                    button(classes = "bg-orange-500 hover:bg-orange-700 text-white font-bold py-2 px-4 rounded") {
                        id = "weierstrassButton"
                        +"Weierstrass Fractal"
                        onClick { toggleFunction("weierstrass") }
                    }
                }
                div("flex items-center space-x-2") {
                    span("text-white") { +"X Range:" }
                    input(type = InputType.range, classes = "w-64") {
                        id = "rangeSlider"
                        min = "5"
                        max = "100"
                        step = "5"
                        value = "20"
                    }
                    span("text-white w-8 text-center") {
                        id = "rangeValue"
                        +"20"
                    }
                }
            }
        }
        // TODO seems unsafe to run this immediately and yet it works. why
        initOscilloscope()
    }

    private fun initOscilloscope() {
        val canvas = document.getElementById("oscilloscope") as HTMLCanvasElement
        canvasContext = canvas.getContext(CanvasRenderingContext2D.ID)

        // Set canvas size
        updateCanvasSize()

        functions["chirp"] = { x -> chirpFunction(x) }
        functions["gaussian"] = { x -> exp(-(x * x) / 2) }
        functions["logistic"] = { x -> 1 / (1 + exp(-x)) }
        functions["riemann"] = { x -> riemannZetaApproximation(x) }
        functions["agnesi"] = { x -> 8 / (x * x + 4) }
        functions["weierstrass"] = { x -> weierstrassFunction(x) }

        val rangeSlider = document.getElementById("rangeSlider") as HTMLInputElement
        val rangeValueDisplay = document.getElementById("rangeValue") as HTMLElement

        rangeSlider.oninput = EventHandler {
            xRange = rangeSlider.value.toDouble()
            rangeValueDisplay.textContent = xRange.toString()
            draw()
        }

        window.onresize = EventHandler {
            updateCanvasSize()
            draw()
        }

        requestAnimationFrame { draw() }
    }

    private fun updateCanvasSize() {
        val canvas = document.getElementById("oscilloscope") as HTMLCanvasElement
        canvasWidth = window.innerWidth
        canvasHeight = window.innerHeight - 80 // 80px for controls
        canvas.width = canvasWidth
        canvas.height = canvasHeight
    }

    private fun chirpFunction(x: Double): Double {
        return sin(2 * x * x)
    }

    private fun riemannZetaApproximation(x: Double): Double {
        var sum = 0.0
        for (n in 1..100) {
            sum += cos(x * ln(n.toDouble())) / n.toDouble().pow(0.5)
        }
        return sum / 10  // Scaling factor for better visibility
    }

    private fun weierstrassFunction(x: Double): Double {
        var sum = 0.0
        val a = 0.5
        val b = 7.0
        for (n in 0..50) {
            sum += a.pow(n) * cos(b.pow(n) * PI * x)
        }
        return sum
    }

    private fun toggleFunction(name: String) {
        if (name in activeFunctions) {
            activeFunctions.remove(name)
        } else {
            activeFunctions.add(name)
        }
        draw()
    }

    private fun draw() {
        canvasContext?.let { ctx ->
            ctx.clearRect(0.0, 0.0, canvasWidth.toDouble(), canvasHeight.toDouble())

            // Draw x and y axes
            ctx.beginPath()
            ctx.moveTo(0.0, canvasHeight / 2.0)
            ctx.lineTo(canvasWidth.toDouble(), canvasHeight / 2.0)
            ctx.moveTo(canvasWidth / 2.0, 0.0)
            ctx.lineTo(canvasWidth / 2.0, canvasHeight.toDouble())
            ctx.strokeStyle = "#FFFFFF"
            ctx.stroke()

            // Draw grid lines
            ctx.strokeStyle = "rgba(255, 255, 255, 0.2)"
            ctx.beginPath()
            for (i in 1..9) {
                val x = i * canvasWidth / 10.0
                ctx.moveTo(x, 0.0)
                ctx.lineTo(x, canvasHeight.toDouble())
            }
            for (i in 1..7) {
                val y = i * canvasHeight / 8.0
                ctx.moveTo(0.0, y)
                ctx.lineTo(canvasWidth.toDouble(), y)
            }
            ctx.stroke()

            for (name in activeFunctions) {
                drawFunction(name, functions[name] ?: continue)
            }
        }
    }

    private fun drawFunction(name: String, func: (Double) -> Double) {
        canvasContext?.let { ctx ->
            ctx.beginPath()
            ctx.lineWidth = 2.0
            // TODO move this to the function state
            ctx.strokeStyle = when (name) {
                "chirp" -> "#00FFFF"  // Cyan
                "gaussian" -> "#00FF00"  // Bright Green
                "logistic" -> "#FF0000"  // Red
                "riemann" -> "#FFFF00"  // Yellow
                "agnesi" -> "#FF00FF"  // Magenta
                "weierstrass" -> "#FF6600"  // Orange
                else -> "#FFFFFF"  // White
            }

            for (i in 0..canvasWidth) {
                val x = (i - canvasWidth / 2) * xRange / canvasWidth
                val y = -func(x) * canvasHeight / 4 + canvasHeight / 2
                if (i == 0) {
                    ctx.moveTo(i.toDouble(), y)
                } else {
                    ctx.lineTo(i.toDouble(), y)
                }
            }

            ctx.stroke()
        }
    }
}