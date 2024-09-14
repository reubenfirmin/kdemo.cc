package org.example.audio

import org.example.audio.grid.Grid
import org.example.audio.grid.GridEvent
import org.example.audio.grid.GridRow
import org.example.audio.instrument.InstrumentId

object HitTheClub {
    fun createGrid(): Grid {

        val kickPattern =
            listOf(
                true, false, false, false,
                true, false, false, false,
                true, false, false, false,
                true, false, false, false)

        val hiHatPattern =
            listOf(
                false, false, true, false,
                false, false, true, false,
                false, false, true, false,
                false, false, true, false)

        val basslineNotes = listOf(45, 45, 45, 45, 45, 45, 50, 45)

        val basslinePattern = listOf(
                true, false, true, false,
                true, false, true, false,
                true, false, true, false,
                true, false, true, false)

        val kickRow = GridRow(
            instrument = InstrumentId.BASSDRUM,
            divisions = kickPattern.map { if (it) GridEvent(60, 127, 3) else null }
        )

        val hiHatRow = GridRow(
            instrument = InstrumentId.HIHAT,
            divisions = hiHatPattern.map { if (it) GridEvent(60, 127, 3) else null }
        )

        val bassRow = GridRow(
            instrument = InstrumentId.SYNTH,
            divisions = basslinePattern.mapIndexed { index, active ->
                if (active) GridEvent(basslineNotes[index % basslineNotes.size], 127, 3) else null
            }
        )

        return Grid(1, listOf(kickRow, hiHatRow, bassRow))
    }
}