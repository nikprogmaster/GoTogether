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
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.StartViewModelFactory

/**
 * Фрагмент для авторизации пользователя
 */
class AuthFragment : Fragment() {

    private lateinit var createAccountButton: Button
    private lateinit var enterButton: Button
    private lateinit var exitButton: ImageView
    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var viewModel: StartViewModel
    private lateinit var viewModelFactory: StartViewModelFactory
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.auth_fragment, container, false)
        createAccountButton = root.findViewById(R.id.create_account_btn)
        exitButton = root.findViewById(R.id.auth_exit_btn)
        enterButton = root.findViewById(R.id.enter_button)
        email = root.findViewById(R.id.email_et)
        password = root.findViewById(R.id.password_et)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        createAccountButton.setOnClickListener {
            (activity as FragmentManager).openFragment(RegistrationFragment.newInstance())
        }
        exitButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
        enterButton.setOnClickListener {
            if (validateFields()) {
                viewModel.login(email.text.toString(), password.text.toString(), false)
            }
        }
    }

    private fun resolveDependencies() {
        viewModelFactory =
            (requireActivity().application as App).appComponent.getStartViewModelFactory()
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[StartViewModel::class.java]
        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
    }

    private fun initObservers() {
        viewModel.loginResponse.observe(requireActivity(), Observer { response ->
            if (viewModel.requestEvents) {
                viewModel.getEventsRecommendations(response.token)
            }
        })
    }

    private fun validateFields(): Boolean {
        var isEmailCorrect = true
        var isPasswordCorrect = true
        if (email.text.toString() == "") {
            isEmailCorrect = false
            email.error = resources.getString(R.string.error_message)
        }
        if (password.text.toString() == "") {
            isPasswordCorrect = false
            password.error = resources.getString(R.string.error_message)
        }
        return isEmailCorrect && isPasswordCorrect
    }

    companion object {
        fun newInstance(): AuthFragment {
            val args = Bundle()

            val fragment = AuthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}