package org.example.pages.home

import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.append
import org.example.component.badge
import org.example.framework.dom.onClick
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.framework.tags.svg
import web.html.HTMLElement
import web.uievents.MouseEvent

class Index {

    fun TagConsumer<*>.home() {
        main("ml-8") {
            div {
                h1("text-xl text-red-500 mx-8 pt-16") {
                    +"Styling "
                    span("text-white bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 p-4") {
                        // TODO this is required before attaching event handlers...
                        id = "hello"
                        val eventHandler = { event: MouseEvent ->
                            val el = event.target as HTMLElement
                            listOf(
                                "bg-gradient-to-r",
                                "text-white",
                                "text-black",
                                "border-solid",
                                "border-4",
                                "border-black"
                            ).forEach {
                                el.classList.toggle(it)
                            }
                        }

                        onMouseEnter(eventHandler)
                        onMouseLeave(eventHandler)

                        +"demos with KotlinJs & Tailwind"
                    }
                }
            }

            div {
                id = "svgs"
                svg("bg-gray-200") {
                    width = "80"
                    height = "48"

                    viewBox = "0 0 80 48"

                    path {
                        stroke = "#FF0000"
                        strokeLinecap = "round"
                        strokeLinejoin = "round"
                        strokeWidth = "6"
                        d = "M72 40 40 8 8 40"
                    }
                }


            }

            val svg = document.createElementNS("http://www.w3.org/2000/svg", "svg")
            svg.setAttribute("width", "80")
            svg.setAttribute("height", "48")
            svg.setAttribute("viewBox", "0 0 80 48")

            val path = document.createElementNS("http://www.w3.org/2000/svg", "path")
            path.setAttribute("d", "M72 40 40 8 8 40")
            path.setAttribute("stroke", "#0000FF")
            path.setAttribute("stroke-width", "6")
            path.setAttribute("stroke-linecap", "round")
            path.setAttribute("stroke-linejoin", "round")
            svg.appendChild(path)
            document.body!!.appendChild(svg)

            val svgContainer = document.createElement("div")
            svgContainer.innerHTML = """
                <svg class="w-20 h-12 bg-gray-200" xmlns="http://www.w3.org/2000/svg" width="80" height="48" viewBox="0 0 80 48">
                    <path stroke="#00FF00" stroke-linecap="round" stroke-linejoin="round" stroke-width="6" d="M72 40 40 8 8 40" />
                </svg>
            """.trimIndent()
            document.body!!.appendChild(svgContainer)


            div("px-8 pt-8 flex flex-row") {
                // this is an example of a component being used
                badge("w-24 m-4 p-2") {
                    +"This is a component!"
                }

                badge("w-24 m-4 p-2") {
                    +"So is this!"
                }
            }

            div("text-green-500 m-8") {
                p("transition-all duration-500 ease-in-out cursor-pointer font-bold") {
                    id = "toggle_me"

                    +"Click me to toggle classes"

                    onClick { event ->
                        val el = event.target as HTMLElement
                        el.classList.toggle("underline")
                        el.classList.toggle("text-green-500")
                        el.classList.toggle("text-orange-300")
                        el.classList.toggle("blur-sm")
                        el.classList.toggle("text-6xl")
                        el.classList.toggle("skew-y-6")
                    }
                }
            }

            div("inline-block") {
                div("m-4 p-4 border-dashed border-2 border-black inline-block flex flex-row gap-4") {
                    a(classes = "underline text-blue-500") {
                        href = "/blog"
                        +"Blog"
                    }
                    a(classes = "underline text-blue-500") {
                        href = "/book-search"
                        +"Book Search"
                    }
                    a(classes = "underline text-blue-500") {
                        href = "/scope"
                        +"Scope"
                    }
                    a(classes = "underline text-blue-500") {
                        href = "/kanban"
                        +"Kanban"
                    }
                }
            }
        }
    }
}
