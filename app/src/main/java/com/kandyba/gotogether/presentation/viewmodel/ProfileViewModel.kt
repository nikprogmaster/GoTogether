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
 * Вьюмодель для работы с экраном профиля
 *
 * @constructor
 * @property eventsInteractor интерактор для загрузки событий
 * @property userInteractor интерактор для загрузки информации о пользователях
 * @property authInteractor интерактор для выхода из сессии
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

    /**
     * Получить информацию о пользователе
     *
     * @param token токен сессии пользователя
     * @param uid id пользователя
     * @param updateCache обновлять ли кэш
     * @param anotherUser загружаем ли информацию о другом пользователе
     */
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

    /**
     * Выйти из приложения (завершить сессию)
     *
     * @param token токен сессии пользователя
     */
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

    /**
     * Обновить главную информацию пользователя (имя, дата рождения, пол)
     *
     * @param token токен сессии пользователя
     * @param mainRequest новые значения полей
     */
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

    /**
     * Обновить информацию блока "обо мне"
     *
     * @param token токен сессии пользователя
     * @param request новая информация
     */
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

    /**
     * Загрузить аватар пользователя
     *
     * @param token токен сессии пользователя
     * @param filePart изображение, распределенное по частям
     */
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

    /* private fun handleError(e: HttpException): ServerExceptionEntity {
         val errorBody = e.response().errorBody()
         var error = ServerExceptionEntity()
         if (gsonConverter != null && errorBody != null) {
             error = gsonConverter.convert(errorBody) as ServerExceptionEntity
         }
         return error
     }*/
}