package com.kandyba.gotogether.data.converter.messages

import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.models.data.messages.DialogDataModel
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.general.BaseConverter

/**
 * @author Кандыба Никита
 * @since 25.04.2021
 */
class DialogDataConverter(
    private val userConverter: UserDataConverter
) : BaseConverter<List<DialogDataModel>, List<DialogDomainModel>>() {

    override fun convert(from: List<DialogDataModel>): List<DialogDomainModel> {
        return from.map {
            DialogDomainModel(
                it.id,
                userConverter.convert(it.companion),
                it.withBlockedUser,
                it.blockedByCompanion,
                it.lastMessage?.let { mes ->
                    MessageDomainModel(mes.userId, mes.dialogId, mes.text, mes.createdAt)
                }
            )
        }
    }

}