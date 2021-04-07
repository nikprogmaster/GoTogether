package com.kandyba.gotogether.presentation.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kandyba.gotogether.R

/**
 * @author Кандыба Никита
 * @since 05.04.2021
 */
class SearchFragment : Fragment() {

    private lateinit var search: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var eventsRecyclerView: RecyclerView

    //private lateinit var eventsAdapter:

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_fragment, container, false)
        search = root.findViewById(R.id.search)
        fab = root.findViewById(R.id.fab)
        eventsRecyclerView = root.findViewById(R.id.events_recycler)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private fun initViews() {

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