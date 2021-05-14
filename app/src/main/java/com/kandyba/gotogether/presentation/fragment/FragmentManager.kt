package com.kandyba.gotogether.presentation.fragment

import androidx.fragment.app.Fragment

/**
 * Legacy-интерфейс для управления фрагментами стартовой активити
 */
interface FragmentManager {

    /**
     * Открыть фрагмент
     *
     * @param fragment инстанс фрагмента
     */
    fun openFragment(fragment: Fragment)

    /**
     * Закрыть фрагмент
     */
    fun closeFragment()

    /**
     * Открыть главную активити
     */
    fun openMainActivity()
}