package org.example.audio.grid

import org.example.audio.instrument.InstrumentId

data class GridRow(
    val instrument: InstrumentId,
    val divisions: List<GridEvent?>,
    var lastScheduledTime: Double = 0.0,
    var lastIdx: Int = 0
)

data class GridEvent(
    val note: Int,
    val velocity: Int,
    val octave: Int
)