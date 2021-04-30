package com.kandyba.gotogether.models.domain.messages

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel

/**
 * @author Кандыба Никита
 * @since 25.04.2021
 */
class DialogDomainModel(
    val id: String,
    val companion: UserInfoDomainModel,
    val withBlockedUser: Boolean,
    val blockedByCompanion: Boolean,
    val lastMessage: MessageDomainModel? = null
)
