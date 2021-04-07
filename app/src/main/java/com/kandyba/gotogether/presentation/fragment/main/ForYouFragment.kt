package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.*
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
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        factory = component.getForYouViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[ForYouViewModel::class.java]
        settings = component.getSharedPreferences()

        val events = arguments?.get(EVENTS_KEY) as Events
        eventsAdapter = EventsAdapter(
            events.events.toMutableList(),
            object : EventsAdapter.OnEventClickListener {
                override fun onClick(eventId: String) {
                    viewModel.loadEventInfo(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        eventId
                    )
                }

                override fun onLikeButtonClick(
                    eventId: String,
                    probability: Probability
                ) {
                    viewModel.likeEvent(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        ParticipationRequestBody(probability.value),
                        eventId
                    )
                }

            })
        eventsRecyclerView.adapter = eventsAdapter
    }

    private fun initObservers() {
        viewModel.eventInfo.observe(requireActivity(), Observer {
            (requireActivity() as FragmentManager).openFragment(EventFragment.newInstance(it))
            Log.i("ForYouFragment", "Открываю опять фрагмент")
        })
        viewModel.enableLikeButton.observe(requireActivity(), Observer {
            eventsAdapter.changeButtonState(it)
        })
        viewModel.eventLiked.observe(requireActivity(), Observer {
            eventsAdapter.changeUserLikedProperty(it)
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

    override fun onResume() {
        super.onResume()
        Log.i("Вызываюся!!!!!", "ДААА")
        viewModel.makeForYouEventsToolbar()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.eventInfo.removeObservers(requireActivity())
        viewModel.enableLikeButton.removeObservers(requireActivity())
        viewModel.eventLiked.removeObservers(requireActivity())
    }
}