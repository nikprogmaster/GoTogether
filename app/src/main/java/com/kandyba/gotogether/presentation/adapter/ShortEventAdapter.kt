package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.getCalendarDate
import com.kandyba.gotogether.models.presentation.getFormattedTime
import com.kandyba.gotogether.models.presentation.getMonth
import com.squareup.picasso.Picasso
import java.util.*

class ShortEventAdapter(
    private var events: List<EventModel>
) : RecyclerView.Adapter<ShortEventAdapter.ShortEventViewHolder>() {

    fun setEvents(newEvents: List<EventModel>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortEventViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.short_event_card, parent, false)
        return ShortEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShortEventViewHolder, position: Int) {
        holder.bindViews(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class ShortEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val picture: ImageView = itemView.findViewById(R.id.picture)
        private val title: TextView = itemView.findViewById(R.id.short_card_title)
        private val dateAndTime: TextView = itemView.findViewById(R.id.event_date_time)
        private val price: TextView = itemView.findViewById(R.id.price)

        fun bindViews(event: EventModel) {
            title.text = event.title
            if (event.isFree == true) {
                price.text = itemView.resources.getText(R.string.free)
            } else {
                price.text = event.price
            }
            if (event.dates != null && event.dates.isNotEmpty()) {
                val timeUnix = event.dates[0].startUnix.toLong()
                val day = getCalendarDate(timeUnix).get(Calendar.DAY_OF_MONTH)
                val month = getMonth(timeUnix)
                val time = getFormattedTime(timeUnix)
                val dateText = "$day $month $time"
                dateAndTime.text = dateText
            }
            if (event.photoLinks.isNotEmpty() && event.photoLinks[0].isNotEmpty()) {
                Picasso.get()
                    .load(event.photoLinks[0])
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(picture)
            }
        }

    }
}