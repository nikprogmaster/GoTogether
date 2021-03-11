package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.getMonth
import com.squareup.picasso.Picasso


class EventsAdapter: RecyclerView.Adapter<EventsAdapter.EventHolder>() {

    private lateinit var events: List<EventModel>

    fun setEvents(events: List<EventModel>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = events[position]
        holder.bindViews(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class EventHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val categories: RecyclerView = itemView.findViewById(R.id.categories_list)
        private val dayOfMonth: TextView = itemView.findViewById(R.id.day_of_month)
        private val month: TextView = itemView.findViewById(R.id.month)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val picture: ImageView = itemView.findViewById(R.id.picture)
        private val categoriesAdapter: CategoriesAdapter = CategoriesAdapter()

        fun bindViews(event: EventModel) {
            title.text = event.title
            event.dates.let { if (it.isNotEmpty()) {
                dayOfMonth.text = it[0].getStartCalendarDay()
                month.text = getMonth(it[0].getStartCalendarMonth())
            }
            }
            if (event.isFree) {
                price.text = itemView.resources.getText(R.string.free)
            } else {
                price.text = event.price
            }
            categoriesAdapter.setCategories(event.categories)
            val layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            categories.layoutManager = layoutManager
            categories.adapter = categoriesAdapter
            Picasso.get()
                .load(event.photoLinks.let { if (it.isNotEmpty()) it[0] else EMPTY_STRING })
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_placeholder)
                .into(picture)
        }
    }
}