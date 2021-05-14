package com.kandyba.gotogether.data.converter.users


import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

/**
 * Конвертер информации о пользователе из data слоя в domain
 *
 * @constructor
 * @property eventsConverter конвертер событий
 */
class UserDataConverter(
    private val eventsConverter: EventDetailsDataConverter
) : BaseConverter<UserInfoDataModel, UserInfoDomainModel>() {

    override fun convert(from: UserInfoDataModel): UserInfoDomainModel {
        return UserInfoDomainModel(
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
            from.likedEvents.map { eventsConverter.convertWithId(it, it.id) },
            from.currentUserInBlackList,
            from.inCurrentUserBlackList
        )
    }
}