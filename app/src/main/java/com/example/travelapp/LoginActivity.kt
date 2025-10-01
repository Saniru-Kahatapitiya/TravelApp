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
import com.example.travelapp.repository.TravelRepository
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var repository: TravelRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvSignup = findViewById<TextView>(R.id.tv_signup)
        val forgotPasswordBtn = findViewById<TextView>(R.id.btnForgotPassword)

        forgotPasswordBtn.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = hashPassword(password)

            lifecycleScope.launch {
                val userByEmail = repository.getUserByEmail(email)

                runOnUiThread {
                    if (userByEmail == null) {
                        Toast.makeText(this@LoginActivity, "Email not registered", Toast.LENGTH_SHORT).show()
                    } else {
                        lifecycleScope.launch {
                            val user = repository.getUserByCredentials(email, hashedPassword)
                            runOnUiThread {
                                if (user != null) {
                                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
