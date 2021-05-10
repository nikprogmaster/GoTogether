package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.ParticipantsList
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.presentation.adapter.OnProfileButtonClickListener
import com.kandyba.gotogether.presentation.adapter.ParticipantsAdapter
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel

class ParticipantsFragment : Fragment() {

    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var recommendedParticipantsRecyclerView: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private var eventDetailsViewModel: EventDetailsViewModel? = null
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.participants_fragment, container, false)
        participantsRecyclerView = root.findViewById(R.id.participants_recycler_view)
        recommendedParticipantsRecyclerView =
            root.findViewById(R.id.recommended_participants_recycler_view)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = (requireActivity().application as App).appComponent.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]
        val eventFactory =
            (requireActivity().application as App).appComponent.getEventDetailsViewModelFactory()
        eventDetailsViewModel = ViewModelProvider(requireActivity(), eventFactory)
            .get(EventDetailsViewModel::class.java)
        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
        val list = (requireArguments().get(PARTICIPANTS_KEY) as ParticipantsList).participants
        val adapter = ParticipantsAdapter(list, object : OnProfileButtonClickListener {
            override fun onProfileClick(userId: String) {
                mainViewModel.openFragment(ProfileFragment.newInstance(userId))
            }
        })
        mainViewModel.showToolbar(true)
        mainViewModel.makeParticipantsToolbar()

        val manager = GridLayoutManager(requireContext(), 2)
        participantsRecyclerView.adapter = adapter
        participantsRecyclerView.layoutManager = manager
        initObservers()
        eventDetailsViewModel?.getParticipantsRecommendations(
            settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        )
    }

    private fun initObservers() {
        eventDetailsViewModel?.recommendedParticipants?.observe(requireActivity(), Observer {
            val adapter = ParticipantsAdapter(it, object : OnProfileButtonClickListener {
                override fun onProfileClick(userId: String) {
                    mainViewModel.openFragment(ProfileFragment.newInstance(userId))
                }
            })
            val manager = GridLayoutManager(requireContext(), 2)
            recommendedParticipantsRecyclerView.adapter = adapter
            recommendedParticipantsRecyclerView.layoutManager = manager
        })
    }

    override fun onDestroy() {
        eventDetailsViewModel?.recommendedParticipants?.removeObservers(requireActivity())
        super.onDestroy()
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