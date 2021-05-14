package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.DateDomainModel
import com.kandyba.gotogether.models.presentation.getDayOfWeek
import com.kandyba.gotogether.models.presentation.getFormattedDate
import com.kandyba.gotogether.models.presentation.getFormattedTime

/**
 * Адаптер расписания
 *
 * @constructor
 * @property dates даты, в которые проходит мероприятие
 */
class ScheduleAdapter(
    private val dates: List<DateDomainModel>
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val scheduleItem = dates[position]
        holder.bindViews(scheduleItem)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dateField: TextView = itemView.findViewById(R.id.event_date)
        private val timeField: TextView = itemView.findViewById(R.id.event_time)

        fun bindViews(day: DateDomainModel) {
            val startUnixTime = day.startUnix.toLong()
            val endUnixTime = day.endUnix.toLong()
            val dayOfWeek = getDayOfWeek(startUnixTime)
            val date = getFormattedDate(startUnixTime)
            val dateResult = "${date}\n${dayOfWeek}"
            dateField.text = dateResult

            val timeStart = getFormattedTime(startUnixTime)
            val timeEnd = getFormattedTime(endUnixTime)
            val timeResult = "C ${timeStart}\nДо ${timeEnd}"
            timeField.text = timeResult
        }
    }
}