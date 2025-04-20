package com.example.trainingarc.ui.team

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trainingarc.data.UserDao
import com.example.trainingarc.model.User
import com.example.trainingarc.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()

    // LiveData to hold the user data
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Team"
    }
    val text: LiveData<String> = _text

    // Function to get user data asynchronously (using coroutine)
    fun getUser(userId: Int) {
        // Launch a coroutine to perform the query on a background thread
        viewModelScope.launch {
            try {
                // Run the non-suspended getUser function on a background thread
                val user = withContext(Dispatchers.IO) {
                    userDao.getUser(userId)  // This is the non-suspended function
                }
                _user.postValue(user)  // Post the result to LiveData
                // Log the user data after it's retrieved
                Log.d("TeamViewModel", "User retrieved: ${user.username}, Points: ${user.pointsThisWeek}")
            } catch (e: Exception) {
                // Handle errors like user not found
            }
        }
    }
}
