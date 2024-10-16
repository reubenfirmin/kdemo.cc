package org.example.pages.home.svgmaskcard

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import org.example.framework.tags.svg
import web.dom.Element
import web.dom.document
import web.html.HTMLElement
import web.svg.SVGElement

private const val maskedId = "masked-background"

fun FlowContent.maskedBackgroundLayer() {
    div("fixed inset-0") {
        id = maskedId
        svg {
            attributes["class"] = "w-full h-full"
            attributes["viewBox"] = "0 0 100 100"
            attributes["preserveAspectRatio"] = "none"

            defs {
                mask {
                    id = "card-mask"
                    rect {
                        x = "0"
                        y = "0"
                        width = "100"
                        height = "100"
                        fill = "white"
                    }
                }
            }

            rect {
                x = "0"
                y = "0"
                width = "100"
                height = "100"
                fill = "#111827"  // Match bg-gray-900
                attributes["mask"] = "url(#card-mask)"
            }
        }
    }
}

fun createMaskHole(cardId: String): Element {
    val card = document.getElementById(cardId) as HTMLElement
    val rect = card.getBoundingClientRect()

    val svg = document.querySelector("#$maskedId svg") as SVGElement
    val svgRect = svg.getBoundingClientRect()

    // Calculate position relative to the SVG
    val x = (rect.left - svgRect.left) / svgRect.width * 100
    val y = (rect.top - svgRect.top) / svgRect.height * 100
    val width = rect.width / svgRect.width * 100
    val height = rect.height / svgRect.height * 100

    return document.createElementNS("http://www.w3.org/2000/svg", "rect").apply {
        id = "hole-$cardId"
        setAttribute("x", x.toString())
        setAttribute("y", y.toString())
        setAttribute("width", width.toString())
        setAttribute("height", height.toString())
        setAttribute("fill", "black")
    }
}