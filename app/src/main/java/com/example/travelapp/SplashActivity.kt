package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay 2.5 seconds then decide next screen (Onboarding first-run, else Login)
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("onboarding", MODE_PRIVATE)
            val completed = prefs.getBoolean("completed", false)
            val next = if (!completed) OnboardingActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, next))
            finish() // Close splash so back button won’t return here
        }, 2500)
    }
}
