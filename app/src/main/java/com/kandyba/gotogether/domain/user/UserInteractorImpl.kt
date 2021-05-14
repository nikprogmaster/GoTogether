package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.data.repository.UserRepository
import com.kandyba.gotogether.models.domain.events.Participant
import com.kandyba.gotogether.models.general.Cache
import com.kandyba.gotogether.models.general.requests.UserInfoRequestBody
import com.kandyba.gotogether.models.general.requests.UserInterestsRequestBody
import com.kandyba.gotogether.models.general.requests.UserMainRequestBody
import com.kandyba.gotogether.models.presentation.UserInfoModel
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody

/**
 * Реализация интерактора для пользователей [UserInteractor]
 *
 * @constructor
 * @property repository репозиторий пользователей
 * @property converter конвертер двнных пользователей
 */
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
        updateCache: Boolean,
        anotherUser: Boolean
    ): Single<UserInfoModel> {
        return if (Cache.instance.getCachedUserInfo() == null || updateCache || anotherUser) {
            repository.getUserInfo(token, uid)
                .map { converter.convert(it) }
                .doAfterSuccess { if (!anotherUser) Cache.instance.setUserInfo(it) }
        } else {
            Single.just(Cache.instance.getCachedUserInfo())
        }
    }

    override fun uploadUserAvatar(token: String, filePart: MultipartBody.Part): Completable {
        return repository.uploadUserAvatar(token, filePart)
    }

    override fun getParticipantsRecommendations(
        token: String,
        amount: Int
    ): Single<List<Participant>> {
        return repository.getParticipantsRecommendations(token, amount)
    }
}