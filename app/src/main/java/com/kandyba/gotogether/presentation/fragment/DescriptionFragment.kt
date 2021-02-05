package com.kandyba.gotogether.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kandyba.gotogether.R

class DescriptionFragment : Fragment() {

    private lateinit var continueButton: Button
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.description_fragment, container, false)
        backButton = root.findViewById(R.id.back_btn)
        continueButton = root.findViewById(R.id.continue_btn)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        backButton.setOnClickListener { (activity as FragmentManager).closeFragment() }
        continueButton.setOnClickListener {
            (activity as FragmentManager).openFragment(InterestsFragment.newInstance())
        }
    }

    companion object {
        fun newInstance(): DescriptionFragment {
            val args = Bundle()

            val fragment = DescriptionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}