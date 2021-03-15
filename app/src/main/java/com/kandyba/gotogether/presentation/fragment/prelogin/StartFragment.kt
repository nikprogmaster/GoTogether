package com.kandyba.gotogether.presentation.fragment.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.kandyba.gotogether.R
import com.kandyba.gotogether.presentation.fragment.FragmentManager


class StartFragment : Fragment() {

    private lateinit var enterButton: Button
    private lateinit var registrationButton: Button
    //private lateinit var mapview: MapView

    private val TAG = "StartFragment: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*MapKitFactory.setApiKey("026f81f1-6fbf-4e34-8296-19948b9e5bb1");
        MapKitFactory.initialize(requireContext());*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.start_fragment, container, false)
        /*mapview = root.findViewById(R.id.mapview) as MapView
        mapview.map.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        mapview.isClickable = false*/

        enterButton = root.findViewById(R.id.start_enter_btn)
        registrationButton = root.findViewById(R.id.registration_btn)
        return root
    }

    /*override fun onStop() {
        super.onStop()
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
    }*/

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