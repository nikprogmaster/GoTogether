package com.kandyba.gotogether.presentation.viewmodel

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Вьюмодель для всех экранов главной активити
 */
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

    /**
     * Скрыть/показать toolbar
     *
     * @param show показывать или нет
     */
    fun showToolbar(show: Boolean) {
        showToolbarMLD.postValue(show)
    }

    /**
     * Установить заголовок toolbar`у
     *
     * @param message название
     */
    fun setAppbarTitle(message: String) {
        appbarTitleMLD.postValue(message)
    }

    /**
     * Сделать toolbar специального вида для экрана персональных рекомендаций
     */
    fun makeForYouEventsToolbar() {
        makeForYouEventsToolbarMLD.postValue(Unit)
    }

    /**
     * Сделать toolbar специального вида для экрана участников мероприятия
     */
    fun makeParticipantsToolbar() {
        makeParticipantsToolbarMLD.postValue(Unit)
    }

    /**
     * Закрыть фрагмент
     */
    fun closeFragment() {
        closeFragmentMLD.postValue(Unit)
    }

    /**
     * Отобразить диалог
     *
     * @param dialog инстанс диалог-фрагмента
     */
    fun showDialogFragment(dialog: DialogFragment) {
        openDialogFragmentMLD.postValue(dialog)
    }

    /**
     * Открыть фрагмент
     *
     * @param fragment инстанс фрагмента
     */
    fun openFragment(fragment: Fragment) {
        openFragmentMLD.postValue(fragment)
    }
}