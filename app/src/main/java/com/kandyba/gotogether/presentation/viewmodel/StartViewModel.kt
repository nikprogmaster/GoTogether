package com.kandyba.gotogether.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.general.*
import com.kandyba.gotogether.models.presentation.AuthResponse
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Converter
import java.net.ConnectException


class StartViewModel(
    private val authInteractor: AuthInteractor,
    private val eventsInteractor: EventsInteractor,
    private val userInteractor: UserInteractor,
    private val gsonConverter: Converter<ResponseBody, *>?
) : BaseViewModel() {

    private val loginResponseMLD = MutableLiveData<AuthResponse>()
    private val showProgressMLD = MutableLiveData<Boolean>()
    private val saveUserInfoMLD = MutableLiveData<AuthResponse>()
    private val showStartFragmentMLD = MutableLiveData<Unit>()
    private val showHeadpieceMLD = MutableLiveData<Boolean>()
    private val showMainActivityMLD = MutableLiveData<List<EventModel>>()
    private val signupResponseMLD = MutableLiveData<AuthResponse>()
    private var updateUserInfoMLD = MutableLiveData<UserInfoModel>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()

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
    val showMainActivity: LiveData<List<EventModel>>
        get() = showMainActivityMLD
    val signupResponse: LiveData<AuthResponse>
        get() = signupResponseMLD
    val updateUserInfo: LiveData<UserInfoModel>
        get() = updateUserInfoMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD

    fun init(settings: SharedPreferences) {
        val token = settings.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        if (token != EMPTY_STRING) {
            showHeadpieceMLD.postValue(true)
            eventsInteractor.getEventsRecommendations(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { eventModel ->
                        showMainActivityMLD.postValue(eventModel)
                        showHeadpieceMLD.postValue(false)
                    },
                    {
                        showStartFragmentMLD.postValue(Unit)
                        showHeadpieceMLD.postValue(false)
                        if (it is ConnectException) {
                            showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                        }
                    }
                ).addTo(rxCompositeDisposable)
        } else {
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
                },
                {
                    if (it is HttpException) {
                        val error = handleError(it)
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
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
                    if (it is HttpException) {
                        val error = handleError(it)
                        showSnackbarMLD.postValue(SnackbarMessage.USER_ALREADY_EXISTS)
                    }
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
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
                    loginResponseMLD.postValue(response)
                    saveUserInfoMLD.postValue(response)
                },
                {
                    if (it is HttpException) {
                        showSnackbarMLD.postValue(SnackbarMessage.INCORRECT_PASSWORD)
                    }
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                }
            ).addTo(rxCompositeDisposable)
    }

    private fun handleError(e: HttpException): ServerExceptionEntity {
        val errorBody = e.response().errorBody()
        var error = ServerExceptionEntity()
        if (gsonConverter != null && errorBody != null) {
            error = gsonConverter.convert(errorBody) as ServerExceptionEntity
        }
        return error
    }
}