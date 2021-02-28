package com.kandyba.gotogether.models.presentation

enum class Category(
    val categoryName: String,
    val serverName: String
) {

    BUSINES_OBRAZOVANIE("Бизнес и образование", "busines_obrazovanie"),
    TANCI("Танцы", "tanci_balli"),
    TEATR_KINO("Театр и кино", "teatr_kino"),
    VISTAVKI_TVORCHESTVO("Выставки и искусство", "vistavki_tvorchestvo"),
    VECHERINKI_SHOY("Вечеринки и шоу", "vecherinki_shou"),
    HUMOR("Юмор", "umor"),
    KONCERTI_FESTIVALI("Концерты и фестивали", "koncerti_festivali"),
    MODA("Мода", "moda"),
    IGRI_KVESTI("Игры, квесты", "igri_kvesti"),
    ROMANTIKA("Романтика", "romantika"),
    ACTIVNIY_OTDIH("Активный отдых", "activniy_otdih")


}

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