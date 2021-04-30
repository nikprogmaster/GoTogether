package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    override fun uploadUserAvatar(token: String, filePart: MultipartBody.Part): Completable {
        val type = RequestBody.create(MultipartBody.FORM, "image/jpeg")
        return apiMapper.uploadUserAvatar(token, filePart)
    }
}
