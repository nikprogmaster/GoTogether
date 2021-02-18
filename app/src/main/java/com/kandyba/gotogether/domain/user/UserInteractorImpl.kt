package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.data.repository.UserRepository
import com.kandyba.gotogether.models.general.UserRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
class UserInteractorImpl(
    val repository: UserRepository,
    val converter: UserDomainConverter
) : UserInteractor {

    override fun updateUserInfo(
        token: String,
        uid: String,
        requestBody: UserRequestBody
    ): Single<UserInfoModel> {
        return repository.updateUserInfo(token, uid, requestBody).map { converter.convert(it) }
    }

    override fun getUserInfo(token: String, uid: String): Single<UserInfoModel> {
        return repository.getUserInfo(token, uid).map { converter.convert(it) }
    }
}