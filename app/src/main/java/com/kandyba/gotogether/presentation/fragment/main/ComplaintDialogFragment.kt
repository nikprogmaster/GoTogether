package com.kandyba.gotogether.presentation.fragment.main


import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.EventDetailsViewModelFactory


/**
 * @author Кандыба Никита
 * @since 26.03.2021
 */
class ComplaintDialogFragment : DialogFragment() {

    private lateinit var complainText: EditText
    private lateinit var sendComplaint: Button

    private lateinit var factory: EventDetailsViewModelFactory
    private lateinit var viewModel: EventDetailsViewModel
    private lateinit var settings: SharedPreferences

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val appComponent = (requireActivity().application as App).appComponent
        factory = appComponent.getEventDetailsViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[EventDetailsViewModel::class.java]
        settings = appComponent.getSharedPreferences()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val alertView = inflater.inflate(R.layout.complaint_dialog, null)
        builder.setView(alertView)
        val dialog = builder.create()
        complainText = alertView.findViewById(R.id.complaint_text)
        sendComplaint = alertView.findViewById(R.id.send_complaint)
        sendComplaint.setOnClickListener {
            if (checkField()) {
                viewModel.complain(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                    requireArguments().getString(EVENT_ID_KEY, EMPTY_STRING),
                    makeComplainRequest()
                )
                dialog.dismiss()
            }
        }

        return dialog
    }

    private fun checkField(): Boolean {
        return if (complainText.text.toString() == EMPTY_STRING) {
            complainText.error = requireContext().resources.getText(R.string.error_message)
            false
        } else {
            true
        }
    }

    private fun makeComplainRequest(): EventComplaintRequestBody =
        EventComplaintRequestBody(
            complainText.text.toString()
        )

    companion object {
        private const val EVENT_ID_KEY = "event_id_key"

        fun newInstance(eventId: String): ComplaintDialogFragment {
            val args = Bundle()
            args.putSerializable(EVENT_ID_KEY, eventId)
            val fragment = ComplaintDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}