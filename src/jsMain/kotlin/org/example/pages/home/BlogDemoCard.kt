package org.example.pages.home

import kotlinx.html.*
import org.example.App
import org.example.pages.home.components.card

fun FlowContent.blogDemoCard() {
    card("bg-blue-700 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-300 overflow-hidden flex flex-col justify-between") {
        div("h-2 bg-gradient-to-r from-purple-400 via-red-500 to-pink-500")

        div("p-6") {
            h2("text-xl font-semibold mb-4 text-gray-100") {
                +"Blog Demo"
            }

            p("mb-4") {
                +"Super simple micro blogging interface, demonstrating tailwind styling/layout, and basic event handling. Maybe I'll call it Y."
            }
        }
        div("p-6") {
            a(classes = "inline-block bg-gray-100 text-blue-600 font-semibold py-2 px-4 rounded-full hover:bg-blue-100 transition-colors duration-300 shadow-lg hover:text-blue-900") {
                href = App.blog.main.pattern
                +"View Demo"
            }
        }
    }
}