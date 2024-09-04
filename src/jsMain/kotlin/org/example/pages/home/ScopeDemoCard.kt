package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card


fun FlowContent.scopeDemoCard() {
    card("bg-gradient-to-br from-blue-700 to-purple-800 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-300 overflow-hidden") {
        div("p-6") {
            h2("text-2xl font-bold mb-3 text-white") {
                +"Scope Demo"
            }

            p("text-blue-100 mb-4") {
                +"A full screen graphing calculator, using web canvas for rendering."
            }

            a(classes = "inline-block bg-gray-100 text-blue-600 font-semibold py-2 px-4 rounded-full hover:bg-blue-100 transition-colors duration-300 shadow-lg hover:text-blue-900") {
                href = "/scope"
                +"View Demo"
            }
        }

        div("h-2 bg-gradient-to-r from-purple-400 via-red-500 to-pink-500")
    }
}