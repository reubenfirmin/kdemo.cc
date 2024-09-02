package org.example.pages.blog

import kotlinx.html.*
import org.example.component.badge
import org.example.framework.dom.onSubmit
import web.dom.document
import web.html.HTMLInputElement

class BlogIndex {

    fun TagConsumer<*>.blogIndex() {
        main("ml-8") {
            h1("text-xl") {
                +"A Blog"
            }

            badge {
                +"Type something and press enter!"
            }

            div("mt-4") {
                id = "container"
            }

            form {
                id = "blog-form"
                input(classes = "border-solid border-2") {
                    id = "blog-form-input"
                    type = InputType.text
                    autoFocus = true
                    name = "post"
                }

                onSubmit { event ->
                    event.preventDefault()

                    val containerEl = document.getElementById("container")!!
                    val inputEl = document.getElementById("blog-form-input") as HTMLInputElement

                    val newPost = inputEl.value
                    if (newPost.isNotBlank()) {
                        // TODO can't use dsl here because of using web.dom.document
                        val div = document.createElement("div")
                        div.textContent = newPost
                        containerEl.appendChild(div)
                        inputEl.value = ""
                    }
                }
            }

            a(classes = "pt-4") {
                href = "/"
                +"Back"
            }
        }
    }
}