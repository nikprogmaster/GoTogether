package com.kandyba.gotogether.presentation.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Базовая [ViewModel]
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * [CompositeDisposable] для хранения всех disposables
     */
    val rxCompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        rxCompositeDisposable.dispose()
    }

    /**
     * Удалить [Disposable]
     *
     * @param disposable удаляемый disposable
     */
    protected fun deleteDisposable(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            rxCompositeDisposable.remove(disposable)
        }
    }

    /**
     * Добавить [Disposable]
     *
     * @param compositeDisposable добавляемый disposable
     */
    fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
        apply { compositeDisposable.add(this) }
}