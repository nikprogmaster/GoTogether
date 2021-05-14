package com.kandyba.gotogether.models.general

import com.kandyba.gotogether.models.presentation.EventModel
import com.kandyba.gotogether.models.presentation.UserInfoModel

/**
 * Сессионный кэш для мероприятий, пользователей, диалогов
 */
class Cache {

    /** Кэш мероприятий */
    private val eventsCache = HashMap<String, List<EventModel>>()

    /** Кэш информации о пользователе */
    private val userCache = HashMap<String, UserInfoModel>()

    /**
     * Получить закэшированные мероприятия
     *
     * @return [List] список событий
     */
    fun getCachedEvents(): List<EventModel>? {
        return if (eventsCache.isNotEmpty()) eventsCache[EVENTS_NAME] else null
    }

    /**
     * Получить закэшированную информацию о пользователе
     *
     * @return [UserInfoModel] информация о пользователе
     */
    fun getCachedUserInfo(): UserInfoModel? {
        return if (userCache.isNotEmpty()) userCache[USER_NAME] else null
    }

    /**
     * Положить инф-ю о пользователе в кэш
     *
     * @param userInfoModel инф-я пользователя
     */
    fun setUserInfo(userInfoModel: UserInfoModel?) {
        userInfoModel?.apply { userCache[USER_NAME] = userInfoModel }
    }

    /**
     * Положить информацию о событиях в кэш
     *
     * @param events cобытия
     */
    fun saveEvents(events: List<EventModel>?) {
        events?.apply { eventsCache[EVENTS_NAME] = events }
    }

    /**
     * Очистить кэш пользователя
     */
    fun clearUserCache() {
        instance.userCache.clear()
    }

    /**
     * Очистить весь кэш
     */
    fun clearAllCache() {
        instance.eventsCache.clear()
        instance.userCache.clear()
    }

    companion object {
        val instance = Cache()
        private const val EVENTS_NAME = "events"
        private const val USER_NAME = "user"
        private const val DIALOGS_NAME = "dialogs"
    }
}
