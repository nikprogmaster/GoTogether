package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.models.general.UserRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
interface UserInteractor {

    fun updateUserInfo(
        token: String,
        uid: String,
        requestBody: UserRequestBody
    ): Single<UserInfoModel>

    fun getUserInfo(token: String, uid: String): Single<UserInfoModel>
}