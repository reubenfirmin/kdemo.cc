package org.example.pages.home

import kotlinx.html.*
import org.example.pages.home.card1.textStyleCard
import org.example.pages.home.card2.animatedBackground
import org.example.pages.home.card2.buttonClickCard
import org.example.pages.home.card2.maskedBackgroundLayer
import org.example.pages.home.card3.chartCard

class Index {

    fun TagConsumer<*>.home() {
        div("min-h-screen bg-gray-900 text-gray-100 relative") {

            animatedBackground()

            maskedBackgroundLayer()

            div("relative z-10 min-h-screen") {

                // Navigation bar
                nav("bg-gray-800 shadow-lg opacity-100 z-5") {
                    div("container mx-auto px-6 py-3 flex justify-between items-center") {
                        a(href = "/", classes = "font-semibold text-xl text-white") {
                            +"Dashboard"
                        }
                        div("flex space-x-4") {
                            listOf(
                                "Blog" to "/blog",
                                "Book Search" to "/book-search",
                                "Scope" to "/scope",
                                "Kanban" to "/kanban"
                            ).forEach { (name, path) ->
                                a(
                                    href = path,
                                    classes = "text-gray-300 hover:text-white transition duration-150 ease-in-out"
                                ) {
                                    +name
                                }
                            }
                        }
                    }
                }

                main("container mx-auto px-6 py-8") {
                    h1("text-3xl font-semibold text-white mb-6") {
                        +"Interactive Demos"
                    }

                    div("grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6") {

                        textStyleCard()
                        buttonClickCard()
                        sourceCard()
                        blogDemoCard()
                        chartCard()
                        bookSearchCard()
                        scopeDemoCard()
                        kanbanDemoCard()
                    }
                }
            }
        }
    }

}