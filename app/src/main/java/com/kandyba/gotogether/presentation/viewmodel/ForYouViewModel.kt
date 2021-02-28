package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

class ForYouViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val logoutCompletedMLD = MutableLiveData<Unit>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val logoutCompleted: LiveData<Unit>
        get() = logoutCompletedMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD

    fun logout(token: String) {
        showProgressMLD.postValue(true)
        authInteractor.logout(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logoutCompletedMLD.postValue(Unit)
                    showProgressMLD.postValue(false)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                }
            ).addTo(rxCompositeDisposable)
    }
}