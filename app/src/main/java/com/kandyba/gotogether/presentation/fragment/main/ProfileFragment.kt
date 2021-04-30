package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.models.presentation.UserInfoModel
import com.kandyba.gotogether.models.presentation.getAge
import com.kandyba.gotogether.presentation.adapter.ShortEventAdapter
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var profileAvatar: CircleImageView
    private lateinit var userName: TextView
    private lateinit var userAge: TextView
    private lateinit var editOrWriteMessageButton: Button
    private lateinit var description: TextView
    private lateinit var showAllButton: TextView
    private lateinit var myEvents: RecyclerView
    private lateinit var myEventsTitle: TextView

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences
    private lateinit var adapter: ShortEventAdapter

    private var expanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.user_profile_fragment, container, false)
        initViews(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        initListeners()
        viewModel.loadUserInfo(
            settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
            requireArguments().getString(CURRENT_USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
            false,
            !checkIsItMyProfile()
        )
    }

    private fun initViews(root: View) {
        profileAvatar = root.findViewById(R.id.profile_avatar)
        userName = root.findViewById(R.id.user_name)
        userAge = root.findViewById(R.id.user_age)
        editOrWriteMessageButton = root.findViewById(R.id.edit_button)
        description = root.findViewById(R.id.description_text)
        showAllButton = root.findViewById(R.id.show_all)
        myEvents = root.findViewById(R.id.my_events_recycler)
        myEventsTitle = root.findViewById(R.id.my_events)
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        val factory = component.getProfileViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)
            .get(ProfileViewModel::class.java)
        val mainFactory = component.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)
            .get(MainViewModel::class.java)
        settings = component.getSharedPreferences()
        adapter = ShortEventAdapter(
            mutableListOf(), object : ShortEventAdapter.OnEventClickListener {
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
                    adapter.removeEvent(eventId)
                }
            },
            false
        )
        myEvents.adapter = adapter
        mainViewModel.makeForYouEventsToolbar()
        mainViewModel.showToolbar(false)
    }

    private fun initListeners() {
        showAllButton.setOnClickListener {
            if (expanded) {
                showAllButton.text = resources.getText(R.string.show_all)
                description.maxLines = 5
            } else {
                description.maxLines = 10000
                showAllButton.text = resources.getText(R.string.roll_up)
            }
            expanded = !expanded
        }
        if (checkIsItMyProfile()) {
            editOrWriteMessageButton.text = resources.getString(R.string.edit)
            editOrWriteMessageButton.setOnClickListener {
                mainViewModel.openFragment(
                    ChangeProfileInfoFragment.newInstance()
                )
            }
        } else {
            editOrWriteMessageButton.text = resources.getString(R.string.type_message)
            editOrWriteMessageButton.setOnClickListener {
                mainViewModel.openFragment(
                    DialogsFragment.newInstance(
                        requireArguments().getString(CURRENT_USER_ID)
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.userInfo.observe(requireActivity(), Observer { userInfo ->
            setViews(userInfo)
        })
        viewModel.enableLikeButton.observe(requireActivity(), Observer {
            adapter.changeButtonState(it)
        })
        viewModel.eventNotLiked.observe(requireActivity(), Observer {
            adapter.changeUserLikedProperty(it)
        })
        viewModel.eventInfo.observe(requireActivity(), Observer {
            mainViewModel.openFragment(EventFragment.newInstance(it))
        })
    }

    private fun setViews(userInfo: UserInfoModel) {
        userName.text = userInfo.firstName
        description.text =
            if (userInfo.info != EMPTY_STRING) userInfo.info else ABOUT_ME_PLACEHOLDER
        Picasso.get()
            .load(userInfo.avatar)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .into(profileAvatar)
        userAge.text = userInfo.birthDate?.toLong()?.let { getAge(it) }
        if (description.lineCount < FIRST_VISIBLE_LINES_NUMBER) {
            showAllButton.visibility = View.GONE
        }
        if (userInfo.likedEvents.isNotEmpty()) {
            myEventsTitle.visibility = View.VISIBLE
        }
        adapter.setEvents(userInfo.likedEvents)
    }

    override fun onDestroy() {
        viewModel.eventInfo.removeObservers(requireActivity())
        super.onDestroy()
    }

    private fun checkIsItMyProfile() =
        settings.getString(USER_ID, EMPTY_STRING) == requireArguments().getString(CURRENT_USER_ID)

    companion object {
        private const val FIRST_VISIBLE_LINES_NUMBER = 5
        private const val ABOUT_ME_PLACEHOLDER = "Вы пока ничего не рассказали другим о себе"
        private const val CURRENT_USER_ID = "current_user"
        fun newInstance(userId: String): ProfileFragment {
            val args = Bundle()
            args.putString(CURRENT_USER_ID, userId)
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
}