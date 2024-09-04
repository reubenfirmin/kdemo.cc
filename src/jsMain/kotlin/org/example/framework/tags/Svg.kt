package org.example.framework.tags

import kotlinx.html.*
import kotlinx.html.attributes.Attribute
import kotlinx.html.attributes.StringAttribute

inline fun FlowContent.svg(classes: String? = null, crossinline block: SvgTag.() -> Unit = {}) =
    SvgTag(classes, consumer).visit(block)

/**
 * Implementation note: all classes in this hierarchy must have the namespace defined so that they are added with document.createElementNS
 */
open class SvgTag(classes: String?, consumer: TagConsumer<*>) : HTMLTag(
    "svg", consumer, attributesMapOf("class", classes), "http://www.w3.org/2000/svg", false, false), HtmlBlockInlineTag {

        var id: String
        get() = stringAttr[this, "id"]
        set(newValue) {
            stringAttr[this, "id"] = newValue
        }
    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }
    var viewBox: String
        get() = stringAttr[this, "viewBox"]
        set(newValue) {
            stringAttr[this, "viewBox"] = newValue
        }
    var stroke: String
        get() = stringAttr[this, "stroke"]
        set(newValue) {
            stringAttr[this, "stroke"] = newValue
        }
    var xmlns: String
        get() = stringAttr[this, "xmlns"]
        set(newValue) {
            stringAttr[this, "xmlns"] = newValue
        }

    fun rect(block: SvgRect.() -> Unit = {}) =
        SvgRect(consumer).visit(block)

    fun path(block: SvgPath.() -> Unit = {}) =
        SvgPath(consumer).visit(block)

    fun use(block: SvgUse.() -> Unit = {}) =
        SvgUse(consumer).visit(block)

    fun mask(block: SvgMask.() -> Unit = {}) =
        SvgMask(consumer).visit(block)

    fun defs(block: SvgDefs.() -> Unit = {}) =
        SvgDefs(consumer).visit(block)

    fun text(block: SvgText.() -> Unit = {}) =
        SvgText(consumer).visit(block)

    fun circle(block: SvgCircle.() -> Unit = {}) =
        SvgCircle(consumer).visit(block)

    var width: String
        get() = stringAttr[this, "width"]
        set(newValue) {
            stringAttr[this, "width"] = newValue
        }

    var height: String
        get() = stringAttr[this, "height"]
        set(newValue) {
            stringAttr[this, "height"] = newValue
        }

    fun g(classes: String? = null, block: SvgG.() -> Unit = {}) =
        SvgG(classes, consumer).visit(block)

}


class SvgPath(consumer: TagConsumer<*>) : HTMLTag("path", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {

    var id: String
        get() = stringAttr[this, "id"]
        set(newValue) {
            stringAttr[this, "id"] = newValue
        }

    var d: String
        get() = stringAttr[this, "d"]
        set(newValue) {
            stringAttr[this, "d"] = newValue
        }

    var transform: String
        get() = stringAttr[this, "transform"]
        set(newValue) {
            stringAttr[this, "transform"] = newValue
        }

    var stroke: String
        get() = stringAttr[this, "stroke"]
        set(newValue) {
            stringAttr[this, "stroke"] = newValue
        }

    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }

    var fillRule: String
        get() = stringAttr[this, "fill-rule"]
        set(newValue) {
            stringAttr[this, "fill-rule"] = newValue
        }

    var clipRule: String
        get() = stringAttr[this, "clip-rule"]
        set(newValue) {
            stringAttr[this, "clip-rule"] = newValue
        }

    var strokeLinecap: String
        get() = stringAttr[this, "stroke-linecap"]
        set(newValue) {
            stringAttr[this, "stroke-linecap"] = newValue
        }

    var strokeLinejoin: String
        get() = stringAttr[this, "stroke-linejoin"]
        set(newValue) {
            stringAttr[this, "stroke-linejoin"] = newValue
        }

    var strokeWidth: String
        get() = stringAttr[this, "stroke-width"]
        set(newValue) {
            stringAttr[this, "stroke-width"] = newValue
        }
}
class SvgUse(consumer: TagConsumer<*>) : HTMLTag("use", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {

    var x: String
        get() = stringAttr[this, "x"]
        set(newValue) {
            stringAttr[this, "x"] = newValue
        }
    var y: String
        get() = stringAttr[this, "y"]
        set(newValue) {
            stringAttr[this, "y"] = newValue
        }
    var transform: String
        get() = stringAttr[this, "transform"]
        set(newValue) {
            stringAttr[this, "transform"] = newValue
        }

    var width: String
        get() = stringAttr[this, "width"]
        set(newValue) {
            stringAttr[this, "width"] = newValue
        }

    var height: String
        get() = stringAttr[this, "height"]
        set(newValue) {
            stringAttr[this, "height"] = newValue
        }
}

class SvgRect(consumer: TagConsumer<*>) : HTMLTag("rect", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    var x: String
        get() = stringAttr[this, "x"]
        set(newValue) {
            stringAttr[this, "x"] = newValue
        }
    var y: String
        get() = stringAttr[this, "y"]
        set(newValue) {
            stringAttr[this, "y"] = newValue
        }
    var width: String
        get() = stringAttr[this, "width"]
        set(newValue) {
            stringAttr[this, "width"] = newValue
        }
    var height: String
        get() = stringAttr[this, "height"]
        set(newValue) {
            stringAttr[this, "height"] = newValue
        }
    var rx: String
        get() = stringAttr[this, "rx"]
        set(newValue) {
            stringAttr[this, "rx"] = newValue
        }
    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }
    var stroke: String
        get() = stringAttr[this, "stroke"]
        set(newValue) {
            stringAttr[this, "stroke"] = newValue
        }
    var strokeWidth: String
        get() = stringAttr[this, "stroke-width"]
        set(newValue) {
            stringAttr[this, "stroke-width"] = newValue
        }
}


class SvgMask(consumer: TagConsumer<*>) : HTMLTag("mask", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    var id: String
        get() = stringAttr[this, "id"]
        set(newValue) {
            stringAttr[this, "id"] = newValue
        }

    var style: String
        get() = stringAttr[this, "style"]
        set(newValue) {
            stringAttr[this, "style"] = newValue
        }

    var maskunits: String
        get() = stringAttr[this, "maskunits"]
        set(newValue) {
            stringAttr[this, "maskunits"] = newValue
        }

    var x: String
        get() = stringAttr[this, "x"]
        set(newValue) {
            stringAttr[this, "x"] = newValue
        }

    var y: String
        get() = stringAttr[this, "y"]
        set(newValue) {
            stringAttr[this, "y"] = newValue
        }

    var width: String
        get() = stringAttr[this, "width"]
        set(newValue) {
            stringAttr[this, "width"] = newValue
        }

    var height: String
        get() = stringAttr[this, "height"]
        set(newValue) {
            stringAttr[this, "height"] = newValue
        }


    fun rect(block: SvgRect.() -> Unit = {}) =
        SvgRect(consumer).visit(block)

}


class SvgG(classes: String?, consumer: TagConsumer<*>) : HTMLTag("g", consumer, attributesMapOf("class", classes), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {

    fun use(block: SvgUse.() -> Unit = {}) =
        SvgUse(consumer).visit(block)

    var id: String
        get() = stringAttr[this, "id"]
        set(newValue) {
            stringAttr[this, "id"] = newValue
        }
    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }
    var filter: String
        get() = stringAttr[this, "filter"]
        set(newValue) {
            stringAttr[this, "filter"] = newValue
        }

    var mask: String
        get() = stringAttr[this, "mask"]
        set(newValue) {
            stringAttr[this, "mask"] = newValue
        }

    fun path(block: SvgPath.() -> Unit = {}) =
        SvgPath(consumer).visit(block)

    fun rect(block: SvgRect.() -> Unit = {}) =
        SvgRect(consumer).visit(block)

    fun g(block: SvgG.() -> Unit = {}) =
        SvgG(null, consumer).visit(block)

    fun line(block: SvgLine.() -> Unit = {}) =
        SvgLine(consumer).visit(block)

    fun circle(block: SvgCircle.() -> Unit = {}) =
        SvgCircle(consumer).visit(block)


}

class SvgDefs(consumer: TagConsumer<*>) : HTMLTag("defs", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    fun filter(block: SvgFilter.() -> Unit = {}) =
        SvgFilter(consumer).visit(block)

    fun lineargradient(block: SvgLinearGradient.() -> Unit = {}) =
        SvgLinearGradient(consumer).visit(block)

    fun path(block: SvgPath.() -> Unit = {}) =
        SvgPath(consumer).visit(block)

    fun mask(block: SvgMask.() -> Unit = {}) =
        SvgMask(consumer).visit(block)

    class SvgFilter(consumer: TagConsumer<*>) :
        HTMLTag("filter", consumer, emptyMap(), inlineTag = false, emptyTag = false) {

        var id: String
            get() = stringAttr[this, "id"]
            set(newValue) {
                stringAttr[this, "id"] = newValue
            }

        var x: String
            get() = stringAttr[this, "x"]
            set(newValue) {
                stringAttr[this, "x"] = newValue
            }

        var y: String
            get() = stringAttr[this, "y"]
            set(newValue) {
                stringAttr[this, "y"] = newValue
            }

        var width: String
            get() = stringAttr[this, "width"]
            set(newValue) {
                stringAttr[this, "width"] = newValue
            }

        var height: String
            get() = stringAttr[this, "height"]
            set(newValue) {
                stringAttr[this, "height"] = newValue
            }

        var filterunits: String
            get() = stringAttr[this, "filterunits"]
            set(newValue) {
                stringAttr[this, "filterunits"] = newValue
            }


        fun feflood(block: SvgFilterFeFlood.() -> Unit = {}) =
            SvgFilterFeFlood(consumer).visit(block)

        fun fecolormatrix(block: SvgFilterFeColorMatrix.() -> Unit = {}) =
            SvgFilterFeColorMatrix(consumer).visit(block)

        fun feoffset(block: SvgFilterFeOffset.() -> Unit = {}) =
            SvgFilterFeOffset(consumer).visit(block)

        fun feblend(block: SvgFilterFeBlend.() -> Unit = {}) =
            SvgFilterFeBlend(consumer).visit(block)

        class SvgFilterFeFlood(consumer: TagConsumer<*>) :
            HTMLTag("feflood", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
            var result: String
                get() = stringAttr[this, "result"]
                set(newValue) {
                    stringAttr[this, "result"] = newValue
                }
        }

        class SvgFilterFeColorMatrix(consumer: TagConsumer<*>) :
            HTMLTag("fecolormatrix", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {

            var `in`: String
                get() = stringAttr[this, "in"]
                set(newValue) {
                    stringAttr[this, "in"] = newValue
                }

            var type: String
                get() = stringAttr[this, "type"]
                set(newValue) {
                    stringAttr[this, "type"] = newValue
                }

            var values: String
                get() = stringAttr[this, "values"]
                set(newValue) {
                    stringAttr[this, "values"] = newValue
                }

            var result: String
                get() = stringAttr[this, "result"]
                set(newValue) {
                    stringAttr[this, "result"] = newValue
                }
        }

        class SvgFilterFeOffset(consumer: TagConsumer<*>) :
            HTMLTag("feoffset", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
            var dy: String
                get() = stringAttr[this, "dy"]
                set(newValue) {
                    stringAttr[this, "dy"] = newValue
                }

        }

        class SvgFilterFeBlend(consumer: TagConsumer<*>) :
            HTMLTag("feblend", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
            var mode: String
                get() = stringAttr[this, "mode"]
                set(newValue) {
                    stringAttr[this, "mode"] = newValue
                }

            var `in`: String
                get() = stringAttr[this, "in"]
                set(newValue) {
                    stringAttr[this, "in"] = newValue
                }

            var in2: String
                get() = stringAttr[this, "in2"]
                set(newValue) {
                    stringAttr[this, "in2"] = newValue
                }

            var result: String
                get() = stringAttr[this, "result"]
                set(newValue) {
                    stringAttr[this, "result"] = newValue
                }
        }
    }


    class SvgLinearGradient(consumer: TagConsumer<*>) :
        HTMLTag("lineargradient", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
        var id: String
            get() = stringAttr[this, "id"]
            set(newValue) {
                stringAttr[this, "id"] = newValue
            }

        var x1: String
            get() = stringAttr[this, "x1"]
            set(newValue) {
                stringAttr[this, "x1"] = newValue
            }

        var y1: String
            get() = stringAttr[this, "y1"]
            set(newValue) {
                stringAttr[this, "y1"] = newValue
            }

        var x2: String
            get() = stringAttr[this, "x2"]
            set(newValue) {
                stringAttr[this, "x2"] = newValue
            }

        var y2: String
            get() = stringAttr[this, "y2"]
            set(newValue) {
                stringAttr[this, "y2"] = newValue
            }

        var gradientunits: String
            get() = stringAttr[this, "gradientunits"]
            set(newValue) {
                stringAttr[this, "gradientunits"] = newValue
            }

        fun stop(block: SvgLinearGradientStop.() -> Unit = {}) =
            SvgLinearGradientStop(consumer).visit(block)

        class SvgLinearGradientStop(consumer: TagConsumer<*>) :
            HTMLTag("stop", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
            var offset: String
                get() = stringAttr[this, "offset"]
                set(newValue) {
                    stringAttr[this, "offset"] = newValue
                }

        }
    }
}

class SvgText(consumer: TagConsumer<*>) : HTMLTag("text", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    var x: String
        get() = stringAttr[this, "x"]
        set(newValue) {
            stringAttr[this, "x"] = newValue
        }
    var y: String
        get() = stringAttr[this, "y"]
        set(newValue) {
            stringAttr[this, "y"] = newValue
        }
    var dx: String
        get() = stringAttr[this, "dx"]
        set(newValue) {
            stringAttr[this, "dx"] = newValue
        }
    var dy: String
        get() = stringAttr[this, "dy"]
        set(newValue) {
            stringAttr[this, "dy"] = newValue
        }
    var textAnchor: String
        get() = stringAttr[this, "text-anchor"]
        set(newValue) {
            stringAttr[this, "text-anchor"] = newValue
        }
    var dominantBaseline: String
        get() = stringAttr[this, "dominant-baseline"]
        set(newValue) {
            stringAttr[this, "dominant-baseline"] = newValue
        }
    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }
    var fontSize: String
        get() = stringAttr[this, "font-size"]
        set(newValue) {
            stringAttr[this, "font-size"] = newValue
        }
    var fontFamily: String
        get() = stringAttr[this, "font-family"]
        set(newValue) {
            stringAttr[this, "font-family"] = newValue
        }
    var fontWeight: String
        get() = stringAttr[this, "font-weight"]
        set(newValue) {
            stringAttr[this, "font-weight"] = newValue
        }
    var transform: String
        get() = stringAttr[this, "transform"]
        set(newValue) {
            stringAttr[this, "transform"] = newValue
        }

    // Add a tspan element for more complex text layouts
    fun tspan(block: SvgTspan.() -> Unit = {}) =
        SvgTspan(consumer).visit(block)
}

class SvgTspan(consumer: TagConsumer<*>) : HTMLTag("tspan", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    var x: String
        get() = stringAttr[this, "x"]
        set(newValue) {
            stringAttr[this, "x"] = newValue
        }
    var y: String
        get() = stringAttr[this, "y"]
        set(newValue) {
            stringAttr[this, "y"] = newValue
        }
    var dx: String
        get() = stringAttr[this, "dx"]
        set(newValue) {
            stringAttr[this, "dx"] = newValue
        }
    var dy: String
        get() = stringAttr[this, "dy"]
        set(newValue) {
            stringAttr[this, "dy"] = newValue
        }
    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }
    var fontSize: String
        get() = stringAttr[this, "font-size"]
        set(newValue) {
            stringAttr[this, "font-size"] = newValue
        }
    var fontFamily: String
        get() = stringAttr[this, "font-family"]
        set(newValue) {
            stringAttr[this, "font-family"] = newValue
        }
    var fontWeight: String
        get() = stringAttr[this, "font-weight"]
        set(newValue) {
            stringAttr[this, "font-weight"] = newValue
        }
}

class SvgCircle(consumer: TagConsumer<*>) : HTMLTag("circle", consumer, emptyMap(), "http://www.w3.org/2000/svg", inlineTag = false, emptyTag = false) {
    var cx: String
        get() = stringAttr[this, "cx"]
        set(newValue) {
            stringAttr[this, "cx"] = newValue
        }

    var cy: String
        get() = stringAttr[this, "cy"]
        set(newValue) {
            stringAttr[this, "cy"] = newValue
        }

    var r: String
        get() = stringAttr[this, "r"]
        set(newValue) {
            stringAttr[this, "r"] = newValue
        }

    var fill: String
        get() = stringAttr[this, "fill"]
        set(newValue) {
            stringAttr[this, "fill"] = newValue
        }

    var stroke: String
        get() = stringAttr[this, "stroke"]
        set(newValue) {
            stringAttr[this, "stroke"] = newValue
        }

    var strokeWidth: String
        get() = stringAttr[this, "stroke-width"]
        set(newValue) {
            stringAttr[this, "stroke-width"] = newValue
        }
}

class SvgLine(consumer: TagConsumer<*>) : HTMLTag("line", consumer, emptyMap(), "http://www.w3.org/2000/svg", false, false) {
    var x1: String
        get() = stringAttr[this, "x1"]
        set(newValue) {
            stringAttr[this, "x1"] = newValue
        }

    var y1: String
        get() = stringAttr[this, "y1"]
        set(newValue) {
            stringAttr[this, "y1"] = newValue
        }

    var x2: String
        get() = stringAttr[this, "x2"]
        set(newValue) {
            stringAttr[this, "x2"] = newValue
        }

    var y2: String
        get() = stringAttr[this, "y2"]
        set(newValue) {
            stringAttr[this, "y2"] = newValue
        }

    var stroke: String
        get() = stringAttr[this, "stroke"]
        set(newValue) {
            stringAttr[this, "stroke"] = newValue
        }

    var strokeWidth: String
        get() = stringAttr[this, "stroke-width"]
        set(newValue) {
            stringAttr[this, "stroke-width"] = newValue
        }
}


internal val stringAttr: Attribute<String> = StringAttribute()
