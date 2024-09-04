package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card

fun FlowContent.bookSearchCard() {
    card("bg-white border-l-8 border-blue-600 rounded-lg shadow-md hover:shadow-lg transition-all duration-300 flex flex-col justify-between") {
        div("p-6 relative overflow-hidden") {
            div("absolute top-0 right-0 w-32 h-32 bg-blue-100 rounded-full transform translate-x-16 -translate-y-16 opacity-20")

            h2("text-2xl font-bold mb-3 text-blue-800") {
                +"Book Search Demo"
            }

            p("text-gray-600 mb-4") {
                +"An ecommerce store layout. Implements a simple/simplistic http client, and demonstrates handling delayed responses. On-the-fly SVG generation."
            }
        }

        div("flex justify-between px-6 pb-4") {
            a(classes = "inline-block bg-blue-600 text-white font-semibold py-2 px-6 rounded hover:bg-blue-700 transition-colors duration-300 shadow") {
                href = "/book-search"
                +"Explore"
            }
            span("text-xs text-gray-400 italic") {
                +"Interactive Demo"
            }
        }
    }
}
