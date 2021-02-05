package com.kandyba.gotogether.data.converter.auth

import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.domain.events.Date
import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class UserDataConverter: BaseConverter<UserInfoDataModel, UserInfoDomainModel>() {

    override fun convert(from: UserInfoDataModel): UserInfoDomainModel {
        return UserInfoDomainModel(
            from.id,
            from.email,
            from.firstName,
            from.birthDate,
            from.sex,
            from.age,
            from.longitude,
            from.latitude,
            from.createdAt,
            from.updatedAt,
            from.events.mapValues { pair -> EventInfoDomainModel(
                pair.value.title,
                pair.value.photoLinks,
                pair.value.likedByUser,
                pair.value.dates.map { date -> Date(date.startUnix, date.endUnix) },
                pair.value.price,
                pair.value.isFree,
                pair.value.categories
            ) }
        )
    }
}