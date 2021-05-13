package com.kandyba.gotogether.models.general

abstract class BaseConverter<T, R> {
    abstract fun convert(from: T): R
}

