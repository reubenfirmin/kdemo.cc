package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card

fun FlowContent.kanbanDemoCard() {
    card("bg-gray-900 rounded-lg overflow-hidden border-2 border-neon-blue transition-all duration-300 hover:border-neon-green") {
        div("p-6 relative") {
            div("absolute inset-0 bg-neon-blue opacity-10")
            div("relative z-10") {
                h2("text-3xl font-extrabold mb-4 text-neon-blue") {
                    style = "text-shadow: 0 0 10px #00FFFF, 0 0 20px #00FFFF, 0 0 30px #00FFFF;"
                    +"Kanban Demo"
                }

                p("text-gray-300 mb-6 border-neon-green pl-4") {
                    +"A basic kanban board. Demonstrates integrating libraries from NPM (in this case, sortable.js)."
                }

                a(classes = "inline-block bg-neon-green text-gray-900 font-bold py-2 px-6 rounded transition-all duration-300 hover:bg-neon-blue hover:text-gray-900") {
                    href = "/kanban"
                    style = "box-shadow: 0 0 10px #FF00FF, 0 0 20px #FF00FF; text-shadow: 0 0 5px rgba(0,0,0,0.5);"
                    +"View Demo"
                }
            }
        }

        div("h-1 bg-neon-blue") {
            style = "box-shadow: 0 0 10px #00FFFF, 0 0 20px #00FFFF;"
        }
    }
}