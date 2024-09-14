package org.example.audio.instrument

import web.audio.AudioTimestamp

interface Instrument {

    fun scheduledEventTimes(): List<Double>
}