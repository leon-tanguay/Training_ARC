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

import android.widget.TextView
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.ImageView
import kotlin.concurrent.thread
import android.content.Context
import java.time.LocalDateTime
import android.util.Log

import java.util.*
import com.example.trainingarc.model.AppDatabase

import com.example.trainingarc.model.Exercise
import com.example.trainingarc.model.Profile
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

        val testPerson = Profile("greg", "gregs g's",
            25, 4, emptyList())

        binding.addWorkoutButton.setOnClickListener {
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)

            // Create the new workout instance
            currentWorkout = Workout(
                workoutName = "Workout at ${"%02d".format(hour)}:${"%02d".format(minute)}",
                userId = -1,
                hourOf = hour,
                minuteOf = minute,
                exercises = emptyList(),
                startDateTime = LocalDateTime.now()
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

        fun updateSetLabels(container: LinearLayout) {
            for (i in 0 until container.childCount) {
                val row = container.getChildAt(i)
                val label = row.findViewById<TextView>(R.id.setLabel)
                label.text = "Set ${i + 1}"
            }
        }

        fun addSetRow(setContainer: LinearLayout, exerciseBlock: View) {
            val setRow = layoutInflater.inflate(R.layout.item_set_row, setContainer, false)

            val deleteButton = setRow.findViewById<ImageView>(R.id.deleteSetButton)
            deleteButton.setOnClickListener {
                // Remove the current set row
                setContainer.removeView(setRow)

                // If no sets remain, remove the entire exercise block
                if (setContainer.childCount == 0) {
                    val parent = binding.exerciseParentContainer
                    parent.removeView(exerciseBlock)
                } else {
                    // Otherwise just update the set labels
                    updateSetLabels(setContainer)
                }
            }

            setContainer.addView(setRow)
            updateSetLabels(setContainer)
        }

        binding.finishWorkoutButton.setOnClickListener {

            // Stop the timer
            timerHandler.removeCallbacks(timerRunnable)

            val updatedExercises = mutableListOf<Exercise>()
            val parentContainer = binding.exerciseParentContainer

            for (i in 0 until parentContainer.childCount) {
                val exerciseBlock = parentContainer.getChildAt(i)
                val nameView = exerciseBlock.findViewById<TextView>(R.id.exerciseNameText)
                val setContainer = exerciseBlock.findViewById<LinearLayout>(R.id.setContainer)

                val sets = mutableListOf<Set>()

                for (j in 0 until setContainer.childCount) {
                    val setView = setContainer.getChildAt(j)

                    val reps = setView.findViewById<EditText>(R.id.repsInput).text.toString().toIntOrNull() ?: -1
                    val weight = setView.findViewById<EditText>(R.id.weightInput).text.toString().toIntOrNull() ?: -1
                    val time = setView.findViewById<EditText>(R.id.timeInput).text.toString().toIntOrNull() ?: -1

                    sets.add(Set(
                        time = time,
                        reps = reps,
                        addedWeight = weight
                    ))
                }

                updatedExercises.add(
                    Exercise(
                        name = nameView.text.toString(),
                        sets = sets,
                        type = when {
                            sets.any { it.addedWeight > 0 } -> "weight"
                            sets.any { it.reps > 0 } -> "reps"
                            else -> "time"
                        }
                    )
                )
            }

            currentWorkout = currentWorkout?.copy(exercises = updatedExercises)

            // ✅ Save workout
            currentWorkout = currentWorkout?.copy(exercises = updatedExercises)

            // ✅ Print everything
            currentWorkout?.let { workout ->
                thread {
                    val db = AppDatabase.getDatabase(requireContext())
                        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val userId = prefs.getInt("logged_in_user_id", -1)

                    val user = db.userDao().getUser(userId)
                    val profile = user.profile ?: Profile("Unknown", "None", 0, 0, emptyList())

                    // Save updated workout with proper user ID
                    val savedWorkout = workout.copy(userId = userId)
                    db.workoutDao().insert(savedWorkout)

                    // ✅ Log workout info + profile name
                    Log.d("WorkoutDebug", "Workout Name: ${savedWorkout.workoutName}")
                    Log.d("WorkoutDebug", "Username: ${profile.name}")
                    Log.d("WorkoutDebug", "Time: ${savedWorkout.hourOf}:${"%02d".format(savedWorkout.minuteOf)}")
                    Log.d("WorkoutDebug", "Start DateTime: ${savedWorkout.startDateTime}")

                    savedWorkout.exercises.forEachIndexed { exIndex, exercise ->
                        Log.d("WorkoutDebug", "  Exercise ${exIndex + 1}: ${exercise.name} [Type: ${exercise.type}]")
                        exercise.sets.forEachIndexed { setIndex, set ->
                            Log.d(
                                "WorkoutDebug",
                                "    Set ${setIndex + 1}: ${set.reps} reps, ${set.addedWeight}kg, ${set.time} sec"
                            )
                        }
                    }

                    Log.d("WorkoutSave", "Workout saved to DB for ${profile.name}")
                }
            }

            // RETURN / SAVE WORKOUT DATA HERE
            // RETURN / SAVE WORKOUT DATA HERE
            thread {
                val db = AppDatabase.getDatabase(requireContext())
                val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val userId = prefs.getInt("logged_in_user_id", -1)

                // ✅ Get user and their profile
                val user = db.userDao().getUser(userId)
                val profile = user.profile ?: Profile("Unknown", "None", 0, 0, emptyList())

                currentWorkout?.let { workout ->
                    val savedWorkout = workout.copy(userId = userId)
                    db.workoutDao().insert(savedWorkout)
                    Log.d("WorkoutSave", "Workout saved to DB for ${profile.name}")
                }
            }

            // Optionally show a success message or summary
            binding.workoutTimer.text = "Workout finished!"
            binding.finishWorkoutButton.visibility = View.GONE

            // Clear all dynamic views
            binding.exerciseParentContainer.removeAllViews()

            // Reset all views to match the initial state
            binding.workoutTimer.visibility = View.GONE
            binding.addExerciseSection.visibility = View.GONE
            binding.finishWorkoutButton.visibility = View.GONE
            binding.addWorkoutButton.visibility = View.VISIBLE
            binding.workoutTimer.text = "Workout Timer: 00:00:00"

            // Reset the data
            currentWorkout = null
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


            val dialogView = layoutInflater.inflate(R.layout.dialog_exercise_selector, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val exerciseList = listOf(
                "Push Ups", "Squats", "Deadlift", "Plank", "Jump Rope", "Burpees", "Bench Press"
            )

            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.exerciseRecyclerView)
            val searchView = dialogView.findViewById<SearchView>(R.id.searchView)

            val adapter = ExerciseAdapter(exerciseList) { selected ->
                // Add to currentWorkout model (optional, already doing this)
                val workout = currentWorkout
                if (workout != null) {
                    val updatedList = workout.exercises.toMutableList()
                    updatedList.add(Exercise(selected, emptyList(), "reps"))
                    currentWorkout = workout.copy(exercises = updatedList)
                }

                // 🧱 Inflate the exercise block UI
                val exerciseBlock = layoutInflater.inflate(R.layout.exercise_list_box, binding.exerciseParentContainer, false)

                val nameText = exerciseBlock.findViewById<TextView>(R.id.exerciseNameText)
                val setContainer = exerciseBlock.findViewById<LinearLayout>(R.id.setContainer)
                val addSetButton = exerciseBlock.findViewById<View>(R.id.addSetButton)


                nameText.text = selected

                // Add initial set row
                addSetRow(setContainer, exerciseBlock)

                // Wire the Add Set button
                addSetButton.setOnClickListener { addSetRow(setContainer, exerciseBlock) }

                // Add the full exercise block to the parent container
                binding.exerciseParentContainer.addView(exerciseBlock)

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
