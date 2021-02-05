package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.Interest

class InterestsAdapter(
    private val interestsList: MutableList<Interest>
) : RecyclerView.Adapter<InterestsAdapter.InterestHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.interest_item, parent, false)
        return InterestHolder(view)
    }

    override fun getItemCount() = interestsList.size

    override fun onBindViewHolder(holder: InterestHolder, position: Int) {
        val interest = interestsList[position]
        holder.bindViews(interest)
    }

    class InterestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.title)
        private var level: SeekBar = itemView.findViewById(R.id.interest_level)

        fun bindViews(interest: Interest) {
            title.text = interest.name
        }
    }
}