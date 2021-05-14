package com.kandyba.gotogether.models.domain.messages

import com.kandyba.gotogether.models.domain.user.UserInfoDomainModel

/**
 * Модель диалога (domain-слой)
 */
data class DialogDomainModel(
    val id: String,
    val companion: UserInfoDomainModel,
    val withBlockedUser: Boolean,
    val blockedByCompanion: Boolean,
    val lastMessage: MessageDomainModel? = null
)
