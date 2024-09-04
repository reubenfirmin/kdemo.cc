package org.example.pages.home

import kotlinx.html.*
import org.example.component.badge
import org.example.framework.dom.onClick
import org.example.framework.dom.onMount
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.framework.interop.appendTo
import org.example.framework.tags.svg
import web.dom.Element
import web.dom.document
import web.html.HTMLElement
import web.svg.SVGElement
import web.uievents.MouseEvent
import kotlin.random.Random

class Index {

    fun TagConsumer<*>.home() {
        div("min-h-screen bg-gray-900 text-gray-100 relative") {
            // Animated background container
            div("fixed inset-0 pointer-events-none z-0") {
                id = "animated-bg"
                svg {
                    attributes["class"] = "w-full h-full"
                    attributes["viewBox"] = "0 0 100 100"
                    attributes["preserveAspectRatio"] = "none"

                    for (i in 0..100) {
                        circle {
                            val neonColors =
                                listOf("#ff00ff", "#00ff00", "#00ffff", "#ff9900", "#ff0066")

                            cx = Random.nextInt(100).toString()
                            cy = Random.nextInt(100).toString()
                            r = Random.nextInt(10).toString()
                            fill = neonColors[Random.nextInt(neonColors.size)]
                            attributes["class"] = "opacity-50 animate-spin"
                        }
                    }
                }
            }

            // Masked background layer
            div("fixed inset-0") {
                id = "masked-background"
                svg {
                    attributes["class"] = "w-full h-full"
                    attributes["viewBox"] = "0 0 100 100"
                    attributes["preserveAspectRatio"] = "none"

                    defs {
                        mask {
                            id = "card-mask"
                            rect {
                                x = "0"
                                y = "0"
                                width = "100"
                                height = "100"
                                fill = "white"
                            }
                        }
                    }

                    rect {
                        x = "0"
                        y = "0"
                        width = "100"
                        height = "100"
                        fill = "#111827"  // Match bg-gray-900
                        attributes["mask"] = "url(#card-mask)"
                    }
                }
            }

            div("relative z-10 min-h-screen") {

                // Navigation bar
                nav("bg-gray-800 shadow-lg opacity-100 z-5") {
                    div("container mx-auto px-6 py-3 flex justify-between items-center") {
                        a(href = "/", classes = "font-semibold text-xl text-white") {
                            +"Dashboard"
                        }
                        div("flex space-x-4") {
                            listOf(
                                "Blog" to "/blog",
                                "Book Search" to "/book-search",
                                "Scope" to "/scope",
                                "Kanban" to "/kanban"
                            ).forEach { (name, path) ->
                                a(
                                    href = path,
                                    classes = "text-gray-300 hover:text-white transition duration-150 ease-in-out"
                                ) {
                                    +name
                                }
                            }
                        }
                    }
                }

                // Main content

                main("container mx-auto px-6 py-8") {
                    h1("text-3xl font-semibold text-white mb-6") {
                        +"Interactive Demos"
                    }

                    // Grid layout for cards
                    div("grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6") {
                        // Card 1: Text styling demo
                        card {
                            h2("text-xl font-semibold mb-4 text-gray-100") {
                                +"Text Styling Demo"
                            }
                            p("transition-all duration-500 ease-in-out cursor-pointer text-blue-400 transform origin-center text-xl") {
                                id = "text_demo"
                                +"Hover over me to change style"

                                val initialState = listOf("text-blue-400", "text-xl")
                                val toggleState = listOf(
                                    "text-transparent",
                                    "bg-clip-text",
                                    "bg-gradient-to-r",
                                    "from-green-400",
                                    "to-yellow-400",
                                    "text-6xl",
                                    "font-bold",
                                    "z-50",
                                    "uppercase",
                                    "tracking-widest",
                                    "-skew-y-12"
                                )

                                onMouseEnter { event ->
                                    val el = event.target as HTMLElement
                                    initialState.forEach { el.classList.remove(it) }
                                    toggleState.forEach { el.classList.add(it) }
                                    el.style.textShadow =
                                        "0 0 20px rgba(0, 255, 0, 0.7), 0 0 30px rgba(0, 255, 0, 0.5), 0 0 40px rgba(0, 255, 0, 0.3)"
                                }

                                onMouseLeave { event ->
                                    val el = event.target as HTMLElement
                                    initialState.forEach { el.classList.add(it) }
                                    toggleState.forEach { el.classList.remove(it) }
                                    el.style.textShadow = "none"
                                }
                            }
                        }

                        // Card 2: Button demo
                        val cardId = "button_demo_card"
                        card {
                            id = cardId
                            h2("text-xl font-semibold mb-4 text-gray-100") {
                                +"Button Demo"
                            }

                            onMount {
                                val cardEl = document.getElementById(cardId)!!
                                val rect = cardEl.getBoundingClientRect()
                                val buttonTop = rect.bottom - 50
                                val buttonLeft = rect.x + 16

                                document.body.appendTo().button(classes = "fixed bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded " +
                                        "transition duration-300 ease-in-out z-50") {
                                    id = "click_me_$cardId"
                                    style = "top: ${buttonTop}px; left: ${buttonLeft}px;"

                                    +"Click me"
                                    onClick { event ->
                                        val el = event.target as HTMLElement
                                        el.classList.toggle("bg-green-600")
                                        el.classList.toggle("bg-blue-600")
                                        el.textContent = if (el.classList.contains("bg-green-600")) "Awesome!" else "Click me"

                                        val card = document.getElementById(cardId)!!
                                        val mask = document.getElementById("card-mask") as Element

                                        if (el.classList.contains("bg-green-600")) {
                                            mask.appendChild(createMaskHole(cardId))
                                            card.classList.add("opacity-0")
                                        } else {
                                            mask.querySelector("#hole-$cardId")?.remove()
                                            card.classList.remove("opacity-0")
                                        }
                                    }
                                }
                            }
                        }



                        // Card 3: Chart demo
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
                }
            }
        }
    }

    private fun createMaskHole(cardId: String): Element {
        val card = document.getElementById(cardId) as HTMLElement
        val rect = card.getBoundingClientRect()

        val svg = document.querySelector("#masked-background svg") as SVGElement
        val svgRect = svg.getBoundingClientRect()

        // Calculate position relative to the SVG
        val x = (rect.left - svgRect.left) / svgRect.width * 100
        val y = (rect.top - svgRect.top) / svgRect.height * 100
        val width = rect.width / svgRect.width * 100
        val height = rect.height / svgRect.height * 100

        return document.createElementNS("http://www.w3.org/2000/svg", "rect").apply {
            id = "hole-$cardId"
            setAttribute("x", x.toString())
            setAttribute("y", y.toString())
            setAttribute("width", width.toString())
            setAttribute("height", height.toString())
            setAttribute("fill", "black")
        }
    }

    private fun TagConsumer<*>.card(block: DIV.() -> Unit) {
        div("bg-gray-800 rounded-lg shadow-md p-6") {
            block()
        }
    }
}