package com.kandyba.gotogether.presentation.fragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

interface FragmentManager {

    fun openFragment(fragment: Fragment)

    fun closeFragment()

    fun openMainActivity()

    fun showDialogFragment(dialog: DialogFragment)


}