package com.planday.employeesassignment.app.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.planday.employeesassignment.R
import kotlinx.android.synthetic.main.view_splash_screen_logo.view.*

class SplashScreenLogoView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    interface SplashScreenLogoViewListener {
        fun onSplashSceenLogoAnimationFinished()
    }

    companion object {
        private var listener: SplashScreenLogoViewListener? = null
        var animateViews = true

        @JvmStatic
        @BindingAdapter("attachSplashScreenLogoViewListener")
        fun restoreRecyclerViewState(view: SplashScreenLogoView, listener: SplashScreenLogoViewListener) {
            this.listener = listener
        }

        @JvmStatic
        @BindingAdapter("setProgressText")
        fun setProgressText(view: SplashScreenLogoView, text: String) {
            view.setProgressText(text)
        }

        @JvmStatic
        @BindingAdapter("showProgress")
        fun showProgress(view: SplashScreenLogoView, show: Boolean) {
            view.showProgress(show)
        }

    }

    init {
        inflate(context, R.layout.view_splash_screen_logo, this)
        showLogoViews(animateViews)
    }

    fun setProgressText(text: String) {
        splashProgressTextView.text = text
    }

    fun showProgress(show: Boolean) {

    }

    fun showLogoViews(useAnimation: Boolean) {
        if (!useAnimation) {
            splashLogo.visibility       = View.VISIBLE
            splashLogoText.visibility   = View.VISIBLE
            return
        }
        animateViews = false

        AnimationUtils.loadAnimation(context, R.anim.rotate_180_fade_in).also { rotateFadeAnimation ->
            splashLogo.startAnimation(rotateFadeAnimation)
        }.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                AnimationUtils.loadAnimation(context, R.anim.scale_width_fade_in).also { scaleSlideIn ->
                    splashLogoText.startAnimation(scaleSlideIn)
                }.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        listener?.onSplashSceenLogoAnimationFinished()
                    }
                })
            }
        })


    }
}