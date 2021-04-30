package com.kandyba.gotogether.presentation.fragment.prelogin

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel

class DescriptionFragment : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var backButton: ImageView
    private lateinit var aboutYou: EditText

    private lateinit var viewModel: StartViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.description_fragment, container, false)
        backButton = root.findViewById(R.id.back_btn)
        continueButton = root.findViewById(R.id.continue_btn)
        aboutYou = root.findViewById(R.id.about_you)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
        initObservsers()
    }


    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        viewModel = ViewModelProvider(
            requireActivity(),
            component.getStartViewModelFactory()
        )[StartViewModel::class.java]
        settings = component.getSharedPreferences()
    }

    private fun initListeners() {
        backButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
        continueButton.setOnClickListener {
            val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
            viewModel.updateAdditionalUserInfo(token, createUserRequest())
        }
    }

    private fun initObservsers() {
        viewModel.updateAdditionalUserInfo.observe(requireActivity(), Observer {
            if (it != null) {
                (activity as FragmentManager).openFragment(InterestsFragment.newInstance())
            }
        })
    }

    private fun createUserRequest(): UserInfoRequestBody {
        val info = aboutYou.text.toString()
        return UserInfoRequestBody(info)
    }

    override fun onDestroy() {
        viewModel.updateAdditionalUserInfo.removeObservers(requireActivity())
        super.onDestroy()
    }

    companion object {
        private const val INFO_KEY = "info"

        fun newInstance(): DescriptionFragment {
            val args = Bundle()

            val fragment = DescriptionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}