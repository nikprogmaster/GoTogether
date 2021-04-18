package com.kandyba.gotogether.presentation.viewmodel

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel : BaseViewModel() {

    private val showToolbarMLD = MutableLiveData<Boolean>()
    private val appbarTitleMLD = MutableLiveData<String>()
    private val makeForYouEventsToolbarMLD = MutableLiveData<Unit>()
    private val makeParticipantsToolbarMLD = MutableLiveData<Unit>()
    private val closeFragmentMLD = MutableLiveData<Unit>()
    private val openFragmentMLD = MutableLiveData<Fragment>()
    private val openDialogFragmentMLD = MutableLiveData<DialogFragment>()

    val showToolbar: LiveData<Boolean>
        get() = showToolbarMLD
    val appbarTitle: LiveData<String>
        get() = appbarTitleMLD
    val makeForYouToolbar: LiveData<Unit>
        get() = makeForYouEventsToolbarMLD
    val makeParticipantsToolbar: LiveData<Unit>
        get() = makeParticipantsToolbarMLD
    val closeFragment: LiveData<Unit>
        get() = closeFragmentMLD
    val openFragment: LiveData<Fragment>
        get() = openFragmentMLD
    val openDialogFragment: LiveData<DialogFragment>
        get() = openDialogFragmentMLD

    fun showToolbar(show: Boolean) {
        showToolbarMLD.postValue(show)
    }

    fun setAppbarTitle(message: String) {
        appbarTitleMLD.postValue(message)
    }

    fun makeForYouEventsToolbar() {
        makeForYouEventsToolbarMLD.postValue(Unit)
    }

    fun makeParticipantsToolbar() {
        makeParticipantsToolbarMLD.postValue(Unit)
    }

    fun closeFragment() {
        closeFragmentMLD.postValue(Unit)
    }

    fun showDialogFragment(dialog: DialogFragment) {
        openDialogFragmentMLD.postValue(dialog)
    }

    fun openFragment(fragment: Fragment) {
        openFragmentMLD.postValue(fragment)
    }
}