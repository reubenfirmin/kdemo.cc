package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.components.card

fun FlowContent.sourceCard() {
    card("relative overflow-hidden transform -rotate-2 hover:rotate-0 transition-transform duration-300") {
        div("absolute inset-0 flex flex-col") {
            div("flex-1 bg-gray-800 flex items-center justify-center z-10 shadow-2xl") {
                div("relative") {
                    for (i in 5 downTo 1) {
                        h2("text-3xl font-black uppercase tracking-wider absolute inset-0 flex items-center justify-center whitespace-nowrap") {
                            val color = when (i) {
                                1 -> "text-orange-400"
                                2 -> "text-indigo-500"
                                3 -> "text-blue-600"
                                4 -> "text-cyan-700"
                                else -> "text-teal-700"
                            }
                            classes = classes + color

                            val opacity = 1.2 - (i * 0.21)
                            val translateX = (i - 1) * (3 + (i * 0.3))
                            val translateY = (i - 1) * (3 + (i * 0.3))

                            val outlineStyle = if (i == 1) {
                                "-2px -2px 0 #4a5568, 2px -2px 0 #4a5568, -2px 2px 0 #4a5568, 2px 2px 0 #4a5568, "
                            } else {
                                ""
                            }

                            style = "opacity: $opacity; transform: translate(${translateX}px, ${translateY}px); text-shadow: ${outlineStyle}2px 2px 4px rgba(0,0,0,0.5);"

                            +"Source Code On Github"
                        }
                    }
                }
            }
            div("flex-1 bg-gradient-to-r from-indigo-500 from-2% to-blue-800 to-10% transform flex items-center justify-center") {
                div("transform rotate-2 hover:rotate-2 transition-transform duration-300") {
                    a(classes = "inline-block bg-white hover:-rotate-2 text-blue-900 font-bold py-3 px-8 rounded-full shadow-lg hover:shadow-xl hover:bg-blue-50 active:bg-blue-100 active:shadow-inner transition-all duration-300 border-2 border-blue-200") {
                        href = "https://github.com/reubenfirmin/kdemo.cc"
                        style = "text-shadow: 0.5px 0.5px 1px rgba(0,0,0,0.1);"
                        +"View Source"
                    }
                }
            }
        }

        div("absolute bottom-0 right-0 w-20 h-20") {
            div("absolute transform -rotate-45 bg-gradient-to-r from-orange-400 to-red-600 text-white text-xs font-bold right-[-35px] top-[32px] w-[170px] flex items-center justify-center") {
                style = "box-shadow: 0 3px 10px rgba(0,0,0,0.3); padding: 0.25rem 0;"
                span("inline-block") {
                    style = "margin-right: -0.5rem;"
                    +"SUPER CODE!"
                }
            }
        }
    }
}