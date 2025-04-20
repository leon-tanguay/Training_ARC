package com.example.trainingarc.ui.home
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
import androidx.lifecycle.ViewModelProvider
import com.example.trainingarc.databinding.FragmentHomeBinding
import com.example.trainingarc.model.*
import com.example.trainingarc.testing.*
import com.example.trainingarc.R
import kotlin.concurrent.thread

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

        lifecycleScope.launch {
            val workoutFeed = withContext(Dispatchers.IO) {
                generateTestWorkoutFeed(requireContext())
            }

            val container = binding.root.findViewById<LinearLayout>(R.id.workout_container)
            val inflater = LayoutInflater.from(requireContext())

            for (workout in workoutFeed.workouts) {
                val box = inflater.inflate(R.layout.item_workout_box, binding.workoutContainer, false)

                val titleText = box.findViewById<TextView>(R.id.workout_title)
                val usernameText = box.findViewById<TextView>(R.id.workout_username)
                val highlightContainer = box.findViewById<LinearLayout>(R.id.highlight_container)

                titleText.text = workout.workoutName

                // ✅ Coroutine-safe Room call
                val profileName = withContext(Dispatchers.IO) {
                    val db = AppDatabase.getDatabase(requireContext())
                    val user = db.userDao().getUser(workout.userId)
                    user?.profile?.name ?: "Unknown"
                }
                usernameText.text = "by $profileName"

                val maxToShow = 4
                for ((index, exercise) in workout.exercises.withIndex()) {
                    if (index >= maxToShow) break

                    val line = TextView(requireContext()).apply {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
