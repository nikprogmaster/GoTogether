package com.kandyba.gotogether.presentation.fragment.main

import android.content.Context
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
    private var viewModel: ForYouViewModel? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    private var lastClickedEventPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(
            TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState"
        )
        val root = inflater.inflate(R.layout.for_you_fragment, container, false)
        eventsRecyclerView = root.findViewById(R.id.events)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated() called with: savedInstanceState = $savedInstanceState")
        resolveDependencies()
        initObservers()
        viewModel?.init(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
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
        viewModel?.eventInfo?.observe(requireActivity(), Observer {
            mainViewModel.openFragment(EventFragment.newInstance(it))
        })
        viewModel?.enableLikeButton?.observe(requireActivity(), Observer {
            eventsAdapter.changeButtonState(it)
        })
        viewModel?.eventNotLiked?.observe(requireActivity(), Observer {
            eventsAdapter.changeUserLikedProperty(it)
        })
        viewModel?.eventsRecommendations?.observe(requireActivity(), Observer {
            initEventsAdapter(it)
        })
    }

    private fun initEventsAdapter(events: List<EventModel>) {
        eventsAdapter = EventsAdapter(
            events.toMutableList(),
            object : EventsAdapter.OnEventClickListener {
                override fun onClick(eventId: String, cardPosition: Int) {
                    lastClickedEventPosition = cardPosition
                    viewModel?.loadEventInfo(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        eventId
                    )
                }

                override fun onLikeButtonClick(eventId: String) {
                    viewModel?.likeEvent(
                        settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                        eventId
                    )
                }

            })
        eventsRecyclerView.adapter = eventsAdapter
        eventsRecyclerView.scrollToPosition(lastClickedEventPosition)
    }

    companion object {
        fun newInstance(): ForYouFragment {
            val args = Bundle()

            val fragment = ForYouFragment()
            fragment.arguments = args
            return fragment
        }

        private const val TAG = "ForYouFragment"
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.makeForYouEventsToolbar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called with: context = $context")
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        Log.d(TAG, "onAttachFragment() called with: childFragment = $childFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView() called")
        viewModel?.eventInfo?.removeObservers(requireActivity())
        viewModel?.enableLikeButton?.removeObservers(requireActivity())
        viewModel?.eventNotLiked?.removeObservers(requireActivity())
    }

}