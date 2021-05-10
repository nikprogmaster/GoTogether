package com.kandyba.gotogether.models.general

import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.UserInfoModel

private const val EVENTS_NAME = "events"
private const val USER_NAME = "user"
private const val DIALOGS_NAME = "dialogs"

class Cache {

    private val eventsCache = HashMap<String, List<EventModel>>()
    private val userCache = HashMap<String, UserInfoModel>()
    private val dialogsCache = HashMap<String, List<DialogDomainModel>>()

    fun getCachedEvents(): List<EventModel>? {
        return if (eventsCache.isNotEmpty()) eventsCache[EVENTS_NAME] else null
    }

    fun getCachedUserInfo(): UserInfoModel? {
        return if (userCache.isNotEmpty()) userCache[USER_NAME] else null
    }

    fun getCachedDialogs(): List<DialogDomainModel>? {
        return if (dialogsCache.isNotEmpty()) dialogsCache[DIALOGS_NAME] else null
    }

    fun setUserInfo(userInfoModel: UserInfoModel?) {
        userInfoModel?.apply { userCache[USER_NAME] = userInfoModel }
    }

    fun saveEvents(events: List<EventModel>?) {
        events?.apply { eventsCache[EVENTS_NAME] = events }
    }

    fun saveDialogs(dialogs: List<DialogDomainModel>?) {
        dialogs?.apply { dialogsCache[DIALOGS_NAME] = dialogs }
    }

    fun clearUserCache() {
        instance.userCache.clear()
    }

    fun clearAllCache() {
        instance.eventsCache.clear()
        instance.userCache.clear()
    }

    companion object {
        val instance = Cache()
    }
}
