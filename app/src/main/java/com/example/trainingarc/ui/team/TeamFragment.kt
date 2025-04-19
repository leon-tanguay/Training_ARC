package com.example.trainingarc.ui.team

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

// Data class representing a teammate
data class Teammate(val name: String, val points: Int)

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val teamViewModel = ViewModelProvider(this).get(TeamViewModel::class.java)
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        binding.teamName.text = "Spencer's Soldiers ⭐"
        binding.timerText.text = "5:23:11"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
