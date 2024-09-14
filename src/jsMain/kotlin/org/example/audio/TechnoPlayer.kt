package org.example.audio

import org.example.audio.grid.Grid
import web.audio.AnalyserNode
import web.timers.Interval
import web.timers.clearInterval
import web.timers.setInterval

class TechnoPlayer {

    private var audioState: AudioState? = null
    private var isPlaying = false
    private var grid = Grid(1, listOf())
    private var bpm = 120
    private var clockInterval: Interval? = null
    private val scheduleAheadTime = 0.1 // Look 100ms ahead

    var onTick: ((Int, Double) -> Unit)? = null

    fun startTechno(newGrid: Grid): AnalyserNode {
        if (audioState == null) {
            audioState = AudioState()
        } else {
            console.log("Resuming")
            audioState!!.resume()
        }

        grid = newGrid

        grid.rows.forEach { row ->
            row.lastScheduledTime = audioState!!.currentTime() + (scheduleAheadTime * 3)
            row.lastIdx = row.divisions.size - 1
        }
        isPlaying = true
        startClock()
        return audioState!!.analyser
    }

    fun stopTechno() {
        audioState?.disconnect()
        isPlaying = false
        stopClock()
    }

    private fun startClock() {
        stopClock() // Ensure any existing clock is stopped
        clockInterval = setInterval({
            if (isPlaying) {
                scheduleNotes()
            }
        }, 25) // Check every 25ms
    }

    private fun stopClock() {
        clockInterval?.let { interval ->
            clearInterval(interval)
        }
        clockInterval = null
    }

    private fun scheduleNotes() {
        val currentTime = audioState!!.currentTime()
        val scheduleUntil = currentTime + scheduleAheadTime

        val secondsPerBeat = 60.0 / bpm
        val secondsPerBar = secondsPerBeat * 4 // Assuming 4 beats per measure

        grid.rows.forEachIndexed { rowIndex, row ->
            val divisionsPerBar = row.divisions.size / grid.bars
            val secondsPerDivision = secondsPerBar / divisionsPerBar

            var nextTickTime = row.lastScheduledTime + secondsPerDivision

            while (isPlaying && nextTickTime < scheduleUntil) {
                val divisionIndex = (row.lastIdx + 1) % row.divisions.size

                row.divisions[divisionIndex]?.let { event ->
                    if (audioState!!.sequence(row.instrument, nextTickTime, event)) {
                        console.log("$currentTime || ${row.instrument} $secondsPerDivision $divisionIndex $nextTickTime")
                        onTick?.invoke(rowIndex, nextTickTime)
                    }
                }
                row.lastScheduledTime = nextTickTime
                row.lastIdx = divisionIndex

                nextTickTime += secondsPerDivision
            }
        }
    }

    fun isPlaying() = isPlaying

    fun updateGrid(newGrid: Grid) {
        grid = newGrid
    }

    fun updateBPM(newBpm: Int) {
        bpm = newBpm
    }

    fun bpm() = bpm
}