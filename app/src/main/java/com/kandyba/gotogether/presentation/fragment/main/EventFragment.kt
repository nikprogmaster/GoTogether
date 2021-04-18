package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.domain.events.ParticipantsList
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.EVENT_KEY
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.presentation.adapter.CategoriesAdapter
import com.kandyba.gotogether.presentation.adapter.ScheduleAdapter
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
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
    private lateinit var addressTextView: TextView
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var peopleGroup: ConstraintLayout
    private lateinit var person1: CircleImageView
    private lateinit var person2: CircleImageView
    private lateinit var person3: CircleImageView
    private lateinit var person4: CircleImageView
    private lateinit var tellError: TextView
    private lateinit var complainButton: Button

    private lateinit var participantsList: List<CircleImageView>
    private lateinit var viewModel: EventDetailsViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

    private var expanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        MapKitFactory.initialize(requireActivity())
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.event_detail_fragment, container, false)
        initViews(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val event = arguments?.get(EVENT_KEY) as EventDetailsDomainModel
        setValues(event)
        resolveDependencies()
        setClickListeners(event)
        initObservers()
    }

    private fun resolveDependencies() {
        val appComponent = (requireActivity().application as App).appComponent
        viewModel =
            ViewModelProvider(requireActivity(), appComponent.getEventDetailsViewModelFactory())
                .get(EventDetailsViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity(), appComponent.getMainViewModelFactory())
            .get(MainViewModel::class.java)
        mainViewModel.showToolbar(false)
        settings = appComponent.getSharedPreferences()
    }

    private fun initViews(root: View) {
        cover = root.findViewById(R.id.cover)
        back = root.findViewById(R.id.back_arrow)
        likeButton = root.findViewById(R.id.like_event)
        categoriesRecycler = root.findViewById(R.id.categories_event_list)
        title = root.findViewById(R.id.event_title)
        description = root.findViewById(R.id.description)
        showAllText = root.findViewById(R.id.show_all_text)
        price = root.findViewById(R.id.price_value)
        age = root.findViewById(R.id.age)
        mapView = root.findViewById(R.id.mapview)
        addressTextView = root.findViewById(R.id.address)
        scheduleRecyclerView = root.findViewById(R.id.schedule_list)
        peopleGroup = root.findViewById(R.id.people_group)
        person1 = root.findViewById(R.id.person1)
        person2 = root.findViewById(R.id.person2)
        person3 = root.findViewById(R.id.person3)
        person4 = root.findViewById(R.id.person4)
        tellError = root.findViewById(R.id.tell_about_error)
        complainButton = root.findViewById(R.id.complain_btn)

        participantsList = listOf(person1, person2, person3, person4)
    }

    private fun setValues(event: EventDetailsDomainModel) {
        event.images.let { if (it.isNotEmpty()) setImageWithPicasso(it[0], cover) }
        title.text = event.title
        description.movementMethod = LinkMovementMethod.getInstance()
        description.text = event.description
        price.text =
            if (event.isFree == true) requireContext().getString(R.string.free) else event.price
        age.text = event.ageRestriction
        likeButton.isChecked = event.likedByUser

        //set
        val hideViewsList = defineHideViewList(event)
        hideViews(hideViewsList)
        participantsList = participantsList.minus(hideViewsList)
        if (event.amountOfParticipants != null && event.amountOfParticipants > 0) {
            for (i in 0 until event.amountOfParticipants) {
                event.participants?.get(i)?.avatar?.let {
                    setImageWithPicasso(
                        it,
                        participantsList[i]
                    )
                }
            }
        }

        //set categoriesAdapter
        val categoriesAdapter = CategoriesAdapter()
        event.categories?.let { categoriesAdapter.setCategories(it) }
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        categoriesRecycler.adapter = categoriesAdapter
        categoriesRecycler.layoutManager = manager

        //set scheduleAdapter
        val scheduleAdapter = event.dates?.let { ScheduleAdapter(it) }
        val scheduleManager = GridLayoutManager(requireContext(), 2)
        scheduleRecyclerView.adapter = scheduleAdapter
        scheduleRecyclerView.layoutManager = scheduleManager

        configureMap(event)
    }

    private fun setClickListeners(event: EventDetailsDomainModel) {
        complainButton.setOnClickListener {
            val complaintDialogFragment =
                ComplaintDialogFragment.newInstance("d83b5792-4273-40d2-babd-6e2a05865894")
            mainViewModel.showDialogFragment(complaintDialogFragment)
        }

        peopleGroup.setOnClickListener {
            val participants =
                (requireArguments().getSerializable(EVENT_KEY) as EventDetailsDomainModel).participants
            if (participants != null) {
                mainViewModel.openFragment(
                    ParticipantsFragment.newInstance(
                        ParticipantsList(
                            participants
                        )
                    )
                )
                mainViewModel.showToolbar(true)
                mainViewModel.makeParticipantsToolbar()
            }
        }

        showAllText.setOnClickListener {
            if (expanded) {
                showAllText.text = resources.getText(R.string.show_all)
                description.maxLines = 5
                description.text = Html.fromHtml(event.bodyText)
                description.linksClickable = true
                description.movementMethod = LinkMovementMethod.getInstance()
            } else {
                description.maxLines = 10000
                description.text = Html.fromHtml(event.bodyText)
                description.linksClickable = true
                description.movementMethod = LinkMovementMethod.getInstance()
                showAllText.text = resources.getText(R.string.roll_up)
            }
            expanded = !expanded
        }

        likeButton.setOnClickListener {
            viewModel.likeEvent(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                event.id
            )
        }
        back.setOnClickListener {
            mainViewModel.closeFragment()
        }
    }

    private fun changeUserParticipation(eventId: String) {
        Cache.instance.getCachedEvents()
            ?.map { if (it.id == eventId) it.likedByUser = !it.likedByUser }
    }

    private fun initObservers() {
        viewModel.enableLikeButton.observe(requireActivity(), Observer {
            likeButton.isEnabled = it
        })
        viewModel.changeEventLikeProperty.observe(requireActivity(), Observer {
            changeUserParticipation(it)
        })
    }

    private fun configureMap(event: EventDetailsDomainModel) {
        if (event.latitude != null && event.longitude != null) {
            val point = Point(event.latitude.toDouble(), event.longitude.toDouble())
            mapView.map.move(
                CameraPosition(point, 16.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 5f),
                null
            )
            SearchFactory.initialize(requireContext());
            val searchManager =
                SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
            val geometryPoint = Geometry.fromPoint(point)
            val searchSession = searchManager.submit(point, 16, SearchOptions(),
                object : Session.SearchListener {
                    override fun onSearchError(p0: Error) {}
                    override fun onSearchResponse(p0: Response) {
                        val address = p0.collection.children.firstOrNull()?.obj
                            ?.metadataContainer
                            ?.getItem(ToponymObjectMetadata::class.java)
                            ?.address
                            ?.formattedAddress
                        addressTextView.text = address
                    }
                }
            )
        }

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