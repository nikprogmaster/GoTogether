package com.kandyba.gotogether.presentation.fragment.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.kandyba.gotogether.R
import com.kandyba.gotogether.presentation.fragment.FragmentManager

/**
 * Разводной экран старта приложения
 */
class StartFragment : Fragment() {

    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.start_fragment, container, false)
        enterButton = root.findViewById(R.id.start_enter_btn)
        registrationButton = root.findViewById(R.id.registration_btn)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enterButton.setOnClickListener { (activity as FragmentManager).openFragment(AuthFragment.newInstance()) }
        registrationButton.setOnClickListener {
            (activity as FragmentManager).openFragment(RegistrationFragment.newInstance())
        }
    }

    companion object {
        fun newInstance(): StartFragment {
            val args = Bundle()

            val fragment = StartFragment()
            fragment.arguments = args
            return fragment
        }
    }


}