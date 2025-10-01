package com.example.travelapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class GoogleAccountAdapter(
    private val accounts: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<GoogleAccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmail: TextView = view.findViewById(R.id.tv_account_email)
        //val ivAvatar: ImageView = view.findViewById(R.id.iv_account_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_google_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val email = accounts[position]
        holder.tvEmail.text = email
        holder.itemView.setOnClickListener { onClick(email) }
    }

    override fun getItemCount(): Int = accounts.size
}
