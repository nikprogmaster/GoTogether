package com.kandyba.gotogether.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.general.*
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartViewModel(
    private val authInteractor: AuthInteractor,
    private val eventsInteractor: EventsInteractor,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private val loginResponseMLD = MutableLiveData<AuthResponse>()
    private val showProgressMLD = MutableLiveData<Boolean>()
    private val saveUserInfoMLD = MutableLiveData<AuthResponse>()
    private val showStartFragmentMLD = MutableLiveData<Unit>()
    private val showHeadpieceMLD = MutableLiveData<Boolean>()
    private val showMainActivityMLD = MutableLiveData<Map<String, EventModel>>()
    private val signupResponseMLD = MutableLiveData<AuthResponse>()
    private var updateUserInfoMLD = MutableLiveData<UserInfoModel>()

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
    val updateUserInfo: LiveData<UserInfoModel>
        get() = updateUserInfoMLD

    fun init(settings: SharedPreferences) {
        val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        Log.i("token: ", token)
        if (token != EMPTY_STRING) {
            showHeadpieceMLD.postValue(true)
            eventsInteractor.getEventsRecommendations(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { eventModel ->
                        showMainActivityMLD.postValue(eventModel)
                        showHeadpieceMLD.postValue(false)
                        Log.i("Нуууу ", "сюда приходим")
                    },
                    {
                        showStartFragmentMLD.postValue(Unit)
                        showHeadpieceMLD.postValue(false)
                        Log.i("А может быть мы здесь?", "А?")
                    }
                ).addTo(rxCompositeDisposable)
        } else {
            Log.i("И здесь нам ", "хорошооо")
            showStartFragmentMLD.postValue(Unit)
        }
    }

    fun updateUserInfo(token: String, uid: String, request: UserRequestBody) {
        showProgressMLD.postValue(true)
        userInteractor.updateUserInfo(token, uid, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    updateUserInfoMLD.postValue(userInfo)
                    showProgressMLD.postValue(false)
                    updateUserInfoMLD = MutableLiveData()
                    Log.i("StartViewModel", "Ну сюда-то я дошель!!!1!!!!!")
                },
                {
                    showProgressMLD.postValue(false)
                    Log.i("StartViewModel", "К сожалению, я здесь")
                }
            ).addTo(rxCompositeDisposable)
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