package com.kandyba.gotogether.presentation.fragment.prelogin

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.presentation.LevelInterest
import com.kandyba.gotogether.models.presentation.getListOfCategories
import com.kandyba.gotogether.presentation.adapter.InterestsAdapter
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel

/**
 * Фрагмент для указания интересов
 */
class InterestsFragment : Fragment() {

    private lateinit var interestAdapter: InterestsAdapter
    private lateinit var interestList: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var continueButton: Button

    private lateinit var viewModel: StartViewModel
    private lateinit var settings: SharedPreferences

    private lateinit var levelInterests: MutableList<LevelInterest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.interests_fragment, container, false)
        interestList = root.findViewById(R.id.interests_list)
        backButton = root.findViewById(R.id.back_btn)
        continueButton = root.findViewById(R.id.continue_btn)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        resolveDependencies()
        initListeners()
    }

    private fun initListeners() {
        viewModel.updateUserInterests.observe(requireActivity(), Observer {
            viewModel.getEventsRecommendations(
                settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
            )
        })
    }

    private fun setListeners() {
        backButton.setOnClickListener { (requireActivity() as FragmentManager).closeFragment() }
        continueButton.setOnClickListener {
            val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
            viewModel.updateUserInterests(token, createUserRequest())
        }
    }

    private fun createUserRequest(): UserInterestsRequestBody {
        val map = mutableMapOf<String, Int>()
        for (i in levelInterests) {
            map[i.code] = i.level
        }
        return UserInterestsRequestBody(map)
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        viewModel = ViewModelProvider(
            requireActivity(),
            component.getStartViewModelFactory()
        )[StartViewModel::class.java]
        settings = component.getSharedPreferences()
        interestAdapter = InterestsAdapter(fillInterestsList())
        interestList.adapter = interestAdapter
    }

    private fun fillInterestsList(): MutableList<LevelInterest> {
        val categoriesList = getListOfCategories()
        levelInterests = mutableListOf()
        for (category in categoriesList) {
            levelInterests.add(LevelInterest(category.categoryName, category.serverName))
        }
        return levelInterests
    }

    companion object {
        fun newInstance(): InterestsFragment {
            val args = Bundle()

            val fragment = InterestsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}