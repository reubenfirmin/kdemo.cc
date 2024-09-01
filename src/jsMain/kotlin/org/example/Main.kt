package org.example

import kotlinx.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import org.example.component.badge
import org.example.framework.dom.onClick
import org.example.parts.sortableContainer
import org.w3c.dom.HTMLElement

fun main() {
    document.body!!.append.main {
        div {
            h1("text-xl text-red-500") {
                +"Welcome to "
                span("text-blue-500 underline") {
                    +"a KotlinJS Template!"
                }
            }
        }

        div("w-24 mt-4 ml-4") {
            // this is an example of a component being used
            badge {
                +"This is my badge!"
            }
        }

        div("text-green-500 pt-4 ml-16") {
            p("text-sm") {
                +"A dark pink section"
            }
            div {
                id = "toggleUnderline"
                classes = setOf("cursor-pointer", "font-bold")
                +"Click me to toggle underline"

                onClick { _ ->
                    val element = document.getElementById("toggleUnderline") as HTMLElement
                    element.classList.toggle("underline")
                }
            }
        }

        sortableContainer()
    }
}
