package com.kandyba.gotogether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

/**
 * @author Кандыба Никита
 * @since 16.04.2021
 */
class ProfileViewModel(
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private val userInfoMLD = MutableLiveData<UserInfoModel>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()

    val userInfo: LiveData<UserInfoModel>
        get() = userInfoMLD
    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD

    fun loadUserInfo(token: String, userId: String, updateCache: Boolean) {
        userInteractor.getUserInfo(token, userId, updateCache)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    userInfoMLD.postValue(userInfo)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }
}