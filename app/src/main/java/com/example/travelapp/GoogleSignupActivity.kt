package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class GoogleSignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_signup)

        val rvAccounts = findViewById<RecyclerView>(R.id.rv_google_accounts)

        // Sample accounts to display
        val accounts = listOf(
            "Hirushi@gmail.com",
            "Himaya123@gmail.com",
            "Harith789@gmail.com"
        )

        rvAccounts.layoutManager = LinearLayoutManager(this)
        rvAccounts.adapter = GoogleAccountAdapter(accounts) { selectedEmail ->
            // Navigate to WelcomeActivity after selecting an account
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("EMAIL", selectedEmail)
            startActivity(intent)
        }

    }
}
