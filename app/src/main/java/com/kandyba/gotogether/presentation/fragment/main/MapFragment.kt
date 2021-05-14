package com.kandyba.gotogether.presentation.fragment.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.EVENT_KEY
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.ui_view.ViewProvider

/**
 * Фрагмент с картой, где указано местоположение мероприятия
 */
class MapFragment : BottomSheetDialogFragment() {

    private lateinit var mapView: MapView
    private lateinit var addressText: TextView
    private lateinit var root: View

    private val searchListener = object : Session.SearchListener {
        override fun onSearchError(p0: Error) {}
        override fun onSearchResponse(p0: Response) {
            val address = p0.collection.children.firstOrNull()?.obj
                ?.metadataContainer
                ?.getItem(ToponymObjectMetadata::class.java)
                ?.address
                ?.formattedAddress
            addressText.text = address
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        MapKitFactory.initialize(requireActivity())
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.map_fragment, container, false)
        mapView = root.findViewById(R.id.map_full)
        addressText = root.findViewById(R.id.address_text)
        return root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isFragmentExist = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val event = requireArguments().get(EVENT_KEY) as EventDetailsDomainModel
        configureMap(event)
    }

    private fun configureMap(event: EventDetailsDomainModel?) {
        if (event?.place?.latitude != null && event.place.longitude != null) {
            val point = Point(event.place.latitude.toDouble(), event.place.longitude.toDouble())
            mapView.map.move(
                CameraPosition(point, ZOOM_FACTOR, AZIMUTH_FACTOR, TILT_FACTOR),
                Animation(Animation.Type.SMOOTH, DURATION),
                null
            )
            val placeMark = View(requireContext()).apply {
                background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.mc_baseline_place_36dp)
            }
            mapView.map.mapObjects.addPlacemark(point, ViewProvider(placeMark))
            SearchFactory.initialize(requireContext())
            val searchManager =
                SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
            searchManager.submit(point, ZOOM_FACTOR.toInt(), SearchOptions(), searchListener)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        if (layoutParams != null) {
            layoutParams.height = getWindowHeight()
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    companion object {
        fun newInstance(event: EventDetailsDomainModel?): MapFragment? {
            var fragment: MapFragment? = null
            if (!isFragmentExist) {
                isFragmentExist = true
                fragment = MapFragment()
                event?.let {
                    val args = Bundle()
                    args.putSerializable(EVENT_KEY, event)
                    fragment.arguments = args
                }
            }
            return fragment
        }

        var isFragmentExist = false
            private set

        private const val ZOOM_FACTOR = 16.0f
        private const val AZIMUTH_FACTOR = 0.0f
        private const val TILT_FACTOR = 0.0f
        private const val DURATION = 1.5f
    }
}