package com.example.trainingarc.ui.history

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trainingarc.databinding.FragmentHistoryBinding
import com.example.trainingarc.model.*
import com.example.trainingarc.R
import android.content.Context
import java.time.format.DateTimeFormatter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val context = requireContext()

            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getInt("logged_in_user_id", -1)
            if (userId == -1) return@launch

            val workouts: List<Workout> = withContext(Dispatchers.IO) {
                val db = HistoryDatabase.getDatabase(context)
                db.workoutDao().getWorkoutsForUser(userId)
            }

            val container = binding.root.findViewById<LinearLayout>(R.id.workout_container)
            val inflater = LayoutInflater.from(context)

            if (workouts.isEmpty()) {
                val emptyText = TextView(context).apply {
                    text = "Your future workouts will be shown here!"
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    setPadding(24, 48, 24, 0)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 100, 0, 0) // Optional: adds top spacing
                    }
                }
                container.addView(emptyText)
                return@launch
            }

            for (workout in workouts) {
                val box = inflater.inflate(R.layout.item_workout_box, container, false)

                val titleText = box.findViewById<TextView>(R.id.workout_title)
                val usernameText = box.findViewById<TextView>(R.id.workout_username)
                val highlightContainer = box.findViewById<LinearLayout>(R.id.highlight_container)

                titleText.text = workout.workoutName

                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                val workoutDate = workout.startDateTime.toLocalDate().format(formatter)
                usernameText.text = workoutDate

                val maxToShow = 4
                for ((index, exercise) in workout.exercises.withIndex()) {
                    if (index >= maxToShow) break

                    val line = TextView(context).apply {
                        textSize = 14f
                        setTextColor(resources.getColor(android.R.color.black, null))

                        text = android.text.SpannableStringBuilder().apply {
                            append(android.text.SpannableString("${exercise.name}: ").apply {
                                setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, length, 0)
                            })
                            append("Best set - ")
                            append(android.text.SpannableString(exercise.highlightString).apply {
                                setSpan(android.text.style.RelativeSizeSpan(0.9f), 0, length, 0)
                            })
                        }
                    }

                    highlightContainer.addView(line)
                }

                if (workout.exercises.size > maxToShow) {
                    val moreText = TextView(context).apply {
                        text = "..."
                        textSize = 14f
                        setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    }
                    highlightContainer.addView(moreText)
                }

                container.addView(box)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
