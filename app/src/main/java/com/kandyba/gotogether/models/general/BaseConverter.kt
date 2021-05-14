package com.kandyba.gotogether.models.general

/**
 * Базовый конвертер для преобразования моделей из одного слоя в другой
 */
abstract class BaseConverter<T, R> {

    /**
     * Сконвертировать модель
     *
     * @param from исходная модель
     * @return [R] сконвертированная модель
     */
    abstract fun convert(from: T): R
}

