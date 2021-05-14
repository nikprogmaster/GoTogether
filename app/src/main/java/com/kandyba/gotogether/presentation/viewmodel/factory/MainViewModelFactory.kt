package com.kandyba.gotogether.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Фабрика для создания MainViewModel
 *
 * @constructor
 * @property viewModelCreator криэйтор для создания вьюмодели
 */
class MainViewModelFactory(
    private val viewModelCreator: () -> ViewModel
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelCreator.invoke() as T
    }
}