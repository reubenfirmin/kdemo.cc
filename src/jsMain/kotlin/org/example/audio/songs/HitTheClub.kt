package org.example.audio.songs

import org.example.audio.Note.*
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

        val basslineNotes = listOf(Cs, Cs, Cs, Cs, Cs, Cs, As, Cs)

        val basslinePattern = listOf(
                true, false, true, false,
                true, false, true, false,
                true, false, true, false,
                true, false, true, false)

        val kickRow = GridRow(
            instrument = InstrumentId.BASSDRUM,
            divisions = kickPattern.map { if (it) GridEvent(C, 1.0, 3) else null }
        )

        val hiHatRow = GridRow(
            instrument = InstrumentId.HIHAT,
            divisions = hiHatPattern.map { if (it) GridEvent(C, 1.0, 3) else null }
        )

        val bassRow = GridRow(
            instrument = InstrumentId.SYNTH,
            divisions = basslinePattern.mapIndexed { index, active ->
                if (active) GridEvent(basslineNotes[index % basslineNotes.size], 1.0, 1) else null
            }
        )

        return Grid(1, listOf(kickRow, hiHatRow, bassRow))
    }
}