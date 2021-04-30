package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.SocketMessage
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.models.presentation.Message
import com.kandyba.gotogether.presentation.adapter.MessagesAdapter
import com.kandyba.gotogether.presentation.viewmodel.DialogsViewModel
import java.util.*

class MessagesFragment : Fragment() {

    private lateinit var typeMessage: EditText
    private lateinit var sendMessage: ImageView
    private lateinit var messagesRecycler: RecyclerView

    private lateinit var viewModel: DialogsViewModel
    private lateinit var settings: SharedPreferences
    private var adapter: MessagesAdapter? = null
    private var manager: LinearLayoutManager? = null

    private val listener = object : Socket.UniversalListener() {
        override fun onOpen() {
            //do nothing
        }

        override fun onMessage(socketMessage: SocketMessage?) {
            Log.i("MessagesFragment", "сюда вот пришел")
            val userId = settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING
            if (socketMessage != null) {
                if (socketMessage.userId == userId) {
                    socketMessage.time?.let { adapter?.changeMessageStatus(it) }
                    adapter = messagesRecycler.adapter as? MessagesAdapter
                    messagesRecycler.adapter = null
                    messagesRecycler.layoutManager = null
                    messagesRecycler.adapter = adapter
                    messagesRecycler.layoutManager = manager
                    adapter?.notifyDataSetChanged()
                    messagesRecycler.invalidate()
                    messagesRecycler.postInvalidate()
                } else {
                    adapter?.addMessage(Message.convertFromSocketMessage(socketMessage, userId))
                }
            }
        }

        override fun onClosed() {
            //do nothing
        }

        override fun onFailure() {
            //do nothing
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.messages_fragment, container, false)
        typeMessage = root.findViewById(R.id.type_message_field)
        sendMessage = root.findViewById(R.id.send_button)
        messagesRecycler = root.findViewById(R.id.messages_recycler)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        viewModel.getDialogMessages(
            settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
            requireArguments().getString(DIALOG_ID, EMPTY_STRING),
            settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING
        )
        initListeners()
    }

    private fun resolveDependencies() {
        val appComponent = (requireActivity().application as App).appComponent
        val factory = appComponent.getDialogsViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[DialogsViewModel::class.java]
        settings = appComponent.getSharedPreferences()
        adapter = MessagesAdapter(mutableListOf())
        manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        manager?.stackFromEnd = true;
        messagesRecycler.layoutManager = manager
        messagesRecycler.adapter = adapter
    }

    private fun initObservers() {
        viewModel.dialogMessages.observe(requireActivity(), Observer { messages ->
            adapter?.setMessages(messages)
            viewModel.startMessaging(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING, listener
            )
        })
    }

    private fun initListeners() {
        sendMessage.setOnClickListener {
            if (typeMessage.text.toString() != EMPTY_STRING) {
                if (viewModel.getSocketState() == Socket.State.OPEN) {
                    val message = createMessage(typeMessage.text.toString())
                    adapter?.addMessage(message)
                    adapter?.itemCount?.minus(1)
                        ?.let { it1 -> messagesRecycler.smoothScrollToPosition(it1) }
                    viewModel.sendMessage(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        SocketMessage(
                            userId = settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
                            dialogId = requireArguments().getString(DIALOG_ID) ?: EMPTY_STRING,
                            message = typeMessage.text.toString(),
                            time = message.createdAt
                        )
                    )
                } else {
                    viewModel.startMessaging(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING, listener
                    )
                }
                typeMessage.setText(EMPTY_STRING)
            }

        }
        typeMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendMessage.alpha = if (s.toString() != EMPTY_STRING) 1f else 0.5f
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun createMessage(text: String): Message {
        return Message(
            settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
            requireArguments().getString(DIALOG_ID) ?: EMPTY_STRING,
            text,
            Calendar.getInstance().timeInMillis,
            true,
            delivered = false
        )
    }

    override fun onDestroy() {
        Log.i("Kandyba", "Устрой Destroy")
        viewModel.dialogMessages.removeObservers(requireActivity())
        super.onDestroy()
    }

    companion object {
        fun newInstance(dialogId: String): MessagesFragment {
            val args = Bundle()
            args.putString(DIALOG_ID, dialogId)
            val fragment = MessagesFragment()
            fragment.arguments = args
            return fragment
        }

        private const val DIALOG_ID = "dialogId"
    }
}