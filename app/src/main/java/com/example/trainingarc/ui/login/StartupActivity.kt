package com.example.trainingarc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class StartupActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
//        val loggedInUserId = prefs.getInt("logged_in_user_id", -1)
//
//        if (loggedInUserId != -1) {
//            // ✅ User is already logged in → go to main
//            startActivity(Intent(this, MainActivity::class.java))
//        } else {
//            // ❌ No user logged in → go to login
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
//
//        finish() // Prevents returning to this activity on back press
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Clear any previously stored user ID
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit() { remove("logged_in_user_id") }

        // Always start with LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}