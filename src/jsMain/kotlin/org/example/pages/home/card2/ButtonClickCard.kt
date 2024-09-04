package org.example.pages.home.card2

import kotlinx.browser.window
import kotlinx.html.*
import org.example.framework.dom.onClick
import org.example.framework.dom.onMount
import org.example.framework.interop.appendTo
import org.example.pages.home.components.card
import web.dom.Element
import web.dom.document
import web.html.HTMLElement

fun FlowContent.buttonClickCard() {
    card("min-h-64 bg-gray-800 rounded-lg shadow-md p-6") {
        val cardId = "button_demo_card"
        id = cardId
        h2("text-xl font-semibold mb-4 text-gray-100") {
            +"Button Demo"
        }

        p("pb-16") {
            +"""When you click on the button, you'll reveal an animated layer. The animation is constantly running in the bottom layer on this dashboard; in front of it is an svg. Clicking the button
                cuts a hole in the svg matching the dimensions and position of this card, allowing you to see what's underneath.""".trimIndent()
        }

        onMount {
            val buttonId = "click_me_$cardId"

            fun updateButtonPosition() {
                val cardEl = document.getElementById(cardId)!!
                val rect = cardEl.getBoundingClientRect()
                val buttonTop = rect.bottom - 50
                val buttonLeft = rect.x + 16

                document.getElementById(buttonId)?.let { button ->
                    button.setAttribute("style", "top: ${buttonTop}px; left: ${buttonLeft}px;")
                }
            }

            document.body.appendTo().button(classes = "fixed bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded " +
                    "transition duration-300 ease-in-out z-50") {
                id = buttonId

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

            // Initial position update
            updateButtonPosition()

            // Add resize event listener
            window.addEventListener("resize", {
                updateButtonPosition()
            })
        }
    }
}