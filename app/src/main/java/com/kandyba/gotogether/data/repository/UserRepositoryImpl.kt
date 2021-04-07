package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.UserInfoRequestBody
import com.kandyba.gotogether.models.general.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.UserMainRequestBody
import io.reactivex.Single

class UserRepositoryImpl(
    private val apiMapper: UserApiMapper,
    private val converter: UserDataConverter
) : UserRepository {

    override fun updateUserInfo(
        token: String,
        body: UserInfoRequestBody
    ): Single<UserInfoDomainModel> {
        return apiMapper.updateUserInfo(token, body).map { converter.convert(it) }
    }

    override fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel> {
        return apiMapper.getUserInfo(token, uid).map { converter.convert(it) }
    }

    override fun updateMainUserInfo(
        token: String,
        bodyMain: UserMainRequestBody
    ): Single<UserInfoDomainModel> {
        return apiMapper.updateMainUserInfo(token, bodyMain).map { converter.convert(it) }
    }

    override fun updateUserInterests(
        token: String,
        body: UserInterestsRequestBody
    ): Single<UserInfoDomainModel> {
        return apiMapper.updateUserInterests(token, body).map { converter.convert(it) }
    }
}