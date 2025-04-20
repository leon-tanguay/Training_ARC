package com.example.trainingarc.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trainingarc.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProfileViewModel::class.java]

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            val profile = user.profile

            binding.nameText.text = profile?.name ?: "Unknown"
            binding.teamText.text = profile?.team ?: "No Team"
            binding.levelText.text = "Level ${user.level}"
            binding.xpProgressBar.progress = user.progressToNextLevel

            profile?.badges?.let { badges ->
                if (badges.size >= 3) {
                    binding.badge1.setImageResource(badges[0])
                    binding.badge2.setImageResource(badges[1])
                    binding.badge3.setImageResource(badges[2])
                }
            }
        }

        // ✅ Reload profile as soon as view is created and viewModel is ready
        profileViewModel.reloadUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
