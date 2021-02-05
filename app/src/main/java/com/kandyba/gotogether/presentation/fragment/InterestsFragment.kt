package com.kandyba.gotogether.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.Interest
import com.kandyba.gotogether.presentation.adapter.InterestsAdapter

class InterestsFragment : Fragment() {

    private lateinit var interestAdapter: InterestsAdapter
    private lateinit var interestList: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var continueButton: Button

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
        interestAdapter = InterestsAdapter(fullInterestsList())
        interestList.adapter = interestAdapter
        setListeners()
    }

    private fun setListeners() {
        backButton.setOnClickListener { (requireActivity() as FragmentManager).closeFragment() }
        //continueButton.setOnClickListener { (requireActivity() as FragmentManager).openMainActivity() }
    }

    private fun fullInterestsList(): MutableList<Interest> {
        val array = requireContext().resources.getStringArray(R.array.interests)
        return array.map { Interest(it) }.toMutableList()
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