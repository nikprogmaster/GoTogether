package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.UserRequestBody
import io.reactivex.Single

class UserRepositoryImpl(
    private val apiMapper: UserApiMapper,
    private val converter: UserDataConverter
) : UserRepository {

    override fun updateUserInfo(
        token: String,
        requestBody: UserRequestBody
    ): Single<UserInfoDomainModel> {
        return apiMapper.updateUserInfo(token, requestBody)
            .map { converter.convert(it) }
    }

    override fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel> {
        return apiMapper.getUserInfo(token, uid).map { converter.convert(it) }
    }
}