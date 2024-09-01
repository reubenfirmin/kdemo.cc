package org.example

import kotlinx.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import org.example.component.badge
import org.example.framework.dom.onClick
import org.example.parts.sortableContainer
import web.html.HTMLElement

fun main() {
    document.body!!.append.main {
        div {
            h1("text-xl text-red-500 mx-8 pt-16") {
                +"This is "
                span("text-white bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 p-4") {
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
                id = "toggleUnderline"
                +"Click me to toggle classes"

                onClick { event ->
                    val element = event.target as HTMLElement
                    element.classList.toggle("underline")
                    element.classList.toggle("text-green-500")
                    element.classList.toggle("text-orange-300")
                    element.classList.toggle("blur-sm")
                    element.classList.toggle("text-6xl")
                    element.classList.toggle("skew-y-6")
                }
            }
        }

        sortableContainer()
    }
}
