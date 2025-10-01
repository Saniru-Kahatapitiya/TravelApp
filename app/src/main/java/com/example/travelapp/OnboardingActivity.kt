package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var btnNext: MaterialButton
    private lateinit var btnSkip: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPagerOnboarding)
        tabLayout = findViewById(R.id.tabLayoutIndicators)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)

        onboardingAdapter = OnboardingAdapter(
            listOf(
                OnboardingItem(R.drawable.onboarding1, "Welcome to EasyTravel",
                    "EasyTravel is your ultimate travel companion — helping you discover destinations, plan trips, and pack smart."),
                OnboardingItem(R.drawable.onboarding2, "Plan Every Detail, Your Way",
                    "Create day-by-day itineraries, add activities, and keep all your bookings in one place."),
                OnboardingItem(R.drawable.onboarding3, "Pack Smart, Travel Light",
                    "Auto-generate packing lists tailored to your destination and season.")
            )
        )

        viewPager.adapter = onboardingAdapter

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        btnSkip.setOnClickListener { finishOnboarding() }

        btnNext.setOnClickListener {
            if (viewPager.currentItem + 1 < onboardingAdapter.itemCount) {
                viewPager.currentItem++
            } else {
                finishOnboarding()
            }
        }
    }

    private fun finishOnboarding() {
        // Mark onboarding as completed
        getSharedPreferences("onboarding", MODE_PRIVATE)
            .edit()
            .putBoolean("completed", true)
            .apply()
        // Continue to Login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
