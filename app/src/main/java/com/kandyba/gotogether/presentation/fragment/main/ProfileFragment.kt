package com.kandyba.gotogether.presentation.fragment.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
import com.kandyba.gotogether.presentation.viewmodel.MainViewModel
import com.kandyba.gotogether.presentation.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var profileAvatar: ImageView
    private lateinit var userName: TextView
    private lateinit var userAge: TextView
    private lateinit var editButton: Button
    private lateinit var description: TextView
    private lateinit var showAllButton: TextView
    private lateinit var myEvents: RecyclerView

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settings: SharedPreferences

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
            settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING,
            false
        )
    }

    private fun initViews(root: View) {
        backButton = root.findViewById(R.id.profile_back_btn)
        profileAvatar = root.findViewById(R.id.profile_avatar)
        userName = root.findViewById(R.id.user_name)
        userAge = root.findViewById(R.id.user_age)
        editButton = root.findViewById(R.id.edit_button)
        description = root.findViewById(R.id.description_text)
        showAllButton = root.findViewById(R.id.show_all)
        myEvents = root.findViewById(R.id.my_events_recycler)
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        val factory = component.getProfileViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)
            .get(ProfileViewModel::class.java)
        val mainFactory = component.getMainViewModelFactory()
        mainViewModel = ViewModelProvider(requireActivity(), mainFactory)
            .get(MainViewModel::class.java)
        mainViewModel.showToolbar(false)
        settings = component.getSharedPreferences()
    }

    private fun initListeners() {
        backButton.setOnClickListener { mainViewModel.closeFragment() }
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
    }

    private fun initObservers() {
        viewModel.userInfo.observe(requireActivity(), Observer { userInfo ->
            setViews(userInfo)
        })
    }

    private fun setViews(userInfo: UserInfoModel) {
        userName.text = userInfo.firstName
        description.text = userInfo.info
        Picasso.get()
            .load(userInfo.avatar)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .into(profileAvatar)
        userAge.text = userInfo.birthDate?.toLong()?.let { getAge(it) }
        if (description.lineCount < FIRST_VISIBLE_LINES_NUMBER) {
            showAllButton.visibility = View.GONE
        }
    }

    companion object {
        private const val FIRST_VISIBLE_LINES_NUMBER = 5
        fun newInstance(): ProfileFragment {
            val args = Bundle()

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
}