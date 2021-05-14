package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.getCalendarDay
import com.kandyba.gotogether.models.presentation.getMonth
import com.squareup.picasso.Picasso

/**
 * Адаптер событий
 *
 * @constructor
 * @property events список событий
 * @property listener слушатель нажатия на событие
 */
class EventsAdapter(
    private var events: MutableList<EventModel>,
    private val listener: OnEventClickListener
) : RecyclerView.Adapter<EventsAdapter.EventHolder>() {

    /**
     * Изменить активацию кнопки "лайк"
     *
     * @param eventId id мероприятия
     */
    fun changeButtonState(eventId: String) {
        for (i in events.indices) {
            if (events[i].id == eventId) {
                events[i].activated = !events[i].activated
            }
        }
    }

    /**
     * Изменить состояние кнопки "лайк"
     *
     * @param eventId id мероприятия
     */
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

    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val categories: RecyclerView = itemView.findViewById(R.id.categories_list)
        private val dayOfMonth: TextView = itemView.findViewById(R.id.day_of_month)
        private val month: TextView = itemView.findViewById(R.id.month)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val picture: ImageView = itemView.findViewById(R.id.picture)
        private val card: MaterialCardView = itemView.findViewById(R.id.card)
        private val likeButton: ToggleButton = itemView.findViewById(R.id.like)
        private val date: LinearLayout = itemView.findViewById(R.id.date)
        private val categoriesAdapter: CategoriesAdapter = CategoriesAdapter()

        fun bindViews(event: EventModel) {
            title.text = event.title.capitalize()
            event.dates?.let {
                if (it.isNotEmpty()) {
                    dayOfMonth.text = getCalendarDay(it[0].startUnix)
                    month.text = getMonth(it[0].startUnix)
                } else {
                    date.visibility = View.INVISIBLE
                }
            }
            if (event.isFree == true) {
                price.text = itemView.resources.getText(R.string.free)
            } else {
                price.text = event.price
                if (event.price == EMPTY_STRING) {
                    price.text = itemView.resources.getText(R.string.not_mentioned)
                }
            }
            likeButton.isEnabled = event.activated
            likeButton.isChecked = event.likedByUser
            event.categories?.let { categoriesAdapter.setCategories(it) }
            val layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            categories.layoutManager = layoutManager
            categories.adapter = categoriesAdapter
            if (event.photoLinks.isNotEmpty() && event.photoLinks[0].isNotEmpty()) {
                Picasso.get()
                    .load(event.photoLinks[0])
                    .placeholder(R.drawable.ill_placeholder_300dp)
                    .error(R.drawable.ill_placeholder_300dp)
                    .into(picture)
            }
            card.setOnClickListener {
                listener.onClick(event.id, events.indexOf(event))
            }
            likeButton.setOnClickListener {
                event.likedByUser = !event.likedByUser
                listener.onLikeButtonClick(
                    event.id
                )
            }
        }
    }

    interface OnEventClickListener {
        fun onClick(eventId: String, cardPosition: Int)
        fun onLikeButtonClick(eventId: String)
    }
}