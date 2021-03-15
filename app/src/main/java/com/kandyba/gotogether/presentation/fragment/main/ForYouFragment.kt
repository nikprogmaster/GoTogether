package com.kandyba.gotogether.presentation.fragment.main

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
import com.kandyba.gotogether.models.general.EVENTS_KEY
import com.kandyba.gotogether.models.presentation.Events
import com.kandyba.gotogether.presentation.adapter.EventsAdapter
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.ForYouViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.ForYouViewModelFactory

class ForYouFragment : Fragment() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    private lateinit var factory: ForYouViewModelFactory
    private lateinit var viewModel: ForYouViewModel

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
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        factory = component.getForYouViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[ForYouViewModel::class.java]

        val events = arguments?.get(EVENTS_KEY) as Events
        eventsAdapter = EventsAdapter()
        eventsAdapter.setEvents(events.events)
        eventsRecyclerView.adapter = eventsAdapter
    }

    fun initObservers() {
        viewModel.eventInfo.observe(requireActivity(), Observer {
            (requireActivity() as FragmentManager).openFragment(EventFragment.newInstance(it))
        })
    }

    companion object {
        fun newInstance(events: Events): ForYouFragment {
            val args = Bundle()
            args.putSerializable(EVENTS_KEY, events)
            val fragment = ForYouFragment()
            fragment.arguments = args
            return fragment
        }
    }
}