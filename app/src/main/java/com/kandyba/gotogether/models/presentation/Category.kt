package com.kandyba.gotogether.models.presentation

/**
 * Категория (интерес) события
 *
 * @constructor
 * @property categoryName название категории
 * @property serverName название категории для бэка
 */
enum class Category(
    val categoryName: String,
    val serverName: String
) {
    BUSINES_OBRAZOVANIE("Бизнес и образование", "businesObrazovanie"),
    TANCI("Танцы", "tanciBalli"),
    TEATR_KINO("Театр и кино", "teatrKino"),
    VISTAVKI_TVORCHESTVO("Выставки и искусство", "vistavkiTvorchestvo"),
    VECHERINKI_SHOY("Вечеринки и шоу", "vecherinkiShou"),
    HUMOR("Юмор", "umor"),
    KONCERTI_FESTIVALI("Концерты и фестивали", "koncertiFestivali"),
    MODA("Мода", "moda"),
    IGRI_KVESTI("Игры, квесты", "igriKvesti"),
    ROMANTIKA("Романтика", "romantika"),
    ACTIVNIY_OTDIH("Активный отдых", "activniyOtdih")
}

/**
 * Получить список всех категорий
 *
 * @return [List] список категорий
 */
fun getListOfCategories(): List<Category> =
    listOf(
        Category.BUSINES_OBRAZOVANIE,
        Category.TANCI,
        Category.TEATR_KINO,
        Category.VISTAVKI_TVORCHESTVO,
        Category.VECHERINKI_SHOY,
        Category.HUMOR,
        Category.KONCERTI_FESTIVALI,
        Category.MODA,
        Category.IGRI_KVESTI,
        Category.ROMANTIKA,
        Category.ACTIVNIY_OTDIH
    )