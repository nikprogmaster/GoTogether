package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.Participant
import com.squareup.picasso.Picasso


class ParticipantsAdapter(
    private val participants: List<Participant>,
    private val listener: OnProfileButtonClickListener
) : RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.participant_item, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.binViews(participants[position])
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    inner class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.person_name)
        private val textAvatar: TextView = itemView.findViewById(R.id.text_avatar)
        private val imageAvatar: ImageView = itemView.findViewById(R.id.image_avatar)
        private val goToProfile: Button = itemView.findViewById(R.id.go_to_profile)

        fun binViews(participant: Participant) {
            name.text = participant.firstName
            if (participant.avatar == "") {
                textAvatar.text = participant.firstName?.substring(0, 1)
                imageAvatar.visibility = View.GONE
            } else {
                Picasso.get()
                    .load(participant.avatar)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(imageAvatar)
            }
            goToProfile.setOnClickListener {
                listener.onProfileClick(participant.id)
            }
        }
    }

}

interface OnProfileButtonClickListener {
    fun onProfileClick(userId: String)
}