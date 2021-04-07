package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.UserInfoRequestBody
import com.kandyba.gotogether.models.general.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.UserMainRequestBody
import io.reactivex.Single

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
}