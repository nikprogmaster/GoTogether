package com.kandyba.gotogether.presentation.fragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.kandyba.gotogether.models.presentation.Events

interface FragmentManager {

    fun openFragment(fragment: Fragment)

    fun closeFragment()

    fun openMainActivity(events: Events)

    fun showDialogFragment(dialog: DialogFragment)


}