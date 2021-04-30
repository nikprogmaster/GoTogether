package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.presentation.adapter.DialogsAdapter
import com.kandyba.gotogether.presentation.adapter.OnDialogClickListener
import com.kandyba.gotogether.presentation.viewmodel.DialogsViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel


class DialogsFragment : Fragment() {

    private lateinit var dialogsRecyclerView: RecyclerView
    private lateinit var adapter: DialogsAdapter

    private lateinit var viewModel: DialogsViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialogs_fragment, container, false)
        dialogsRecyclerView = root.findViewById(R.id.dialogs_recycler)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        mainViewModel.showToolbar(true)
        mainViewModel.setAppbarTitle(requireContext().getString(R.string.dialogs))
        viewModel.getUserDialogs(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
        if (requireArguments().getString(COMPANION_ID) != null) {
            viewModel.createDialog(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                requireArguments().getString(COMPANION_ID, EMPTY_STRING)
            )
        }
    }

    private fun resolveDependencies() {
        val factory =
            (requireActivity().application as App).appComponent.getDialogsViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[DialogsViewModel::class.java]
        val mainFactory =
            (requireActivity().application as App).appComponent.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)[MainViewModel::class.java]
        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
    }

    private fun initObservers() {
        viewModel.userDialogs.observe(requireActivity(), Observer { initAdapterOrDoElse(it) })
        viewModel.dialogCreated.observe(requireActivity(), Observer {
            mainViewModel.openFragment(MessagesFragment.newInstance(it.id))
            viewModel.getUserDialogs(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
        })
    }

    private fun initAdapterOrDoElse(dialogs: List<DialogDomainModel>) {
        if (requireArguments().getString(COMPANION_ID) != null) {
            val dialog =
                dialogs.find { it.companion.id == requireArguments().getString(COMPANION_ID) }
            if (dialog == null) {
                viewModel.createDialog(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                    requireArguments().getString(COMPANION_ID, EMPTY_STRING)
                )
            }
        }
        adapter = DialogsAdapter(
            dialogs,
            object : OnDialogClickListener {
                override fun onDialogClick(dialogId: String) {
                    mainViewModel.openFragment(MessagesFragment.newInstance(dialogId))
                }
            }
        )
        dialogsRecyclerView.adapter = adapter
    }

    override fun onDestroy() {
        viewModel.dialogCreated.removeObservers(requireActivity())
        super.onDestroy()
    }

    companion object {
        private const val COMPANION_ID = "companionId"

        fun newInstance(dialogId: String?): DialogsFragment {
            val args = Bundle()
            dialogId?.let { args.putString(COMPANION_ID, dialogId) }
            val fragment = DialogsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}