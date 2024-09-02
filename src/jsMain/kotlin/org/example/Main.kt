package org.example

import org.example.framework.Router
import org.example.pages.blog.BlogIndex
import org.example.pages.home.Index

fun main() {

    val index = Index()
    val blogIndex = BlogIndex()

    Router.route("/") {
        with (index) {
            home()
        }
    }.route("/blog") {
        with (blogIndex) {
            blogIndex()
        }
    }.start()

}
