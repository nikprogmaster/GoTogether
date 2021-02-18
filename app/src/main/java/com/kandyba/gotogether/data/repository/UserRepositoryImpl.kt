package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.auth.UserDataConverter
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.UserRequestBody
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
class UserRepositoryImpl(
    private val apiMapper: UserApiMapper,
    private val converter: UserDataConverter
) : UserRepository {

    override fun updateUserInfo(
        token: String,
        uid: String,
        requestBody: UserRequestBody
    ): Single<UserInfoDomainModel> {
        val tokenResult = TOKEN_PREFIX + token
        return apiMapper.updateUserInfo(tokenResult, uid, requestBody.fields)
            .map { converter.convert(it) }
    }

    override fun getUserInfo(token: String, uid: String): Single<UserInfoDomainModel> {
        return apiMapper.updateUserInfo(token, uid).map { converter.convert(it) }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}