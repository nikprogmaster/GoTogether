package com.kandyba.gotogether.presentation.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.kandyba.gotogether.models.general.USER_ID
import com.kandyba.gotogether.models.general.UserRequestBody
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel

class AboutMeFragment : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var backButton: ImageView
    private lateinit var name: EditText
    private lateinit var sex: EditText
    private lateinit var birthday: EditText

    private lateinit var viewModel: StartViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.about_me_fragment, container, false)
        backButton = root.findViewById(R.id.back_btn)
        continueButton = root.findViewById(R.id.continue_btn)
        name = root.findViewById(R.id.name_et)
        sex = root.findViewById(R.id.sex_et)
        birthday = root.findViewById(R.id.birthday_et)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
        initObservers()
    }

    private fun resolveDependencies() {
        val component = (requireActivity().application as App).appComponent
        viewModel = ViewModelProvider(
            requireActivity(),
            component.getStartViewModelFactory()
        )[StartViewModel::class.java]
        settings = component.getSharedPreferences()
    }

    private fun initObservers() {
        viewModel.updateUserInfo.observe(requireActivity(), Observer {
            (activity as FragmentManager).openFragment(DescriptionFragment.newInstance())
            Log.d("AboutMeFragment", "initObservers() called")
        })
    }

    private fun initListeners() {
        continueButton.setOnClickListener {
            val request = createUserUpdateRequest()
            if (request.fields.isNotEmpty()) {
                val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
                val uid = settings.getString(USER_ID, EMPTY_STRING) ?: EMPTY_STRING
                viewModel.updateUserInfo(token, uid, request)
            }
        }
        backButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
    }

    private fun createUserUpdateRequest(): UserRequestBody {
        val body = UserRequestBody(mutableMapOf())
        if (name.text.toString() != EMPTY_STRING) {
            body.fields[NAME_KEY] = name.text.toString()
        }
        if (sex.text.toString() != EMPTY_STRING) {
            body.fields[SEX_KEY] = sex.text.toString()
        }
        if (birthday.text.toString() != EMPTY_STRING) {
            body.fields[BIRTHDAY_KEY] = birthday.text.toString()
        }
        return body
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateUserInfo.removeObservers(requireActivity())
        Log.d("AboutMeFragment", "onDestroyView() called")
    }

    companion object {
        private const val NAME_KEY = "first_name"
        private const val SEX_KEY = "sex"
        private const val BIRTHDAY_KEY = "birth_date"

        fun newInstance(): AboutMeFragment {
            val args = Bundle()

            val fragment = AboutMeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}