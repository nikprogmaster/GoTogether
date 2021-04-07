package com.kandyba.gotogether.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException


class SearchViewModel(
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val showProgressMLD = MutableLiveData<Boolean>()
    private val eventsRecommendationsMLD = MutableLiveData<List<EventModel>>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val closeBottomSheetMLD = MutableLiveData<Unit>()
    private val searchResultEventsMLD = MutableLiveData<List<EventModel>>()

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

    fun getEventsRecommendation(token: String) {
        showProgressMLD.postValue(true)
        eventsInteractor.getEventsRecommendations(token, DEFAULT_EVENTS_AMOUNT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { eventModel ->
                    eventsRecommendationsMLD.postValue(eventModel)
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

    fun closeBottomSheet() {
        closeBottomSheetMLD.postValue(Unit)
    }

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

    fun searchEventsByText(token: String, text: String) {
        showProgressMLD.postValue(true)
        eventsInteractor.searchEventsByTextQuery(token, text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { eventModel ->
                    searchResultEventsMLD.postValue(eventModel)
                    showProgressMLD.postValue(false)
                    Log.i("Kandyba", "Всё хорошо")
                },
                {
                    Log.i("Kandyba", "Дела так себе")
                    showProgressMLD.postValue(false)
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

    companion object {
        private const val DEFAULT_EVENTS_AMOUNT = 50
    }
}