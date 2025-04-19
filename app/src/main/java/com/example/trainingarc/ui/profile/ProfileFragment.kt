package com.example.trainingarc.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trainingarc.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.nameText.text = profile.name
            binding.teamText.text = profile.team
            binding.levelText.text = "Level ${profile.level}"
            binding.profileImage.setImageResource(profile.profilePicResId)

            if (profile.badges.size >= 3) {
                binding.badge1.setImageResource(profile.badges[0])
                binding.badge2.setImageResource(profile.badges[1])
                binding.badge3.setImageResource(profile.badges[2])
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
