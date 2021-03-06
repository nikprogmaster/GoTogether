package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.domain.events.EventsDomainConverter
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.UserInfoModel

/**
 * Конвертер данных пользователей из domain-слоя в presentation
 *
 * @constructor
 * @property eventsDomainConverter конвертер событий
 */
class UserDomainConverter(
    private val eventsDomainConverter: EventsDomainConverter
) : BaseConverter<UserInfoDomainModel, UserInfoModel>() {

    override fun convert(from: UserInfoDomainModel): UserInfoModel {
        return UserInfoModel(
            from.id,
            from.email,
            from.firstName,
            from.phone,
            from.birthDate,
            from.sex,
            from.longitude,
            from.latitude,
            from.info,
            from.isLoyal,
            from.avatar,
            from.interests,
            eventsDomainConverter.convert(from.likedEvents),
            from.currentUserInBlackList,
            from.inCurrentUserBlackList
        )
    }

}