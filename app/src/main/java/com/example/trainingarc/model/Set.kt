// Base class (abstract) for all types of sets
sealed class WorkoutSet {
    abstract val exercise: String

    // A helper method to describe the set in a common way (optional)
    open fun describe(): String = exercise
}

// Timed workout set (extends WorkoutSet)
data class TimedWorkoutSet(
    override val exercise: String,
    val durationInSeconds: Int // Duration for timed workouts
) : WorkoutSet() {
    override fun describe(): String = "$exercise for $durationInSeconds seconds"
}

// Bodyweight workout set (only reps, no weight)
data class BodyweightWorkoutSet(
    override val exercise: String,
    val reps: Int // Only reps count
) : WorkoutSet() {
    override fun describe(): String = "$exercise for $reps reps"
}

// Weightlifting workout set (includes weight and reps)
data class WeightliftingSet(
    override val exercise: String,
    val weight: Int,
    val reps: Int
) : WorkoutSet() {
    override fun describe(): String = "$exercise with $weight kg for $reps reps"
}

fun main() {
    // Create a list of sets, with different types
    val workoutSets: List<WorkoutSet> = listOf(
        TimedWorkoutSet("Running", 300),
        BodyweightWorkoutSet("Push-ups", 20),
        WeightliftingSet("Squats", 100, 10)
    )

    // Loop through and print the description of each set
    for (set in workoutSets) {
        println(set.describe())
    }
}
