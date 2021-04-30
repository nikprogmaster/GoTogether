package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.getCalendarDate
import com.kandyba.gotogether.models.presentation.getFormattedTime
import com.kandyba.gotogether.models.presentation.getMonth
import com.squareup.picasso.Picasso
import java.util.*

class ShortEventAdapter(
    private var events: MutableList<EventModel>,
    private val listener: OnEventClickListener,
    private val showLikes: Boolean
) : RecyclerView.Adapter<ShortEventAdapter.ShortEventViewHolder>() {

    fun setEvents(newEvents: List<EventModel>) {
        events = newEvents.toMutableList()
        notifyDataSetChanged()
    }

    fun changeButtonState(eventId: String) {
        events.map { if (it.id == eventId) it.activated = !it.activated }
    }

    fun removeEvent(eventId: String) {
        events.map { if (it.id == eventId) events.remove(it) }
        notifyDataSetChanged()
    }

    fun changeUserLikedProperty(eventId: String) {
        var idInList = -1
        for (i in events.indices) {
            if (events[i].id == eventId) {
                events[i].likedByUser = !events[i].likedByUser
                idInList = i
            }
        }
        if (idInList != -1) {
            notifyItemChanged(idInList)
        }
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

    inner class ShortEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val picture: ImageView = itemView.findViewById(R.id.picture)
        private val title: TextView = itemView.findViewById(R.id.short_card_title)
        private val dateAndTime: TextView = itemView.findViewById(R.id.event_date_time)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val likeButton: ToggleButton = itemView.findViewById(R.id.like_event)
        private val card: MaterialCardView = itemView.findViewById(R.id.short_event_card)

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
            likeButton.isChecked = event.likedByUser
            card.setOnClickListener {
                listener.onClick(event.id)
            }
            likeButton.setOnClickListener {
                event.likedByUser = !event.likedByUser
                listener.onLikeButtonClick(event.id)
            }
            if (!showLikes) {
                likeButton.visibility = View.GONE
            }
        }

    }

    interface OnEventClickListener {
        fun onClick(eventId: String)
        fun onLikeButtonClick(eventId: String)
    }
}