package com.kandyba.gotogether.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author Кандыба Никита
 * @since 26.04.2021
 */
class DialogsViewModelFactory(
    private val viewModelCreator: () -> ViewModel
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelCreator.invoke() as T
    }
}