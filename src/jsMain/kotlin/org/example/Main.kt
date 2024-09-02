package org.example

import org.example.framework.Router
import org.example.pages.blog.BlogIndex
import org.example.pages.client.BookSearch
import org.example.pages.home.Index
import org.example.pages.oscilloscope.Oscilloscope
import kotlin.js.Promise


fun main() {
    val index = Index()
    val blogIndex = BlogIndex()
    val bookSearch = BookSearch()
    val oscilloscope = Oscilloscope()

    Router.route("/") {
            Promise { resolve, _ ->
                with(index) {
                    home()
                }
                resolve(Unit)
            }
        }
        .route("/blog") {
            Promise { resolve, _ ->
                with(blogIndex) {
                    blogIndex()
                }
                resolve(Unit)
            }
        }
        .route("/book-search") {
            Promise { resolve, _ ->
                with(bookSearch) {
                    render()
                }
                resolve(Unit)
            }
        }
        .route("/scope") {
            Promise { resolve, _ ->
                with(oscilloscope) {
                    oscilloscopeDemo()
                }
                resolve(Unit)
            }
        }
        .start()
}
