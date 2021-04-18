package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.data.repository.UserRepository
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.UserInfoRequestBody
import com.kandyba.gotogether.models.general.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Single

class UserInteractorImpl(
    val repository: UserRepository,
    val converter: UserDomainConverter
) : UserInteractor {

    override fun updateMainUserInfo(
        token: String,
        bodyMain: UserMainRequestBody
    ): Single<UserInfoModel> {
        return repository.updateMainUserInfo(token, bodyMain).map { converter.convert(it) }
    }

    override fun updateUserInfo(
        token: String,
        body: UserInfoRequestBody
    ): Single<UserInfoModel> {
        return repository.updateUserInfo(token, body).map { converter.convert(it) }
    }

    override fun updateUserInterests(
        token: String,
        body: UserInterestsRequestBody
    ): Single<UserInfoModel> {
        return repository.updateUserInterests(token, body).map { converter.convert(it) }
    }

    override fun getUserInfo(
        token: String,
        uid: String,
        updateCache: Boolean
    ): Single<UserInfoModel> {
        return if (Cache.instance.getCachedUserInfo() == null || updateCache) {
            repository.getUserInfo(token, uid)
                .map { converter.convert(it) }
                .doAfterSuccess { Cache.instance.setUserInfo(it) }
        } else {
            Single.just(Cache.instance.getCachedUserInfo())
        }
    }
}