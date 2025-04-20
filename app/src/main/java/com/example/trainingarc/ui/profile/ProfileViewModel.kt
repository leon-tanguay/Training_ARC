package com.example.trainingarc.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trainingarc.model.AppDatabase
import com.example.trainingarc.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    init {
        loadLoggedInUserProfile()
    }

    private fun loadLoggedInUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getInt("logged_in_user_id", -1)

            if (userId != -1) {
                val db = AppDatabase.getDatabase(context)
                val user = db.userDao().getUser(userId)
                user.profile?.let {
                    _profile.postValue(it)
                }
            }
        }
    }
}
