package org.example.pages.home

import kotlinx.html.*
import org.example.component.badge
import org.example.framework.dom.onClick
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import web.html.HTMLElement
import web.uievents.MouseEvent

class Index {

    fun TagConsumer<*>.home() {
        main {
            div {
                h1("text-xl text-red-500 mx-8 pt-16") {
                    +"This is "
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

                        +"a KotlinJS Template Project!"
                    }
                }
            }

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

            sortableContainer()

            div("inline-block") {
                div("m-4 p-4 border-dashed border-2 border-black inline-block flex flex-row gap-4") {
                    a(classes = "underline text-blue-500") {
                        href = "/blog"
                        +"Blog"
                    }
                    a(classes = "underline text-blue-500") {
                        href = "/client"
                        +"Client"
                    }
                    a(classes = "underline text-blue-500") {
                        href = "/scope"
                        +"Scope"
                    }
                }
            }
        }
    }
}
