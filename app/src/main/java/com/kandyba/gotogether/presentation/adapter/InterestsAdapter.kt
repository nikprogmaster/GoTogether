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

        private val title: TextView = itemView.findViewById(R.id.title)
        private val seekbar: SeekBar = itemView.findViewById(R.id.interest_level)
        private val level: TextView = itemView.findViewById(R.id.seekbar_value)

        fun bindViews(interest: Interest) {
            title.text = interest.name
            level.text = interest.level.toString()
            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    interest.level = progress
                    level.text = seekBar?.progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }
}