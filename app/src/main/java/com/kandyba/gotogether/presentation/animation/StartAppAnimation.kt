package com.kandyba.gotogether.presentation.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView

/**
 * Стартовая анимация приложения
 */
class StartAppAnimation {

    /** Список аниматоров */
    private var animators: MutableList<Animator> = mutableListOf()

    /**
     * Установить и запустить анимацию
     *
     * @param logo изображение, которое нужно анимировать
     */
    fun setAndStartAnimation(logo: ImageView) {
        animators = mutableListOf()
        // alpha
        val alphaAnimator = ValueAnimator.ofFloat(1f, 0.6f)
        alphaAnimator.duration = 800
        alphaAnimator.repeatMode = ValueAnimator.REVERSE
        alphaAnimator.addUpdateListener { animator ->
            logo.alpha = (animator.animatedValue as Float)
        }
        animators.add(alphaAnimator)

        // scale
        val scaleAnimator = ValueAnimator.ofFloat(1f, 0.96f)
        scaleAnimator.duration = 800
        scaleAnimator.repeatMode = ValueAnimator.REVERSE
        scaleAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            logo.scaleX = scale
            logo.scaleY = scale
        }
        animators.add(scaleAnimator)
        for (animator in animators) {
            animator.start()
        }
    }

    /**
     * Завершить анимацию
     */
    fun finishAnimation() {
        if (animators.isNotEmpty()) {
            for (animator in animators) {
                animator.end()
            }
        }
    }
}