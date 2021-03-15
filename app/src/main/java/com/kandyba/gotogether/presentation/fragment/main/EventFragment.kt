package com.kandyba.gotogether.presentation.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EVENT_KEY
import com.kandyba.gotogether.presentation.adapter.CategoriesAdapter
import com.squareup.picasso.Picasso
import com.yandex.mapkit.mapview.MapView
import de.hdodenhof.circleimageview.CircleImageView

class EventFragment : Fragment() {

    private lateinit var cover: ImageView
    private lateinit var back: ImageView
    private lateinit var likeButton: ToggleButton
    private lateinit var categoriesRecycler: RecyclerView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var showAllText: TextView
    private lateinit var price: TextView
    private lateinit var age: TextView
    private lateinit var mapView: MapView
    private lateinit var address: TextView
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var peopleGroup: FrameLayout
    private lateinit var person1: CircleImageView
    private lateinit var person2: CircleImageView
    private lateinit var person3: CircleImageView
    private lateinit var person4: CircleImageView
    private lateinit var tellError: TextView
    private lateinit var complainButton: Button

    private var participantsList = listOf(person1, person2, person3, person4)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.event_detail_fragment, container, false)
        initViews(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val event = arguments?.get(EVENT_KEY) as EventDetailsDomainModel
        setValues(event)
    }

    private fun initViews(root: View) {
        cover = root.findViewById(R.id.cover)
        back = root.findViewById(R.id.back_arrow)
        likeButton = root.findViewById(R.id.like)
        categoriesRecycler = root.findViewById(R.id.categories_event_list)
        title = root.findViewById(R.id.event_title)
        description = root.findViewById(R.id.event_description)
        showAllText = root.findViewById(R.id.show_all_text)
        price = root.findViewById(R.id.price_value)
        age = root.findViewById(R.id.age)
        mapView = root.findViewById(R.id.mapview)
        address = root.findViewById(R.id.address)
        scheduleRecyclerView = root.findViewById(R.id.schedule_list)
        peopleGroup = root.findViewById(R.id.people_group)
        person1 = root.findViewById(R.id.person1)
        person2 = root.findViewById(R.id.person2)
        person3 = root.findViewById(R.id.person3)
        person4 = root.findViewById(R.id.person4)
        tellError = root.findViewById(R.id.tell_about_error)
        complainButton = root.findViewById(R.id.complain_btn)
    }

    private fun setValues(event: EventDetailsDomainModel) {
        setImageWithPicasso(event.images[0], cover)
        title.text = event.title
        description.text = event.description
        price.text = if (event.isFree) requireContext().getString(R.string.free) else event.price
        age.text = event.ageRestriction

        //set
        val hideViewsList = defineHideViewList(event)
        hideViews(hideViewsList)
        participantsList = participantsList.minus(hideViewsList)
        for (i in 0..event.amountOfParticipants) {
            setImageWithPicasso(event.participants[i].avatar, participantsList[i])
        }

        //set categoriesAdapter
        val categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.setCategories(event.categories)
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        categoriesRecycler.adapter = categoriesAdapter
        categoriesRecycler.layoutManager = manager
    }

    private fun defineHideViewList(event: EventDetailsDomainModel): List<CircleImageView> {
        return when (event.amountOfParticipants) {
            0 -> listOf(person1, person2, person3, person4)
            1 -> listOf(person2, person3, person4)
            2 -> listOf(person3, person4)
            3 -> listOf(person4)
            else -> emptyList()
        }
    }

    private fun setImageWithPicasso(from: String, where: ImageView) {
        Picasso.get()
            .load(from)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .into(where)
    }

    private fun hideViews(views: List<CircleImageView>) {
        for (view in views) {
            view.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(event: EventDetailsDomainModel): EventFragment {
            val args = Bundle()
            args.putSerializable(EVENT_KEY, event)
            val fragment = EventFragment()
            fragment.arguments = args
            return fragment
        }
    }
}