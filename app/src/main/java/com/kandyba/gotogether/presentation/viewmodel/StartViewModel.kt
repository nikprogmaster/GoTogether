package com.kandyba.gotogether.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.models.general.EMPTY_STRING
import com.kandyba.gotogether.models.general.LoginRequestBody
import com.kandyba.gotogether.models.general.SignupRequestBody
import com.kandyba.gotogether.models.general.TOKEN
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.EventModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartViewModel(
    private val authInteractor: AuthInteractor,
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val loginResponseMLD = MutableLiveData<AuthResponse>()
    private val showProgressMLD = MutableLiveData<Boolean>()
    private val saveUserInfoMLD = MutableLiveData<AuthResponse>()
    private val showStartFragmentMLD = MutableLiveData<Unit>()
    private val showHeadpieceMLD = MutableLiveData<Boolean>()
    private val showMainActivityMLD = MutableLiveData<Map<String, EventModel>>()
    private val signupResponseMLD = MutableLiveData<AuthResponse>()

    val loginResponse: LiveData<AuthResponse>
        get() = loginResponseMLD
    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val saveUserInfo: LiveData<AuthResponse>
        get() = saveUserInfoMLD
    val showStartFragment: LiveData<Unit>
        get() = showStartFragmentMLD
    val showHeadpiece: LiveData<Boolean>
        get() = showHeadpieceMLD
    val showMainActivity: LiveData<Map<String, EventModel>>
        get() = showMainActivityMLD
    val signupResponse: LiveData<AuthResponse>
        get() = signupResponseMLD

    fun init(settings: SharedPreferences) {
        val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        if (token != EMPTY_STRING) {
            showHeadpieceMLD.postValue(true)
            eventsInteractor.getEventsRecommendations(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { showHeadpieceMLD.postValue(false) }
                .subscribe(
                    { eventModel ->
                        showMainActivityMLD.postValue(eventModel)
                    },
                    {
                        showStartFragmentMLD.postValue(Unit)
                    }
                ).addTo(rxCompositeDisposable)
        } else {
            showStartFragmentMLD.postValue(Unit)
        }
    }

    fun signup(signupRequest: SignupRequestBody) {
        showProgressMLD.postValue(true)
        authInteractor.signup(signupRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    signupResponseMLD.postValue(response)
                    saveUserInfoMLD.postValue(response)
                    showProgressMLD.postValue(false)
                },
                {
                    showProgressMLD.postValue(false)
                }
            ).addTo(rxCompositeDisposable)
    }

    fun login(email: String, password: String) {
        showProgressMLD.postValue(true)
        val credential = LoginRequestBody(email, password)
        authInteractor.login(credential)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    Log.i("RESPONSE", response.toString())
                    loginResponseMLD.postValue(response)
                    saveUserInfoMLD.postValue(response)
                },
                {
                    //show error dialog
                }
            ).addTo(rxCompositeDisposable)
    }

}