package org.example

import org.example.framework.Router
import org.example.pages.blog.BlogIndex
import org.example.pages.home.Index

fun main() {

    Router.route("/") {
        with (Index()) {
            home()
        }
    }.route("/blog") {
        with (BlogIndex()) {
            blogIndex()
        }
    }.start()

}
