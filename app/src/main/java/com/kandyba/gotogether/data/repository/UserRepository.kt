package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.UserRequestBody
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
interface UserRepository {

    fun updateUserInfo(
        token: String,
        uid: String,
        requestBody: UserRequestBody
    ): Single<UserInfoDomainModel>

    fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel>
}