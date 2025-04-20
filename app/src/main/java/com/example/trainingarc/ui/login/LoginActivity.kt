package com.example.trainingarc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.trainingarc.model.User
import com.example.trainingarc.model.AppDatabase
import com.example.trainingarc.model.Profile
import kotlin.concurrent.thread
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameInput = findViewById(R.id.usernameInput)
        loginButton = findViewById(R.id.loginButton)

        // ✅ Trigger login when pressing enter
        usernameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                loginButton.performClick()
                true
            } else {
                false
            }
        }

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            if (username.isNotEmpty()) {
                thread {
                    val db = AppDatabase.getDatabase(this)
                    val userDao = db.userDao()

                    val existing = userDao.getUserByUsername(username)
                    val userId = existing?.userId ?: userDao.insert(
                        User(
                            username = username,
                            email = "",
                            profile = Profile(
                                name = username,
                                team = "New Recruit",
                                profilePicResId = R.drawable.example_pfp,
                                badges = listOf(
                                    R.drawable.gold_medal,
                                    R.drawable.silver_medal,
                                    R.drawable.bronze_medal
                                )
                            )
                        )
                    )

                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit {
                        putInt("logged_in_user_id", userId.toInt())
                    }

                    runOnUiThread {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                usernameInput.error = "Please enter a username"
            }
        }
    }
}
