package org.example.pages.blog

import kotlinx.html.*
import org.example.App
import org.example.framework.dom.onSubmit
import org.example.framework.interop.appendTo
import web.dom.document
import web.html.HTMLInputElement
import org.example.framework.dom.onClick
import web.html.HTMLElement
import kotlin.js.Date

class BlogIndex {

    fun TagConsumer<*>.blogIndex() {
        main("max-w-2xl mx-auto p-6 font-sans") {
            h1("text-3xl font-bold mb-6 text-gray-800") {
                +"Micro Blog"
            }

            div("mt-6 space-y-4") {
                id = "container"
            }

            form("mt-6") {
                div("flex") {
                    input(classes = "flex-grow p-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500") {
                        id = "blog-form-input"
                        type = InputType.text
                        autoFocus = true
                        name = "post"
                        placeholder = "What's on your mind?"
                    }
                    button(classes = "bg-blue-500 text-white px-4 py-2 rounded-r-md hover:bg-blue-600 transition duration-200") {
                        type = ButtonType.submit
                        +"Post"
                    }
                }

                onSubmit { event ->
                    event.preventDefault()

                    val containerEl = document.getElementById("container")!!
                    val inputEl = document.getElementById("blog-form-input") as HTMLInputElement

                    val newPost = inputEl.value
                    if (newPost.isNotBlank()) {
                        val timestamp = getCurrentTimestamp()
                        containerEl.appendTo().div("bg-white p-4 rounded-md shadow-md") {
                            div("flex justify-between items-center mb-2") {
                                span("text-sm text-gray-500") { +timestamp }
                                button(classes = "text-red-500 hover:text-red-700") {
                                    +"Delete"
                                    onClick { mouseEvent ->
                                        (mouseEvent.currentTarget as HTMLElement).closest("div.bg-white")?.remove()
                                    }
                                }
                            }
                            p("text-gray-800") {
                                +newPost
                            }
                        }
                        inputEl.value = ""
                    }
                }
            }

            a(classes = "block mt-8 text-blue-500 hover:underline") {
                href = "/"
                +"Back to Home"
            }
        }
    }

    fun TagConsumer<*>.blogPost(month: String, day: String, year: String) {
        h1 {
            +"$month $day $year"
        }
        a(classes = "underline pointer") {
            //  demo, not worth complicating with actual date logic :)
            href = App.blog.postOnDate.path((month.toInt() + 1).toString(), day, year)
            // this would throw an exception, because we didn't supply the needed params
            //href = App.blog.date.path()
            +"Next"
        }
    }

    private fun getCurrentTimestamp(): String {
        val now = Date()
        return "${now.toLocaleDateString()} ${now.toLocaleTimeString()}"
    }
}