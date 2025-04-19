package com.example.trainingarc.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainingarc.R
import com.example.trainingarc.model.Profile

class ProfileViewModel : ViewModel() {

    private val _profile = MutableLiveData<Profile>().apply {
        value = Profile(
            name = "Soren",
            team = "Spencer's Soldiers ⭐",
            level = 27,
            profilePicResId = R.drawable.profile_pic_placeholder,
            badges = listOf(
                R.drawable.badge_fire,
                R.drawable.badge_lightning,
                R.drawable.badge_dragon
            )
        )
    }

    val profile: LiveData<Profile> = _profile
    val text: LiveData<String> = MutableLiveData("This is profile Fragment")
}
