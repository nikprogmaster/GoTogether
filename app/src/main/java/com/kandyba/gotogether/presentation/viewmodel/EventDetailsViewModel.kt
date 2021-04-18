package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException


class EventDetailsViewModel(
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val enableLikeButtonMLD = MutableLiveData<Boolean>()
    private val changeEventLikePropertyMLD = MutableLiveData<String>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val enableLikeButton: LiveData<Boolean>
        get() = enableLikeButtonMLD
    val changeEventLikeProperty: LiveData<String>
        get() = changeEventLikePropertyMLD

    fun complain(token: String, eventId: String, request: EventComplaintRequestBody) {
        showProgressMLD.postValue(true)
        eventsInteractor.complainEvent(token, eventId, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    showSnackbarMLD.postValue(SnackbarMessage.COMPLAIN_SENDED)
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

    fun likeEvent(token: String, eventId: String) {
        enableLikeButtonMLD.postValue(false)
        eventsInteractor.participateInEvent(token, eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    enableLikeButtonMLD.postValue(true)
                    changeEventLikePropertyMLD.postValue(eventId)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    } else {
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                    //eventLikedMLD.postValue(eventId)
                    enableLikeButtonMLD.postValue(true)
                }
            ).addTo(rxCompositeDisposable)
    }


}