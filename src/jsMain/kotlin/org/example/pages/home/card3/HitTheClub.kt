package org.example.pages.home.card3

class HitTheClub(
    private val kickDrum: KickDrum,
    private val hiHat: HiHat,
    private val tb303: TB303
) {
    private val bassline = listOf(
        45, 45, 45, 57, 45, 45, 45, 52, // A
        45, 45, 45, 57, 45, 45, 45, 59  // B
    )
    private var bar = 0

    fun grid(beatCount: Int, time: Double) {
        val add = bar * 8
        when (beatCount % 16) {
            0 -> {
                kickDrum.play(time)
                tb303.play(time, bassline[add + 0], 3)
            }
            1 -> {
                // Empty beat
            }
            2 -> {
                hiHat.play(time)
                tb303.play(time, bassline[add + 1], 3)
            }
            3 -> {
                // Empty beat
            }
            4 -> {
                kickDrum.play(time)
                tb303.play(time, bassline[add + 2], 3)
            }
            5 -> {
                hiHat.play(time)
            }
            6 -> {
                hiHat.play(time)
                tb303.play(time, bassline[add + 3], 3)
            }
            7 -> {
                // Empty beat
            }
            8 -> {
                kickDrum.play(time)
                tb303.play(time, bassline[add + 4], 3)
            }
            9 -> {
                // Empty beat
            }
            10 -> {
                hiHat.play(time)
                tb303.play(time, bassline[add + 5], 3)
            }
            11 -> {
                // Empty beat
            }
            12 -> {
                kickDrum.play(time)
                tb303.play(time, bassline[add + 6], 3)
            }
            13 -> {
                // Empty beat
            }
            14 -> {
                hiHat.play(time)
                if (beatCount % 64 == 62) {
                    tb303.play(time, bassline[add + 7] + 12, 3)  // High note for emphasis
                } else {
                    tb303.play(time, bassline[add + 7], 3)
                }
            }
            15 -> {
                // Empty beat
                bar = if (bar == 0) 1 else 0
            }
        }

        // Add some swing with occasional off-beat notes
        if (beatCount % 4 == 3) {
            tb303.play(time, bassline[(add + (beatCount / 4) % 8)], 3)
        }
    }
}