package com.kandyba.gotogether.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.R
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.domain.events.Participant
import com.kandyba.gotogether.models.general.requests.EventComplaintRequestBody
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

/**
 * Вьюмодель для работы с экраном информации о событии
 *
 * @constructor
 * @property eventsInteractor интерактор для загрузки событий
 * @property userInteractor интерактор для загрузки информации о пользователях
 */
class EventDetailsViewModel(
    private val eventsInteractor: EventsInteractor,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val enableLikeButtonMLD = MutableLiveData<Boolean>()
    private val changeEventLikePropertyMLD = MutableLiveData<String>()
    private val recommendedParticipantsMLD = MutableLiveData<List<Participant>>()
    private var eventImagesMLD = MutableLiveData<List<Bitmap>>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val enableLikeButton: LiveData<Boolean>
        get() = enableLikeButtonMLD
    val changeEventLikeProperty: LiveData<String>
        get() = changeEventLikePropertyMLD
    val recommendedParticipants: LiveData<List<Participant>>
        get() = recommendedParticipantsMLD
    val eventImages: LiveData<List<Bitmap>>
        get() = eventImagesMLD

    /**
     * Пожаловаться на событие
     *
     * @param token токен сессии пользователя
     * @param eventId id события
     * @param request текст жалобы
     */
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

    /**
     * Поучаствовать/ отменить участие в событии
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     */
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
                    enableLikeButtonMLD.postValue(true)
                }
            ).addTo(rxCompositeDisposable)
    }

    /**
     * Получить рекомендации участников мероприятия
     *
     * @param token токен сессии пользователя
     */
    fun getParticipantsRecommendations(token: String) {
        userInteractor.getParticipantsRecommendations(token, DEFAULT_PARTICIPANTS_AMOUNT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    recommendedParticipantsMLD.postValue(it)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    } else {
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

    /**
     * Загрузить список изображений с заданного URL
     *
     * @param images список url-ов изображений
     */
    fun loadImages(images: List<String>) {
        eventsInteractor.loadEventImages(
            images,
            R.drawable.ill_placeholder_300dp,
            R.drawable.ill_error_placeholder_300dp
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    eventImagesMLD.postValue(it)
                    eventImagesMLD = MutableLiveData()
                },
                {
                }
            ).addTo(rxCompositeDisposable)
    }

    companion object {
        private const val DEFAULT_PARTICIPANTS_AMOUNT = 10
    }

}