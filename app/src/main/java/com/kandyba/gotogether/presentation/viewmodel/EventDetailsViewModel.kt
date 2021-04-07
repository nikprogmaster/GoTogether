package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.general.EventComplaintRequestBody
import com.kandyba.gotogether.models.general.ParticipationRequestBody
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

/**
 * @author Кандыба Никита
 * @since 26.03.2021
 */
class EventDetailsViewModel(
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val showToolBarMLD = MutableLiveData<Boolean>()
    private val changeToolbarInfoMLD = MutableLiveData<Unit>()
    private val enableLikeButtonMLD = MutableLiveData<Boolean>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val showToolBar: LiveData<Boolean>
        get() = showToolBarMLD
    val changeToolbarInfo: LiveData<Unit>
        get() = changeToolbarInfoMLD
    val enableLikeButton: LiveData<Boolean>
        get() = enableLikeButtonMLD

    fun complain(token: String, request: EventComplaintRequestBody) {
        eventsInteractor.eventComplain(token, request)
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

    fun likeEvent(token: String, requestBody: ParticipationRequestBody, eventId: String) {
        enableLikeButtonMLD.postValue(false)
        eventsInteractor.participateInEvent(token, requestBody, eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    enableLikeButtonMLD.postValue(true)
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

    fun showToolbar(show: Boolean) {
        showToolBarMLD.postValue(show)
    }

    fun makeParticipantsToolbar() {
        changeToolbarInfoMLD.postValue(Unit)
    }

}