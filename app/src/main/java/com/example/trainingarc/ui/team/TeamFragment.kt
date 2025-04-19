package com.example.trainingarc.ui.team

import com.example.trainingarc.model.Teammate
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trainingarc.databinding.FragmentTeamBinding
import android.widget.TextView
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingarc.R
import android.os.CountDownTimer
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.core.content.ContextCompat
import androidx.annotation.ColorRes


// Adapter for the RecyclerView
class TeammateAdapter(private val teammates: List<Teammate>) :
    RecyclerView.Adapter<TeammateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val pointsText: TextView = view.findViewById(R.id.pointsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teammate, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = teammates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teammate = teammates[position]
        holder.nameText.text = teammate.name
        holder.pointsText.text = teammate.points.toString()
    }
}

class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!
    private var countdownTimer: CountDownTimer? = null
    private fun getMillisUntilEndOfWeek(): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        // Set to Sunday at 23:59:59.999
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        // If the target time is earlier than now, move to next Sunday
        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        return calendar.timeInMillis - now
    }

    private fun getColorCompat(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun startCountdownTimer(millisUntilFinished: Long) {
        countdownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                _binding?.let { binding ->
                    binding.timerText.text = String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds)

                    val color = when {
                        days >= 4 -> getColorCompat(R.color.timer_sky_blue)
                        days == 2L -> getColorCompat(R.color.timer_warning_yellow)
                        days == 1L -> getColorCompat(R.color.timer_warning_orange)
                        days == 0L && hours > 12L -> getColorCompat(R.color.timer_warning_red)
                        else -> getColorCompat(R.color.timer_dark_red)
                    }

                    binding.timerText.setTextColor(color)
                }
            }

            override fun onFinish() {
                _binding?.let { binding ->
                    binding.timerText.text = "0:00:00:00"
                    binding.timerText.setTextColor(getColorCompat(R.color.timer_dark_red))
                }
            }
        }.start()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val teamViewModel = ViewModelProvider(this).get(TeamViewModel::class.java)
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val timeLeftMillis = getMillisUntilEndOfWeek()
        startCountdownTimer(timeLeftMillis)

        // Dummy data (up to 8 teammates)
        val teammates = listOf(
            Teammate("DownyWipe27", 5000),
            Teammate("Mattbooties", 3000),
            Teammate("Soren", 2000),
            Teammate("Troy", 0),
            Teammate("Dylan", 0)
        )

        // Setup RecyclerView
        val adapter = TeammateAdapter(teammates)
        binding.teammateRecyclerView.adapter = adapter
        binding.teammateRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Update Progress Bar
        val totalPoints = teammates.sumOf { it.points }
        val maxPoints = 50000
        binding.progressBar.max = maxPoints
        binding.progressBar.progress = totalPoints
        binding.progressLabel.text = "$totalPoints / $maxPoints"

        // Set other static content
        binding.teamName.text = "Team"
        binding.teamName.text = "Spencer's Soldiers"
        val teamLevel = 8 // 🔁 You can change this later
        binding.teamLevel.text = teamLevel.toString()

        return root
    }

    override fun onDestroyView() {
        countdownTimer?.cancel()
        countdownTimer = null
        _binding = null
        super.onDestroyView()
    }
}