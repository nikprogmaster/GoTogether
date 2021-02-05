package com.kandyba.gotogether.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EVENTS_KEY
import com.kandyba.gotogether.models.presentation.Events
import com.kandyba.gotogether.presentation.adapter.EventsAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val events: Events = intent.extras?.getSerializable(EVENTS_KEY) as Events

        eventsRecyclerView = findViewById(R.id.events)
        eventsAdapter = EventsAdapter()
        eventsAdapter.setEvents(events.events)
        eventsRecyclerView.adapter = eventsAdapter
    }

}