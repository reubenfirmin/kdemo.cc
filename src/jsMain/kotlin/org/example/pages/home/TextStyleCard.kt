package org.example.pages.home

import kotlinx.html.FlowContent
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.p
import org.example.framework.dom.onMouseEnter
import org.example.framework.dom.onMouseLeave
import org.example.pages.home.components.card
import web.html.HTMLElement

fun FlowContent.textStyleCard() {
    card {
        h2("text-xl font-semibold mb-4 text-gray-100") {
            +"Tailwind Styling Demo"
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
}