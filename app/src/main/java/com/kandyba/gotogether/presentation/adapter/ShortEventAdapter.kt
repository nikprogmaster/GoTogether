package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.EventModel

/**
 * @author Кандыба Никита
 * @since 05.04.2021
 */
class ShortEventAdapter(
    val events: List<EventModel>
) : RecyclerView.Adapter<ShortEventAdapter.ShortEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortEventViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.short_event_card, parent, false)
        return ShortEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShortEventViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class ShortEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}