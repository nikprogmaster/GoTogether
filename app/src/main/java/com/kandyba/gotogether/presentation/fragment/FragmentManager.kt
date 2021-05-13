package com.kandyba.gotogether.presentation.fragment

import androidx.fragment.app.Fragment

interface FragmentManager {

    fun openFragment(fragment: Fragment)

    fun closeFragment()

    fun openMainActivity()
}