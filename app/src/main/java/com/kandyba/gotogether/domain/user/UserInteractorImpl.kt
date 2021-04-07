package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.data.repository.UserRepository
import com.kandyba.gotogether.models.general.UserRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Single

class UserInteractorImpl(
    val repository: UserRepository,
    val converter: UserDomainConverter
) : UserInteractor {

    override fun updateUserInfo(
        token: String,
        requestBody: UserRequestBody
    ): Single<UserInfoModel> {
        return repository.updateUserInfo(token, requestBody).map { converter.convert(it) }
    }

    override fun getUserInfo(token: String, uid: String): Single<UserInfoModel> {
        return repository.getUserInfo(token, uid).map { converter.convert(it) }
    }
}