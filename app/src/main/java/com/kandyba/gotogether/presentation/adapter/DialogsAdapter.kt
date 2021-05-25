package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.presentation.getTodayTimeOrDate
import com.squareup.picasso.Picasso

/**
 * Адаптер диалогов
 *
 * @constructor
 * @property dialogs список диалогов
 * @property onDialogClickListener слушатель нажатия на диалог
 */
class DialogsAdapter(
    private val dialogs: List<DialogDomainModel>,
    private val onDialogClickListener: OnDialogClickListener
) : RecyclerView.Adapter<DialogsAdapter.DialogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item, parent, false)
        return DialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bindViews(dialogs[position])
    }

    override fun getItemCount(): Int {
        return dialogs.size
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name_title)
        private val lastMessage: TextView = itemView.findViewById(R.id.last_message)
        private val lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time)
        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val dialogLayout: ConstraintLayout = itemView.findViewById(R.id.dialog_item)

        fun bindViews(dialog: DialogDomainModel) {
            name.text = dialog.companion.firstName
            lastMessage.text = dialog.lastMessage?.text
            lastMessageTime.text = dialog.lastMessage?.createdAt?.let { getTodayTimeOrDate(it) }
            val placeholder =
                if (dialog.companion.sex == MAN_SEX) R.drawable.man_stub else R.drawable.woman_stub
            Picasso.get()
                .load(dialog.companion.avatar)
                .placeholder(placeholder)
                .error(placeholder)
                .into(avatar)
            dialogLayout.setOnClickListener {
                onDialogClickListener.onDialogClick(dialog.id, dialog.companion.firstName)
            }
        }
    }

    companion object {
        private const val MAN_SEX = "0"
    }
}

interface OnDialogClickListener {
    fun onDialogClick(dialogId: String, interlocutor: String?)
}