package com.kandyba.gotogether.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kandyba.gotogether.domain.message.MessagesInteractor
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import com.kandyba.gotogether.models.presentation.Message
import com.kandyba.gotogether.models.presentation.SnackbarMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

class DialogsViewModel(
    private val messagesInteractor: MessagesInteractor,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private var showProgressMLD = MutableLiveData<Boolean>()
    private val showSnackbarMLD = MutableLiveData<SnackbarMessage>()
    private val showMessagesProgressMLD = MutableLiveData<Boolean>()
    private var userDialogsMLD = MutableLiveData<List<DialogDomainModel>>()
    private val dialogMessagesMLD = MutableLiveData<List<Message>>()
    private var dialogCreatedMLD = MutableLiveData<DialogResponse>()
    private var companionNameMLD = MutableLiveData<String>()

    private var wereLoadedYet = false

    val showSnackbar: LiveData<SnackbarMessage>
        get() = showSnackbarMLD
    val showProgress: LiveData<Boolean>
        get() = showProgressMLD
    val showMessagesProgress: LiveData<Boolean>
        get() = showMessagesProgressMLD
    val userDialogs: LiveData<List<DialogDomainModel>>
        get() = userDialogsMLD
    val dialogMessages: LiveData<List<Message>>
        get() = dialogMessagesMLD
    val dialogCreated: LiveData<DialogResponse>
        get() = dialogCreatedMLD
    val companionName: LiveData<String>
        get() = companionNameMLD

    fun getUserDialogs(token: String) {
        if (!wereLoadedYet) {
            showProgressMLD.postValue(true)
            Log.i("DialogsViewModel", "true")
            wereLoadedYet = true
        }
        messagesInteractor.getUserDialogs(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dialogs ->
                    showProgressMLD.postValue(false)
                    userDialogsMLD.postValue(dialogs)
                    //userDialogsMLD = MutableLiveData()
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                })
            .addTo(rxCompositeDisposable)
    }

    fun getDialogMessages(token: String, dialogId: String, userId: String) {
        showMessagesProgressMLD.postValue(true)
        messagesInteractor.getDialogMessages(token, dialogId, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dialogs ->
                    showMessagesProgressMLD.postValue(false)
                    dialogMessagesMLD.postValue(dialogs)
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showMessagesProgressMLD.postValue(false)
                })
            .addTo(rxCompositeDisposable)
    }


    fun createDialog(token: String, companionId: String) {
        showProgressMLD.postValue(true)
        messagesInteractor.createDialog(token, companionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    showProgressMLD.postValue(false)
                    dialogCreatedMLD.postValue(response)
                    dialogCreatedMLD = MutableLiveData()
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                    showProgressMLD.postValue(false)
                })
            .addTo(rxCompositeDisposable)
    }

    fun getCompanionName(token: String, userId: String) {
        userInteractor.getUserInfo(token, userId, false, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userInfo ->
                    companionNameMLD.postValue(userInfo.firstName)
                    companionNameMLD = MutableLiveData()
                },
                {
                    if (it is ConnectException) {
                        showSnackbarMLD.postValue(SnackbarMessage.NO_INTERNET_CONNECTION)
                    }
                }
            ).addTo(rxCompositeDisposable)
    }

    fun startMessaging(token: String, listener: Socket.UniversalListener) {
        messagesInteractor.startMessaging(token, listener)
    }

    fun sendMessage(token: String, socketMessage: SocketMessage) {
        val sended = messagesInteractor.sendMessage(token, socketMessage)
    }

    fun getSocketState() = messagesInteractor.getState()
}