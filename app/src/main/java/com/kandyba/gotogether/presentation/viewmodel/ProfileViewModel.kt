package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.domain.events.EventDetailsDomainModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.net.ConnectException

/**
 * @author Кандыба Никита
 * @since 16.04.2021
 */
class ProfileViewModel(
    private val userInteractor: UserInteractor,
    private val authInteractor: AuthInteractor,
    private val eventsInteractor: EventsInteractor
) : BaseViewModel() {

    private val logoutCompletedMLD = MutableLiveData<Unit>()
    private val showProgressMLD = MutableLiveData<Boolean>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private var userInfoMLD = MutableLiveData<UserInfoModel>()
    private val updateMainUserInfoMLD = MutableLiveData<UserInfoModel>()
    private val updateAdditionalUserInfoMLD = MutableLiveData<UserInfoModel>()
    private val userImageUploadedMLD = MutableLiveData<Unit>()
    private var eventInfoMLD = MutableLiveData<EventDetailsDomainModel>()
    private val enableLikeButtonMLD = MutableLiveData<String>()
    private val eventNotLikedMLD = MutableLiveData<String>()

    val userInfo: LiveData<UserInfoModel>
        get() = userInfoMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val logoutCompleted: LiveData<Unit>
        get() = logoutCompletedMLD
    val updateMainUserInfo: LiveData<UserInfoModel>
        get() = updateMainUserInfoMLD
    val updateAdditionalUserInfo: LiveData<UserInfoModel>
        get() = updateAdditionalUserInfoMLD
    val userImageUploaded: LiveData<Unit>
        get() = userImageUploadedMLD
    val eventInfo: LiveData<EventDetailsDomainModel>
        get() = eventInfoMLD
    val enableLikeButton: LiveData<String>
        get() = enableLikeButtonMLD
    val eventNotLiked: LiveData<String>
        get() = eventNotLikedMLD

    fun loadUserInfo(token: String, userId: String, updateCache: Boolean, anotherUser: Boolean) {
        userInteractor.getUserInfo(token, userId, updateCache, anotherUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    userInfoMLD.postValue(userInfo)
                    userInfoMLD = MutableLiveData()
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

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

    fun updateMainUserInfo(token: String, mainRequest: UserMainRequestBody) {
        showProgressMLD.postValue(true)
        userInteractor.updateMainUserInfo(token, mainRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    updateMainUserInfoMLD.postValue(userInfo)
                    showProgressMLD.postValue(false)
                },
                {
                    if (it is HttpException) {
                        //val error = handleError(it)
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                }
            ).addTo(rxCompositeDisposable)
    }

    fun updateAdditionalUserInfo(token: String, request: UserInfoRequestBody) {
        showProgressMLD.postValue(true)
        userInteractor.updateUserInfo(token, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    updateAdditionalUserInfoMLD.postValue(userInfo)
                    showProgressMLD.postValue(false)
                },
                {
                    if (it is HttpException) {
                        //val error = handleError(it)
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                }
            ).addTo(rxCompositeDisposable)
    }

    fun uploadUserAvatar(token: String, filePart: MultipartBody.Part) {
        userInteractor.uploadUserAvatar(token, filePart)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    userImageUploadedMLD.postValue(Unit)
                },
                {
                    if (it is HttpException) {
                        //val error = handleError(it)
                        showSnackbarMLD.postValue(SnackbarMessage.COMMON_MESSAGE)
                    }
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

    /* private fun handleError(e: HttpException): ServerExceptionEntity {
         val errorBody = e.response().errorBody()
         var error = ServerExceptionEntity()
         if (gsonConverter != null && errorBody != null) {
             error = gsonConverter.convert(errorBody) as ServerExceptionEntity
         }
         return error
     }*/
}