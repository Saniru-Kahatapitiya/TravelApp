package com.example.travelapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.widget.EditText

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val tvPassword = view.findViewById<TextView>(R.id.tv_password)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit)

        val prefs = requireContext().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

        // Load stored profile data
        val name = prefs.getString("name", "Saniru Mandira")
        val email = prefs.getString("email", "Saniru@example.com")
        val password = prefs.getString("password", "password123")

        tvName.text = name
        tvEmail.text = email
        tvPassword.text = "*".repeat(password?.length ?: 8)

        // Edit profile dialog
        btnEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

            val etName = dialogView.findViewById<EditText>(R.id.et_name)
            val etEmail = dialogView.findViewById<EditText>(R.id.et_email)
            val etPassword = dialogView.findViewById<EditText>(R.id.et_password)

            etName.setText(name)
            etEmail.setText(email)
            etPassword.setText(password)

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newName = etName.text.toString()
                    val newEmail = etEmail.text.toString()
                    val newPassword = etPassword.text.toString()

                    // Save new values
                    prefs.edit()
                        .putString("name", newName)
                        .putString("email", newEmail)
                        .putString("password", newPassword)
                        .apply()

                    // Update UI
                    tvName.text = newName
                    tvEmail.text = newEmail
                    tvPassword.text = "*".repeat(newPassword.length)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
