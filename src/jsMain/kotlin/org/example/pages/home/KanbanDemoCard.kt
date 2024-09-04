package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card

fun FlowContent.kanbanDemoCard() {
    card("bg-gray-900 rounded-lg overflow-hidden border-2 border-neon-blue transition-all duration-300 hover:border-neon-green") {
        div("p-6 relative") {
            div("absolute inset-0 bg-neon-blue opacity-10")
            div("relative z-10") {
                h2("text-3xl font-extrabold mb-4 text-neon-blue neon-blue-glow") {
                    +"Kanban Demo"
                }

                p("text-gray-300 mb-6 border-l-4 border-neon-green pl-4") {
                    +"A basic kanban board. Demonstrates integrating libraries from NPM (in this case, sortable.js)."
                }

                a(classes = "inline-block bg-neon-green text-gray-900 font-bold py-2 px-6 rounded transition-all duration-300 hover:bg-neon-blue hover:text-gray-900 neon-green-glow shadow-black") {
                    href = "/kanban"
                    +"View Demo"
                }
            }
        }

        div("h-1 bg-neon-green neon-green-glow")
    }
}