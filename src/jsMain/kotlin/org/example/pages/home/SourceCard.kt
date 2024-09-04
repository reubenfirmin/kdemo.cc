package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card

fun FlowContent.sourceCard() {
    card("relative overflow-hidden transform -rotate-2 hover:rotate-0 transition-transform duration-300") {
        div("absolute inset-0 bg-yellow-400 transform rotate-1")
        div("absolute inset-0 bg-red-500 transform -rotate-1")
        div("relative bg-white p-6 shadow-xl") {
            h2("text-3xl font-black mb-4 text-blue-600 uppercase tracking-wider") {
                style = "text-shadow: 2px 2px 0 #000, -1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000;"
                +"Source Code On Github"
            }

            div("transform rotate-3 hover:rotate-0 transition-transform duration-300") {
                a(classes = "inline-block bg-red-500 text-white font-bold py-3 px-6 rounded-lg shadow-md hover:bg-red-600 transition-colors duration-300") {
                    href = "https://github.com/reubenfirmin/kdemo.cc"
                    style = "text-shadow: 1px 1px 0 #000; box-shadow: 3px 3px 0 #000;"
                    +"View Source"
                }
            }
        }

        div("absolute bottom-0 right-0 w-20 h-20") {
            div("absolute transform -rotate-45 bg-blue-500 text-white text-xs font-bold py-1 right-[-35px] top-[32px] w-[170px] text-center") {
                style = "box-shadow: 0 3px 10px rgba(0,0,0,0.3);"
                +"SUPER CODE!"
            }
        }
    }
}