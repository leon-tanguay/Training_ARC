package com.example.trainingarc.model

import java.time.LocalDateTime

data class Exercise(
    val name: String,
    val sets: List<Set>,
    val type: String,
    val startTime: LocalDateTime = LocalDateTime.now() // default to current time
) {
    val totalTime: Int
        get() = sets.sumOf { it.time }

    val totalReps: Int
        get() = sets.sumOf { it.reps }

    val totalVolume: Int
        get() = sets.sumOf { it.addedWeight * it.reps }

    val highlightString: String
        get() {
            val bestSet = when {
                sets.any { it.addedWeight > 0 } ->
                    sets.maxByOrNull { it.addedWeight * (1 + it.reps / 30.0) }
                sets.all { it.reps > 0 } ->
                    sets.maxByOrNull { it.reps }
                else ->
                    sets.maxByOrNull { it.time }
            }

            return when {
                bestSet == null -> "No sets"
                bestSet.addedWeight > 0 -> """"${bestSet.addedWeight} x ${bestSet.reps}""""
                bestSet.reps > 0 -> """"${bestSet.reps} reps""""
                else -> """"${bestSet.time} sec""""
            }
        }
}
