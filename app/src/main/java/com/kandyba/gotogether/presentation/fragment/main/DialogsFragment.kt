package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

/**
 * Фрагмент, отображающий функционал диалогов
 */
class DialogsFragment : Fragment() {

    private lateinit var dialogsRecyclerView: RecyclerView
    private lateinit var adapter: DialogsAdapter
    private lateinit var dialogsLoadingBar: ProgressBar
    private lateinit var stubLayout: ConstraintLayout
    private lateinit var stubImage: ImageView
    private lateinit var stubTitle: TextView
    private lateinit var stubSubtitle: TextView

    private var viewModel: DialogsViewModel? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences
    private var wasOpenedWithMessage: Boolean = false
    private val dialogListener = object : OnDialogClickListener {
        override fun onDialogClick(dialogId: String, interlocutor: String?) {
            mainViewModel.openFragment(
                MessagesFragment.newInstance(dialogId, interlocutor, null)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.dialogs_fragment, container, false)
        dialogsRecyclerView = root.findViewById(R.id.dialogs_recycler)
        dialogsLoadingBar = root.findViewById(R.id.dialogs_loading)
        stubLayout = root.findViewById(R.id.stub_layout)
        stubImage = root.findViewById(R.id.image_stub)
        stubTitle = root.findViewById(R.id.stub_title)
        stubSubtitle = root.findViewById(R.id.stub_subtitle)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        configureStub()
        mainViewModel.showToolbar(true)
        mainViewModel.setAppbarTitle(requireContext().getString(R.string.dialogs))
        viewModel?.getUserDialogs(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
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
        viewModel?.userDialogs?.observe(requireActivity(), Observer { initAdapterOrDoElse(it) })
        viewModel?.dialogCreated?.observe(requireActivity(), Observer {
            mainViewModel.openFragment(
                MessagesFragment.newInstance(
                    it.id,
                    null,
                    requireArguments().getString(COMPANION_ID)
                )
            )
            wasOpenedWithMessage = true
        })
        viewModel?.showProgress?.observe(requireActivity(), Observer {
            showDialogsLoading(it)
        })
    }

    private fun configureStub() {
        stubImage.setImageResource(R.drawable.dialogs_stub)
        stubTitle.text = resources.getString(R.string.dialogs_stub_title)
        stubSubtitle.text = resources.getString(R.string.dialogs_stub_subtitle)
    }

    private fun initAdapterOrDoElse(dialogs: List<DialogDomainModel>) {
        if (requireArguments().getString(COMPANION_ID) != null) {
            val dialog =
                dialogs.find { it.companion.id == requireArguments().getString(COMPANION_ID) }
            if (dialog == null) {
                viewModel?.createDialog(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                    requireArguments().getString(COMPANION_ID, EMPTY_STRING)
                )
            } else if (!wasOpenedWithMessage) {
                wasOpenedWithMessage = true
                mainViewModel.openFragment(
                    MessagesFragment.newInstance(dialog.id, dialog.companion.firstName, null)
                )
            }
        }
        if (dialogs.isEmpty()) {
            stubLayout.visibility = View.VISIBLE
        } else {
            stubLayout.visibility = View.GONE
            adapter = DialogsAdapter(dialogs, dialogListener)
            dialogsRecyclerView.adapter = adapter
        }
    }

    private fun showDialogsLoading(loading: Boolean) {
        dialogsLoadingBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.dialogCreated?.removeObservers(requireActivity())
        viewModel?.userDialogs?.removeObservers(requireActivity())
        viewModel?.showProgress?.removeObservers(requireActivity())
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