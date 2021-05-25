package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.kandyba.gotogether.models.general.*
import com.kandyba.gotogether.presentation.adapter.CategoriesAdapter
import com.kandyba.gotogether.presentation.adapter.ScheduleAdapter
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.ui_view.ViewProvider
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Фрагмент, отображающий подробную информацию о событии
 */
class EventFragment : Fragment() {

    private lateinit var cover: ImageView
    private lateinit var carousel: CarouselView
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
    private lateinit var morePeople: CircleImageView
    private lateinit var complainButton: Button
    private lateinit var whenToGoTitle: TextView
    private lateinit var timetable: TextView
    private lateinit var whoWillGoTitle: TextView
    private lateinit var eventPlace: LinearLayout
    private lateinit var participantsList: List<CircleImageView>

    private var viewModel: EventDetailsViewModel? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences
    private var expanded = false
    private var bottomSheet: MapFragment? = null
    private val searchListener = object : Session.SearchListener {
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
        resolveDependencies()
        setValues(event)
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

        //set carousel images
        val event = arguments?.get(EVENT_KEY) as EventDetailsDomainModel
        if (event.images.isNotEmpty()) {
            viewModel?.loadImages(event.images)
        }
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
        complainButton = root.findViewById(R.id.complain_btn)
        whenToGoTitle = root.findViewById(R.id.when_to_go)
        timetable = root.findViewById(R.id.timetable)
        morePeople = root.findViewById(R.id.more_people)
        whoWillGoTitle = root.findViewById(R.id.who_will_go)
        eventPlace = root.findViewById(R.id.event_place)
        carousel = root.findViewById(R.id.carousel)

        participantsList = listOf(person1, person2, person3, person4, morePeople)
    }

    private fun setValues(event: EventDetailsDomainModel) {
        event.images.let { if (it.isNotEmpty()) setImageWithPicasso(it[0], cover) }
        title.text = event.title.capitalize()
        description.movementMethod = LinkMovementMethod.getInstance()
        description.text = Html.fromHtml(event.bodyText)
        price.text =
            if (event.isFree == true) requireContext().getString(R.string.free) else event.price
        age.text = event.ageRestriction
        likeButton.isChecked = event.likedByUser

        setWhoWillGoSection(event)
        setWhenToGoSection(event)
        setCategoriesAdapter(event)
        setScheduleAdapter(event)
        configureMap(event)
    }

    private fun setWhoWillGoSection(event: EventDetailsDomainModel) {
        val hideViewsList = defineHideViewList(event)
        hideViews(hideViewsList)
        participantsList = participantsList.minus(hideViewsList)
        if (event.amountOfParticipants != null && event.amountOfParticipants > 0) {
            for (i in 0 until event.amountOfParticipants) {
                event.participants?.get(i)?.let {
                    if (it.id != settings.getString(USER_ID, EMPTY_STRING)) {
                        setImageWithPicasso(it.avatar, participantsList[i])
                    }
                }
            }
        }
    }

    private fun setWhenToGoSection(event: EventDetailsDomainModel) {
        if (event.dates != null && event.dates.isNotEmpty()) {
            scheduleRecyclerView.visibility = View.VISIBLE
        } else if (event.place?.timetable != null && event.place.timetable != EMPTY_STRING) {
            timetable.visibility = View.VISIBLE
            val time = event.place.timetable?.capitalize()
            timetable.text = time
        } else {
            whenToGoTitle.visibility = View.GONE
        }
    }

    private fun setCategoriesAdapter(event: EventDetailsDomainModel) {
        val categoriesAdapter = CategoriesAdapter()
        event.categories?.let { categoriesAdapter.setCategories(it) }
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoriesRecycler.adapter = categoriesAdapter
        categoriesRecycler.layoutManager = manager
    }

    private fun setScheduleAdapter(event: EventDetailsDomainModel) {
        val scheduleAdapter = event.dates?.let { ScheduleAdapter(it) }
        val scheduleManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        scheduleRecyclerView.adapter = scheduleAdapter
        scheduleRecyclerView.layoutManager = scheduleManager
    }

    private fun setClickListeners(event: EventDetailsDomainModel) {
        complainButton.setOnClickListener {
            val complaintDialogFragment =
                ComplaintDialogFragment.newInstance(
                    (requireArguments().getSerializable(EVENT_KEY) as EventDetailsDomainModel).id
                )
            mainViewModel.showDialogFragment(complaintDialogFragment)
        }

        peopleGroup.setOnClickListener {
            val participants =
                (requireArguments().getSerializable(EVENT_KEY) as EventDetailsDomainModel).participants?.toMutableList()
                    ?: mutableListOf()
            if (participants.isNotEmpty()) {
                val iAm = participants.find { it.id == settings.getString(USER_ID, EMPTY_STRING) }
                if (iAm != null) {
                    participants.remove(iAm)
                }
            }
            mainViewModel.openFragment(
                ParticipantsFragment.newInstance(ParticipantsList(participants))
            )
            mainViewModel.showToolbar(true)
            mainViewModel.makeParticipantsToolbar()
        }

        showAllText.setOnClickListener {
            if (expanded) {
                showAllText.text = resources.getText(R.string.show_all)
                description.maxLines = COLLAPSED_LINES_COUNT
            } else {
                description.maxLines = EXPANDED_LINES_COUNT
                showAllText.text = resources.getText(R.string.roll_up)
            }
            expanded = !expanded
        }

        likeButton.setOnClickListener {
            viewModel?.likeEvent(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                event.id
            )
        }
        back.setOnClickListener {
            mainViewModel.closeFragment()
        }
        eventPlace.setOnClickListener {
            if (!MapFragment.isFragmentExist) {
                val ev = requireArguments().get(EVENT_KEY) as? EventDetailsDomainModel
                bottomSheet = MapFragment.newInstance(ev)
                bottomSheet?.show(requireActivity().supportFragmentManager, null)
            }
        }
    }

    private fun changeUserParticipation(eventId: String) {
        Cache.instance.getCachedEvents()
            ?.map { if (it.id == eventId) it.likedByUser = !it.likedByUser }
    }

    private fun initObservers() {
        viewModel?.enableLikeButton?.observe(requireActivity(), Observer {
            likeButton.isEnabled = it
        })
        viewModel?.changeEventLikeProperty?.observe(requireActivity(), Observer {
            changeUserParticipation(it)
        })
        viewModel?.eventImages?.observe(requireActivity(), Observer { imagesList ->
            val imageListener = ImageListener { position, imageView ->
                imageView.setImageBitmap(imagesList[position])
            }
            carousel.setImageListener(imageListener)
            carousel.pageCount = imagesList.size
            cover.visibility = View.GONE
        })
    }

    private fun configureMap(event: EventDetailsDomainModel) {
        mapView.setNoninteractive(true)
        if (event.place?.latitude != null && event.place.longitude != null) {
            val point = Point(event.place.latitude.toDouble(), event.place.longitude.toDouble())
            mapView.map.move(
                CameraPosition(point, ZOOM_FACTOR, AZIMUTH_FACTOR, TILT_FACTOR),
                Animation(Animation.Type.SMOOTH, DURATION),
                null
            )
            val placeMark = View(requireContext()).apply {
                background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.mc_baseline_place_36dp)
            }
            mapView.map.mapObjects.addPlacemark(point, ViewProvider(placeMark))
            SearchFactory.initialize(requireContext())
            val searchManager =
                SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
            searchManager.submit(point, ZOOM_FACTOR.toInt(), SearchOptions(), searchListener)
        } else {
            addressTextView.text = resources.getString(R.string.place_not_defined)
        }
    }

    private fun defineHideViewList(event: EventDetailsDomainModel): List<CircleImageView> {
        var realParticipantsCount = event.amountOfParticipants ?: 0
        if (event.participants != null && event.participants.isNotEmpty()) {
            for (participant in event.participants) {
                if (participant.id == settings.getString(USER_ID, EMPTY_STRING)) {
                    realParticipantsCount--
                }
            }
        }
        return when (realParticipantsCount) {
            0 -> listOf(person1, person2, person3, person4)
            1 -> listOf(person2, person3, person4)
            2 -> listOf(person3, person4)
            3 -> listOf(person4)
            else -> emptyList()
        }
    }

    private fun setImageWithPicasso(from: String?, where: ImageView) {
        from?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.ill_placeholder_300dp)
                .error(R.drawable.ill_placeholder_300dp)
                .into(where)
        }
    }

    private fun hideViews(views: List<CircleImageView>) {
        for (view in views) {
            view.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.eventImages?.removeObservers(requireActivity())
        viewModel?.changeEventLikeProperty?.removeObservers(requireActivity())
        viewModel?.enableLikeButton?.removeObservers(requireActivity())
    }

    companion object {
        fun newInstance(event: EventDetailsDomainModel): EventFragment {
            val args = Bundle()
            args.putSerializable(EVENT_KEY, event)
            val fragment = EventFragment()
            fragment.arguments = args
            return fragment
        }

        private const val COLLAPSED_LINES_COUNT = 5
        private const val EXPANDED_LINES_COUNT = 10000
        private const val SPAN_COUNT = 2
        private const val ZOOM_FACTOR = 16.0f
        private const val AZIMUTH_FACTOR = 0.0f
        private const val TILT_FACTOR = 0.0f
        private const val DURATION = 3.0f
    }

}