package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

/**
 * Вьюмодель для поиска мероприятий
 *
 * @constructor
 * @property eventsInteractor интерактор для загрузки информации о событиях
 */
class SearchViewModel(
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private var eventInfoMLD = MutableLiveData<EventDetailsDomainModel>()
    private val showProgressMLD = MutableLiveData<Boolean>()
    private val eventsRecommendationsMLD = MutableLiveData<List<EventModel>>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val closeBottomSheetMLD = MutableLiveData<Unit>()
    private val enableLikeButtonMLD = MutableLiveData<String>()
    private val eventNotLikedMLD = MutableLiveData<String>()
    private val searchResultEventsMLD = MutableLiveData<List<EventModel>>()
    private val searchValueMLD = MutableLiveData<String>()

    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val eventsRecommendations: LiveData<List<EventModel>>
        get() = eventsRecommendationsMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val closeBottomSheet: LiveData<Unit>
        get() = closeBottomSheetMLD
    val searchResultEvents: LiveData<List<EventModel>>
        get() = searchResultEventsMLD
    val eventInfo: LiveData<EventDetailsDomainModel>
        get() = eventInfoMLD
    val enableLikeButton: LiveData<String>
        get() = enableLikeButtonMLD
    val eventNotLiked: LiveData<String>
        get() = eventNotLikedMLD
    val searchValue: LiveData<String>
        get() = searchValueMLD

    /**
     * Получить информацию о событии
     *
     * @param token токен сессии пользователя
     * @param eventId id события
     */
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

    /**
     * Поучаствовать/ отменить участие в событии
     *
     * @param token токен сессии пользователя
     * @param eventId id мероприятия
     */
    fun likeEvent(token: String, eventId: String) {
        enableLikeButtonMLD.postValue(eventId)
        eventsInteractor.participateInEvent(token, eventId)
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
                    eventNotLikedMLD.postValue(eventId)
                    enableLikeButtonMLD.postValue(eventId)
                }
            ).addTo(rxCompositeDisposable)
    }

    /**
     * Свернуть нижнюю шторку
     */
    fun closeBottomSheet() {
        closeBottomSheetMLD.postValue(Unit)
    }

    /**
     * Поиск мероприятий по категорям
     *
     * @param token токен сессии пользователя
     * @param interests категории
     */
    fun searchEventsByInterests(token: String, interests: List<String>) {
        showProgressMLD.postValue(true)
        eventsInteractor.searchEventsByInterests(token, interests)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { eventModel ->
                    searchResultEventsMLD.postValue(eventModel)
                    showProgressMLD.postValue(false)
                },
                {
                    showProgressMLD.postValue(false)
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

    /**
     * Поиск мероприятий по тексту
     *
     * @param token токен сессии пользователя
     * @param text текст запроса
     */
    fun searchEventsByText(token: String, text: String) {
        showProgressMLD.postValue(true)
        eventsInteractor.searchEventsByTextQuery(token, text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { eventModel ->
                    searchResultEventsMLD.postValue(eventModel)
                    showProgressMLD.postValue(false)
                },
                {
                    showProgressMLD.postValue(false)
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

    /**
     * Установить последний выпоненный запрос
     *
     * @param value запрос
     */
    fun setSearchValue(value: String) {
        searchValueMLD.postValue(value)
    }
}