package com.example.trainingarc.ui.workout

import ExerciseAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.trainingarc.databinding.FragmentWorkoutBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingarc.R


import java.util.*

import com.example.trainingarc.model.Exercise
import com.example.trainingarc.model.Set
import com.example.trainingarc.model.Workout

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private var currentWorkout: Workout? = null
    private var workoutStartTime: Long = 0L
    private val timerHandler = Handler(Looper.getMainLooper())

    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedMillis = System.currentTimeMillis() - workoutStartTime
            val seconds = (elapsedMillis / 1000) % 60
            val minutes = (elapsedMillis / (1000 * 60)) % 60
            val hours = (elapsedMillis / (1000 * 60 * 60))

            binding.workoutTimer.text = String.format("Workout Timer: %02d:%02d:%02d", hours, minutes, seconds)
            timerHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.addWorkoutButton.setOnClickListener {
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)

            // Create the new workout instance
            currentWorkout = Workout(
                workoutName = "Workout at ${"%02d".format(hour)}:${"%02d".format(minute)}",
                username = "demoUser",
                hourOf = hour,
                minuteOf = minute,
                exercises = emptyList()
            )

            // Show new workout UI
            binding.workoutTimer.visibility = View.VISIBLE
            binding.addExerciseSection.visibility = View.VISIBLE // <--- This line is missing
            binding.finishWorkoutButton.visibility = View.VISIBLE
            binding.addWorkoutButton.visibility = View.GONE

            // Hide the "Add New Workout" button
            binding.addWorkoutButton.visibility = View.GONE

            // Start timer
            workoutStartTime = System.currentTimeMillis()
            timerHandler.post(timerRunnable)
        }

        binding.finishWorkoutButton.setOnClickListener {
            // Stop the timer
            timerHandler.removeCallbacks(timerRunnable)

            // Optional: save workout, show summary, etc.
            binding.workoutTimer.text = "Workout finished!"

            // Hide finish button or reset UI
            binding.finishWorkoutButton.visibility = View.GONE
        }

//        binding.addExerciseButton.setOnClickListener {
//            val workout = currentWorkout
//            if (workout != null) {
//                val newExercise = Exercise(
//                    name = "Exercise ${workout.exercises.size + 1}", // temp name
//                    sets = emptyList(),
//                    type = "reps" // or "weight", "time" — adjust as needed
//                )
//
//                // Make a mutable copy and add the new exercise
//                val updatedList = workout.exercises.toMutableList()
//                updatedList.add(newExercise)
//
//                // Replace the workout with updated one
//                currentWorkout = workout.copy(exercises = updatedList)
//            }
//        }

        binding.addExerciseButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(
                com.example.trainingarc.R.layout.dialog_exercise_selector, null
            )

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val exerciseList = listOf(
                "Push Ups", "Squats", "Deadlift", "Plank", "Jump Rope", "Burpees", "Bench Press"
                // Add as many as needed
            )

            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.exerciseRecyclerView)
            val searchView = dialogView.findViewById<SearchView>(R.id.searchView)

            val adapter = ExerciseAdapter(exerciseList) { selected ->
                // User selected an exercise
                val workout = currentWorkout
                if (workout != null) {
                    val newExercise = Exercise(
                        name = selected,
                        sets = emptyList(),
                        type = "reps"
                    )
                    val updatedList = workout.exercises.toMutableList()
                    updatedList.add(newExercise)
                    currentWorkout = workout.copy(exercises = updatedList)
                }
                dialog.dismiss()
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    val filtered = exerciseList.filter {
                        it.contains(newText.orEmpty(), ignoreCase = true)
                    }
                    adapter.filterList(filtered)
                    return true
                }
            })

            dialog.show()
        }


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        timerHandler.removeCallbacks(timerRunnable)
        _binding = null
    }
}
