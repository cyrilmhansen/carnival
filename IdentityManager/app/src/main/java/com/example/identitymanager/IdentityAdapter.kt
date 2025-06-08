package com.example.identitymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IdentityAdapter(private val identities: List<Identity>) :
    RecyclerView.Adapter<IdentityAdapter.IdentityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdentityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_identity, parent, false)
        return IdentityViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdentityViewHolder, position: Int) {
        val identity = identities[position]
        holder.nicknameTextView.text = identity.nickname
        holder.emailTextView.text = identity.email
    }

    override fun getItemCount(): Int = identities.size

    class IdentityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nicknameTextView: TextView = itemView.findViewById(R.id.nicknameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
    }
}
