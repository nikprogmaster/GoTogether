package com.kandyba.gotogether.models.presentation

/**
 * Сообщение для снэкбара
 *
 * @constructor
 * @property message текст сообщения
 */
enum class SnackbarMessage(
    val message: String
) {
    NO_INTERNET_CONNECTION("Отсутствует интернет соединение"),
    USER_ALREADY_EXISTS("Пользователь с такими данными уже существует"),
    INCORRECT_PASSWORD("Неверный логин или пароль"),
    COMMON_MESSAGE("Ошибка загрузки данных"),
    COMPLAIN_SENDED("Жалоба отправлена")
}