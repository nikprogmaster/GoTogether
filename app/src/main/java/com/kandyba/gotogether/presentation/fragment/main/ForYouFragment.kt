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
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.presentation.adapter.EventsAdapter
import com.kandyba.gotogether.presentation.viewmodel.ForYouViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.ForYouViewModelFactory

class ForYouFragment : Fragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    private lateinit var factory: ForYouViewModelFactory
    private lateinit var viewModel: ForYouViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.for_you_fragment, container, false)
        eventsRecyclerView = root.findViewById(R.id.events)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        viewModel.init(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        factory = component.getForYouViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[ForYouViewModel::class.java]
        settings = component.getSharedPreferences()
        mainViewModel = ViewModelProvider(requireActivity(), component.getMainViewModelFactory())
            .get(MainViewModel::class.java)
    }

    private fun initObservers() {
        viewModel.eventInfo.observe(requireActivity(), Observer {
            mainViewModel.openFragment(EventFragment.newInstance(it))
        })
        viewModel.enableLikeButton.observe(requireActivity(), Observer {
            eventsAdapter.changeButtonState(it)
        })
        viewModel.eventNotLiked.observe(requireActivity(), Observer {
            eventsAdapter.changeUserLikedProperty(it)
        })
        viewModel.eventsRecommendations.observe(requireActivity(), Observer {
            initEventsAdapter(it)
        })
    }

    private fun initEventsAdapter(events: List<EventModel>) {
        eventsAdapter = EventsAdapter(
            events.toMutableList(),
            object : EventsAdapter.OnEventClickListener {
                override fun onClick(eventId: String) {
                    viewModel.loadEventInfo(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        eventId
                    )
                }
                override fun onLikeButtonClick(eventId: String) {
                    viewModel.likeEvent(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        eventId
                    )
                }

            })
        eventsRecyclerView.adapter = eventsAdapter
    }

    companion object {
        fun newInstance(): ForYouFragment {
            val args = Bundle()

            val fragment = ForYouFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.makeForYouEventsToolbar()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.eventInfo.removeObservers(requireActivity())
        viewModel.enableLikeButton.removeObservers(requireActivity())
        viewModel.eventNotLiked.removeObservers(requireActivity())
    }
}