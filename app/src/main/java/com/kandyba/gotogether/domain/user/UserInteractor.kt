package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody


interface UserInteractor {

    fun updateMainUserInfo(token: String, bodyMain: UserMainRequestBody): Single<UserInfoModel>

    fun updateUserInfo(token: String, body: UserInfoRequestBody): Single<UserInfoModel>

    fun updateUserInterests(token: String, body: UserInterestsRequestBody): Single<UserInfoModel>

    fun getUserInfo(
        token: String,
        uid: String,
        updateCache: Boolean,
        anotherUser: Boolean
    ): Single<UserInfoModel>

    fun uploadUserAvatar(token: String, filePart: MultipartBody.Part): Completable

    fun getParticipantsRecommendations(
        token: String,
        amount: Int
    ): Single<List<com.kandyba.gotogether.models.domain.events.Participant>>
}