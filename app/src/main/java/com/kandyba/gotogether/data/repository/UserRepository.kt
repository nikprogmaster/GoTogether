package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
interface UserRepository {

    fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel>

    fun updateMainUserInfo(
        token: String,
        bodyMain: UserMainRequestBody
    ): Single<UserInfoDomainModel>

    fun updateUserInfo(token: String, body: UserInfoRequestBody): Single<UserInfoDomainModel>

    fun updateUserInterests(
        token: String,
        body: UserInterestsRequestBody
    ): Single<UserInfoDomainModel>

    fun uploadUserAvatar(token: String, filePart: MultipartBody.Part): Completable

    fun getParticipantsRecommendations(
        token: String,
        amount: Int
    ): Single<List<com.kandyba.gotogether.models.domain.events.Participant>>
}


