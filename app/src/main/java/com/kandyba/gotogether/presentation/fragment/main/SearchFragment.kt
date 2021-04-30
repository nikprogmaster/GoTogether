package com.kandyba.gotogether.presentation.fragment.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.presentation.adapter.ShortEventAdapter
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.SearchViewModel


class SearchFragment : Fragment() {

    private lateinit var search: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var searchButton: ImageView

    private lateinit var eventsAdapter: ShortEventAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    private var bottomSheet: SearchSettingsBottomSheetDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        search = root.findViewById(R.id.search)
        fab = root.findViewById(R.id.fab)
        eventsRecyclerView = root.findViewById(R.id.events_recycler)
        searchButton = root.findViewById(R.id.search_button)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        initListeners()
        viewModel.getEventsRecommendation(settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
    }

    private fun resolveDependencies() {
        val factory =
            (requireActivity().application as App).appComponent.getSearchViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[SearchViewModel::class.java]
        val mainFactory =
            (requireActivity().application as App).appComponent.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)[MainViewModel::class.java]
        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
    }

    private fun initObservers() {
        viewModel.eventsRecommendations.observe(requireActivity(), Observer { events ->
            eventsAdapter =
                ShortEventAdapter(
                    events.toMutableList(), object : ShortEventAdapter.OnEventClickListener {
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
                    },
                    true
                )
            eventsRecyclerView.adapter = eventsAdapter
        })
        viewModel.closeBottomSheet.observe(requireActivity(), Observer {
            bottomSheet?.dismiss()
        })
        viewModel.searchResultEvents.observe(requireActivity(), Observer {
            eventsAdapter.setEvents(it)
        })
        viewModel.enableLikeButton.observe(requireActivity(), Observer {
            eventsAdapter.changeButtonState(it)
        })
        viewModel.eventNotLiked.observe(requireActivity(), Observer {
            eventsAdapter.changeUserLikedProperty(it)
        })
        viewModel.eventInfo.observe(requireActivity(), Observer {
            mainViewModel.openFragment(EventFragment.newInstance(it))
        })
    }

    private fun initListeners() {
        fab.setOnClickListener {
            if (SearchSettingsBottomSheetDialogFragment.fragment == null) {
                bottomSheet = SearchSettingsBottomSheetDialogFragment.newInstance()
                bottomSheet?.show(requireActivity().supportFragmentManager, null)
            }
        }
        search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                searchButton.visibility = View.VISIBLE
            } else {
                if (search.text.toString() == EMPTY_STRING) {
                    searchButton.visibility = View.GONE
                }
            }
            if (!hasFocus) clearSearchFocus()
        }
        searchButton.setOnClickListener {
            if (search.text.toString() != EMPTY_STRING) {
                viewModel.searchEventsByText(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING, search.text.toString()
                )
            }
            clearSearchFocus()
        }
    }

    override fun onDestroy() {
        viewModel.eventInfo.removeObservers(requireActivity())
        super.onDestroy()
    }

    private fun clearSearchFocus() {
        search.clearFocus()
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(search.windowToken, 0)
    }

    companion object {
        fun newInstance(): SearchFragment {
            val args = Bundle()

            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
}