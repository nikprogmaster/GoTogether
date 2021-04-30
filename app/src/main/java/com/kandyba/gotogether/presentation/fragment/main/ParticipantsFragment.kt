package com.kandyba.gotogether.presentation.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.ParticipantsList
import com.kandyba.gotogether.presentation.adapter.OnProfileButtonClickListener
import com.kandyba.gotogether.presentation.adapter.ParticipantsAdapter
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel

class ParticipantsFragment : Fragment() {

    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.participants_fragment, container, false)
        participantsRecyclerView = root.findViewById(R.id.participants_recycler_view)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = (requireActivity().application as App).appComponent.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]
        val list = (requireArguments().get(PARTICIPANTS_KEY) as ParticipantsList).participants
        val adapter = ParticipantsAdapter(list, object : OnProfileButtonClickListener {
            override fun onProfileClick(userId: String) {
                mainViewModel.openFragment(ProfileFragment.newInstance(userId))
            }
        })
        val manager = GridLayoutManager(requireContext(), 2)

        participantsRecyclerView.adapter = adapter
        participantsRecyclerView.layoutManager = manager
    }

    companion object {
        fun newInstance(list: ParticipantsList): ParticipantsFragment {
            val args = Bundle()
            args.putSerializable(PARTICIPANTS_KEY, list)
            val fragment = ParticipantsFragment()
            fragment.arguments = args
            return fragment
        }

        private const val PARTICIPANTS_KEY = "participants_key"
    }
}