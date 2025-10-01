package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.travelapp.models.User
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.launch
import java.security.MessageDigest

class SignupActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var repository: TravelRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "travel-database"
        ).build()

        repository = TravelRepository(
            db.tripDao(),
            db.dayDao(),
            db.attractionDao(),
            db.collectionDao(),
            db.savedImageDao(),
            db.userDao()
        )

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

        val btnSignup = findViewById<Button>(R.id.btn_signup)
        val tvLogin = findViewById<TextView>(R.id.tv_login)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString().trim().lowercase()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isStrongPassword(password)) {
                Toast.makeText(
                    this,
                    "Password must be at least 8 characters, include uppercase, lowercase, number & special character",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val hashedPassword = hashPassword(password)

            lifecycleScope.launch {
                try {
                    val existingUser = repository.getUserByEmail(email)

                    runOnUiThread {
                        if (existingUser != null) {
                            Toast.makeText(this@SignupActivity, "User already exists", Toast.LENGTH_SHORT).show()
                        } else {
                            lifecycleScope.launch {
                                repository.insertUser(User(email = email, passwordHash = hashedPassword))

                                runOnUiThread {
                                    Toast.makeText(this@SignupActivity, "Signup successful! Please log in.", Toast.LENGTH_SHORT).show()

                                    // Navigate to login
                                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@SignupActivity, "Signup failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun isStrongPassword(password: String): Boolean {
        val passwordPattern =
            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")
        return passwordPattern.matches(password)
    }
}
