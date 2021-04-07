package com.kandyba.gotogether.data.converter.users


import com.kandyba.gotogether.models.data.user.UserInfoDataModel
import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

class UserDataConverter: BaseConverter<UserInfoDataModel, UserInfoDomainModel>() {

    override fun convert(from: UserInfoDataModel): UserInfoDomainModel {
        return UserInfoDomainModel(
            from.tokens,
            from.id,
            from.passwordDigest,
            from.email,
            from.firstName,
            from.phone,
            from.birthDate,
            from.sex,
            from.longitude,
            from.latitude,
            from.createdAt,
            from.updatedAt,
            from.info,
            from.isLoyal
        )
    }
}