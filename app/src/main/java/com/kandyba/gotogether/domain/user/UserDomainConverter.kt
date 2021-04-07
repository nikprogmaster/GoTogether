package com.kandyba.gotogether.domain.user

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel
import com.kandyba.gotogether.models.general.BaseConverter
import com.kandyba.gotogether.models.presentation.UserInfoModel

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
class UserDomainConverter : BaseConverter<UserInfoDomainModel, UserInfoModel>() {

    override fun convert(from: UserInfoDomainModel): UserInfoModel {
        return UserInfoModel(
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