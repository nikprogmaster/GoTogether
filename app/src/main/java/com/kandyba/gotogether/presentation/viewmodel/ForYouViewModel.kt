package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

class ForYouViewModel(
    private val authInteractor: AuthInteractor,
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val logoutCompletedMLD = MutableLiveData<Unit>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private var eventInfoMLD = MutableLiveData<EventDetailsDomainModel>()
    private val enableLikeButtonMLD = MutableLiveData<String>()
    private val eventLikedMLD = MutableLiveData<String>()
    private val changeToolbarInfoMLD = MutableLiveData<Unit>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val logoutCompleted: LiveData<Unit>
        get() = logoutCompletedMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val eventInfo: LiveData<EventDetailsDomainModel>
        get() = eventInfoMLD
    val enableLikeButton: LiveData<String>
        get() = enableLikeButtonMLD
    val eventLiked: LiveData<String>
        get() = eventLikedMLD
    val changeToolbarInfo: LiveData<Unit>
        get() = changeToolbarInfoMLD

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

    fun loadEventInfo(token: String, eventId: String) {
        showProgressMLD.postValue(true)
        eventsInteractor.getEventInfo(token, eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    eventInfoMLD.postValue(it)
                    eventInfoMLD = MutableLiveData()
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

    fun likeEvent(token: String, requestBody: ParticipationRequestBody, eventId: String) {
        enableLikeButtonMLD.postValue(eventId)
        eventsInteractor.participateInEvent(token, requestBody, eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    enableLikeButtonMLD.postValue(eventId)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    } else {
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                    //eventLikedMLD.postValue(eventId)
                    enableLikeButtonMLD.postValue(eventId)
                }
            ).addTo(rxCompositeDisposable)
    }

    fun makeForYouEventsToolbar() {
        changeToolbarInfoMLD.postValue(Unit)
    }

}