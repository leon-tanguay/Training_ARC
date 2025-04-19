package com.example.trainingarc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trainingarc.databinding.FragmentHomeBinding
import com.example.trainingarc.model.*

import com.example.trainingarc.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutFeed = WorkoutFeed(
            workouts = listOf(
                Workout(
                    workoutName = "Ultimate Full Body",
                    username = "alex",
                    hourOf = 7,
                    minuteOf = 15,
                    exercises = listOf(
                        Exercise("Push Ups", listOf(
                            Set(0, 20, 0), Set(0, 15, 0)
                        ), type = "Bodyweight"),
                        Exercise("Squats", listOf(
                            Set(0, 10, 100), Set(0, 8, 110)
                        ), type = "Weightlifting"),
                        Exercise("Deadlifts", listOf(
                            Set(0, 5, 120), Set(0, 3, 130)
                        ), type = "Weightlifting"),
                        Exercise("Plank", listOf(
                            Set(60, 0, 0), Set(45, 0, 0)
                        ), type = "Timed"),
                        Exercise("Jumping Jacks", listOf(
                            Set(30, 0, 0)
                        ), type = "Cardio"),
                        Exercise("Lunges", listOf(
                            Set(0, 12, 20), Set(0, 10, 25)
                        ), type = "Weightlifting")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                Workout(
                    workoutName = "Evening Yoga",
                    username = "danny",
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = listOf(
                        Exercise("Downward Dog", listOf(
                            Set(time = 60, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Child's Pose", listOf(
                            Set(time = 90, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Seated Forward Bend", listOf(
                            Set(time = 75, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Cat-Cow", listOf(
                            Set(time = 40, reps = 0, addedWeight = 0)
                        ), type = "Stretching"),
                        Exercise("Bridge Pose", listOf(
                            Set(time = 50, reps = 0, addedWeight = 0)
                        ), type = "Stretching")
                    )
                ),
                // Keep your original 4-sets-or-less workouts
                Workout(
                    workoutName = "Legs & Core",
                    username = "emily",
                    hourOf = 17,
                    minuteOf = 45,
                    exercises = listOf(
                        Exercise("Lunges", listOf(
                            Set(time = 0, reps = 12, addedWeight = 20),
                            Set(time = 0, reps = 10, addedWeight = 25)
                        ), type = "Weightlifting"),
                        Exercise("Sit Ups", listOf(
                            Set(time = 0, reps = 30, addedWeight = 0)
                        ), type = "Bodyweight")
                    )
                )
                // Add others if needed
            )
        )

        val container = binding.root.findViewById<LinearLayout>(R.id.workout_container)
        val inflater = LayoutInflater.from(requireContext())

        for (workout in workoutFeed.workouts) {
            val box = inflater.inflate(R.layout.item_workout_box, binding.workoutContainer, false)

            val titleText = box.findViewById<TextView>(R.id.workout_title)
            val usernameText = box.findViewById<TextView>(R.id.workout_username)
            val highlightContainer = box.findViewById<LinearLayout>(R.id.highlight_container)

            titleText.text = workout.workoutName
            usernameText.text = "by ${workout.username}"

            val maxToShow = 4
            for ((index, exercise) in workout.exercises.withIndex()) {
                if (index >= 4) break

                val line = TextView(requireContext()).apply {
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.black, null))

                    text = android.text.SpannableStringBuilder().apply {
                        // Exercise name (bold)
                        append(android.text.SpannableString("${exercise.name}: ").apply {
                            setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, length, 0)
                        })

                        // "Best set:" (normal)
                        append("Best set - ")

                        // Highlight string (smaller)
                        append(android.text.SpannableString(exercise.highlightString).apply {
                            setSpan(android.text.style.RelativeSizeSpan(0.9f), 0, length, 0)
                        })
                    }
                }

                highlightContainer.addView(line)
            }

            if (workout.exercises.size > maxToShow) {
                val moreText = TextView(requireContext()).apply {
                    text = "..."
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                }
                highlightContainer.addView(moreText)
            }

            binding.workoutContainer.addView(box)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
