package com.kandyba.gotogether.presentation.fragment.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    private lateinit var searchLayout: LinearLayout
    private lateinit var clearQueryButton: ImageView
    private lateinit var fab: FloatingActionButton
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var searchButton: ImageView
    private lateinit var stubLayout: ConstraintLayout
    private lateinit var stubImage: ImageView
    private lateinit var stubTitle: TextView
    private lateinit var stubSubtitle: TextView

    private lateinit var eventsAdapter: ShortEventAdapter
    private var viewModel: SearchViewModel? = null
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
        stubLayout = root.findViewById(R.id.stub_layout)
        stubImage = root.findViewById(R.id.image_stub)
        stubTitle = root.findViewById(R.id.stub_title)
        stubSubtitle = root.findViewById(R.id.stub_subtitle)
        searchLayout = root.findViewById(R.id.search_layout)
        clearQueryButton = root.findViewById(R.id.clear_query)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        configureStub(
            resources.getString(R.string.search),
            resources.getString(R.string.find_interesting_event)
        )
        stubLayout.visibility = View.VISIBLE
        initObservers()
        initListeners()
    }

    private fun resolveDependencies() {
        val factory =
            (requireActivity().application as App).appComponent.getSearchViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[SearchViewModel::class.java]
        val mainFactory =
            (requireActivity().application as App).appComponent.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)[MainViewModel::class.java]
        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
        eventsAdapter =
            ShortEventAdapter(
                mutableListOf(), object : ShortEventAdapter.OnEventClickListener {
                    override fun onClick(eventId: String) {
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
                },
                true
            )
        eventsRecyclerView.adapter = eventsAdapter
    }

    private fun initObservers() {
        viewModel?.closeBottomSheet?.observe(requireActivity(), Observer {
            bottomSheet?.dismiss()
        })
        viewModel?.searchResultEvents?.observe(requireActivity(), Observer { events ->
            if (events.isEmpty()) {
                stubLayout.visibility = View.VISIBLE
                configureStub(
                    resources.getString(R.string.search_stub_title),
                    resources.getString(R.string.search_stub_subtitle)
                )
            } else {
                stubLayout.visibility = View.GONE
                eventsAdapter.setEvents(events)
            }
        })
        viewModel?.enableLikeButton?.observe(requireActivity(), Observer {
            eventsAdapter.changeButtonState(it)
        })
        viewModel?.eventNotLiked?.observe(requireActivity(), Observer {
            eventsAdapter.changeUserLikedProperty(it)
        })
        viewModel?.eventInfo?.observe(requireActivity(), Observer {
            mainViewModel.openFragment(EventFragment.newInstance(it))
        })
        viewModel?.searchValue?.observe(requireActivity(), Observer {
            search.setText(it)
        })
    }

    private fun initListeners() {
        fab.setOnClickListener {
            if (!SearchSettingsBottomSheetDialogFragment.isFragmentExist) {
                bottomSheet = SearchSettingsBottomSheetDialogFragment.newInstance()
                bottomSheet?.show(requireActivity().supportFragmentManager, null)
            }
        }
        search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchButton.visibility = View.VISIBLE
            } else {
                if (search.text.toString() == EMPTY_STRING) {
                    searchButton.visibility = View.GONE
                }
                clearSearchFocus()
            }
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != EMPTY_STRING) {
                    clearQueryButton.visibility = View.VISIBLE
                } else {
                    clearQueryButton.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        searchButton.setOnClickListener {
            if (search.text.toString() != EMPTY_STRING) {
                viewModel?.searchEventsByText(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING, search.text.toString()
                )
            }
            clearSearchFocus()
        }
        clearQueryButton.setOnClickListener {
            search.setText(EMPTY_STRING)
        }
    }

    private fun configureStub(title: String, subTitle: String) {
        stubImage.setImageResource(R.drawable.search_stub)
        stubTitle.text = title
        stubSubtitle.text = subTitle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.eventInfo?.removeObservers(requireActivity())
        viewModel?.searchResultEvents?.removeObservers(requireActivity())
        viewModel?.searchValue?.removeObservers(requireActivity())
        viewModel?.setSearchValue(search.text.toString())
    }

    private fun clearSearchFocus() {
        search.clearFocus()
        clearQueryButton.visibility = View.GONE
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