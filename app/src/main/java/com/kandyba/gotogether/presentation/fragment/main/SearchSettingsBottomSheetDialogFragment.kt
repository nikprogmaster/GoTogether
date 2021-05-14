package com.kandyba.gotogether.presentation.fragment.main

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kandyba.gotogether.App
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.presentation.SelectableInterest
import com.kandyba.gotogether.models.presentation.getListOfCategories
import com.kandyba.gotogether.presentation.adapter.SelectableCategoriesAdapter
import com.kandyba.gotogether.presentation.viewmodel.SearchViewModel

/**
 * Нижняя шторка параметров поиска
 */
class SearchSettingsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var applyButton: Button
    private lateinit var clearButton: Button
    private lateinit var closeBottomSheet: ImageView
    private lateinit var categoryFilter: RecyclerView
    private lateinit var distanceSeekbar: SeekBar
    private lateinit var priceSwitcher: SwitchMaterial

    private lateinit var adapter: SelectableCategoriesAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var settings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.search_bottom_sheet, container, false)
        applyButton = root.findViewById(R.id.apply)
        clearButton = root.findViewById(R.id.clear)
        categoryFilter = root.findViewById(R.id.category_filter)
        distanceSeekbar = root.findViewById(R.id.distance_seekbar)
        priceSwitcher = root.findViewById(R.id.price_switch)
        closeBottomSheet = root.findViewById(R.id.close_bottom_sheet)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resolveDependencies()
        initListeners()
    }

    private fun resolveDependencies() {
        val factory =
            (requireActivity().application as App).appComponent.getSearchViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[SearchViewModel::class.java]

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = SelectableCategoriesAdapter(fillInterestsList())
        categoryFilter.layoutManager = manager
        categoryFilter.adapter = adapter

        settings = (requireActivity().application as App).appComponent.getSharedPreferences()
    }

    private fun initListeners() {
        closeBottomSheet.setOnClickListener {
            viewModel.closeBottomSheet()
        }
        applyButton.setOnClickListener {
            if (adapter.getSelectedInterests().isNotEmpty()) {
                viewModel.searchEventsByInterests(
                    settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING,
                    adapter.getSelectedInterests()
                )
                viewModel.closeBottomSheet()
            }
        }
        clearButton.setOnClickListener {
            adapter.unselectAllCategories()
            priceSwitcher.isChecked = false
        }
    }

    private fun fillInterestsList(): List<SelectableInterest> {
        val categoriesList = getListOfCategories()
        val interests = mutableListOf<SelectableInterest>()
        for (category in categoriesList) {
            interests.add(SelectableInterest(category.categoryName, category.serverName))
        }
        return interests.toList()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isFragmentExist = false
    }

    companion object {
        fun newInstance(): SearchSettingsBottomSheetDialogFragment? {
            var fragment: SearchSettingsBottomSheetDialogFragment? = null
            if (!isFragmentExist) {
                fragment = SearchSettingsBottomSheetDialogFragment()
            }
            return fragment
        }

        var isFragmentExist = false
            private set

    }
}