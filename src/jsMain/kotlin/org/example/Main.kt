package org.example

import org.example.framework.Router
import org.example.pages.blog.BlogIndex
import org.example.pages.client.ClientIndex
import org.example.pages.home.Index
import org.example.pages.oscilloscope.Oscilloscope

fun main() {

    val index = Index()
    val blogIndex = BlogIndex()
    val clientIndex = ClientIndex()
    val oscilloscope = Oscilloscope()

    Router.route("/") {
        with (index) {
            home()
        }
    }.route("/blog") {
        with(blogIndex) {
            blogIndex()
        }
    }.route("/client") {
        with(clientIndex) {
            clientIndex()
        }
    }.route("/scope") {
        with(oscilloscope) {
            oscilloscopeDemo()
        }
    }.start()

}
