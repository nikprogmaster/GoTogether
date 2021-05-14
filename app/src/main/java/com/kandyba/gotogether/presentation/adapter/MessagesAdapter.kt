package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.Message

/**
 * Адаптер сообщений
 *
 * @constructor
 * @property messages список сообщений
 */
class MessagesAdapter(
    private var messages: MutableList<Message>
) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    fun setMessages(newMessages: List<Message>) {
        messages = newMessages.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Добавить сообщение
     *
     * @param newMessage добавляемое сообщение
     */
    fun addMessage(newMessage: Message) {
        messages.add(newMessage)
        notifyDataSetChanged()
    }

    /**
     * Изменить состояние сообщения
     *
     * @param mesTime время сообщения
     */
    fun changeMessageStatus(mesTime: Long) {
        val changedMessage = messages.find { it.createdAt == mesTime }
        changedMessage?.delivered = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]
        if (message.isMyMessage) {
            holder.bindMyMessage(message)
        } else {
            holder.bindCompanionMessage(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val myMessageText: TextView = itemView.findViewById(R.id.my_message_text)
        private val mySendStatus: ImageView = itemView.findViewById(R.id.my_send_status)
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val sendStatus: ImageView = itemView.findViewById(R.id.send_status)
        private val myMessage: LinearLayout = itemView.findViewById(R.id.my_message)
        private val companionMessage: LinearLayout = itemView.findViewById(R.id.companion_message)

        fun bindMyMessage(message: Message) {
            myMessage.visibility = View.VISIBLE
            companionMessage.visibility = View.GONE
            myMessageText.text = message.text
            setSendStatus(mySendStatus, message.delivered)
        }

        fun bindCompanionMessage(message: Message) {
            myMessage.visibility = View.GONE
            companionMessage.visibility = View.VISIBLE
            messageText.text = message.text
            setSendStatus(sendStatus, message.delivered)
        }

        private fun setSendStatus(view: ImageView, delivered: Boolean) {
            if (delivered) {
                view.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.mc_baseline_check_16dp
                    )
                )
            } else {
                view.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.mc_baseline_watch_later_16dp
                    )
                )
            }
        }
    }
}