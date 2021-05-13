package com.kandyba.gotogether.presentation.fragment.prelogin

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
import com.kandyba.gotogether.models.general.requests.SignupRequestBody
import com.kandyba.gotogether.presentation.fragment.FragmentManager
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel

class RegistrationFragment : Fragment() {

    private lateinit var exitButton: ImageView
    private lateinit var continueButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.registration_fragment, container, false)
        exitButton = root.findViewById(R.id.registration_exit_btn)
        continueButton = root.findViewById(R.id.reg_continue_btn)
        email = root.findViewById(R.id.email_et)
        password = root.findViewById(R.id.password_et)
        confirmPassword = root.findViewById(R.id.confirm_password_et)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
        initObservers()
    }

    private fun resolveDependencies() {
        val factory = (requireActivity().application as App).appComponent.getStartViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[StartViewModel::class.java]
    }

    private fun initListeners() {
        exitButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
        continueButton.setOnClickListener {
            if (validateFields()) {
                viewModel.signup(createRequest(email.text.toString(), password.text.toString()))
            }
        }
    }

    private fun initObservers() {
        viewModel.signupResponse.observe(this, Observer {
            viewModel.login(email.text.toString(), password.text.toString(), true)
        })
        viewModel.loginResponse.observe(this, Observer {
            if (!viewModel.requestEvents) {
                (activity as FragmentManager).openFragment(AboutMeFragment.newInstance())
            }
        })
    }

    private fun createRequest(
        email: String,
        password: String
    ): SignupRequestBody {
        return SignupRequestBody(email, password)
    }

    private fun validateFields(): Boolean {
        var isCorrect = true
        if (email.text.toString() == "") {
            isCorrect = false
            email.error = resources.getString(R.string.error_message)
        }
        if (password.text.toString() == "") {
            isCorrect = false
            password.error = resources.getString(R.string.error_message)
        }
        if (confirmPassword.text.toString() == "") {
            isCorrect = false
            confirmPassword.error = resources.getString(R.string.error_message)
        }
        if (password.text.toString() != confirmPassword.text.toString()) {
            confirmPassword.error = resources.getString(R.string.passwords_must_be_equal)
            isCorrect = false
        }
        if (password.text.toString().length < 8) {
            password.error = resources.getString(R.string.passwords_must_be_more_than_eight_symbols)
            isCorrect = false
        }
        if (confirmPassword.text.toString().length < 8) {
            confirmPassword.error =
                resources.getString(R.string.passwords_must_be_more_than_eight_symbols)
            isCorrect = false
        }
        return isCorrect
    }

    override fun onDestroy() {
        viewModel.loginResponse.removeObservers(requireActivity())
        viewModel.signupResponse.removeObservers(requireActivity())
        super.onDestroy()
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            val args = Bundle()

            val fragment = RegistrationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}