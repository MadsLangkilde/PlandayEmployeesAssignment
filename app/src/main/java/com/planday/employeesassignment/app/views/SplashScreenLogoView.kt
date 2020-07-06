package com.planday.employeesassignment.app.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.planday.employeesassignment.R
import kotlinx.android.synthetic.main.view_splash_screen_logo.view.*

class SplashScreenLogoView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    interface SplashScreenLogoViewListener {
        fun onSplashSceenLogoAnimationFinished()
        fun onSplashSceenRetryButtonClicked()
    }

    companion object {
        private var listener: SplashScreenLogoViewListener? = null
        var animateViews = true

        @JvmStatic
        @BindingAdapter("attachSplashScreenLogoViewListener")
        fun attachSplashScreenLogoViewListener(view: SplashScreenLogoView, listener: SplashScreenLogoViewListener) {
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

        @JvmStatic
        @BindingAdapter("networkAvailable")
        fun networkAvailable(view: SplashScreenLogoView, networkAvailable: Boolean) {
            view.setNetworkAvailable(networkAvailable)
        }

        @JvmStatic
        @BindingAdapter("showRetryButton")
        fun showRetryButton(view: SplashScreenLogoView, show: Boolean) {
            view.showRetryButton(show)
        }

    }

    init {
        inflate(context, R.layout.view_splash_screen_logo, this)
        showLogoViews(animateViews)
        splashRetryButton.setOnClickListener {
            listener?.onSplashSceenRetryButtonClicked()
        }
    }

    fun showRetryButton(show: Boolean) {
        splashRetryButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    var currentText = ""
    fun setProgressText(text: String) {
        currentText = text
        Handler().postDelayed({
            splashProgressTextView.text = currentText
        }, 250)
    }

    fun showProgress(show: Boolean) {
        val currentVisibility = splashBottomProgressBar.visibility
        if ((show && currentVisibility == View.VISIBLE) || (!show && currentVisibility != View.VISIBLE))
            return

        AnimationUtils.loadAnimation(context, if (show) R.anim.fade_in else R.anim.fade_out).also { scaleSlideIn ->
            splashBottomProgressBar.startAnimation(scaleSlideIn)
        }.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (show)
                    splashBottomProgressBar.visibility   = View.VISIBLE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                if (!show)
                    splashBottomProgressBar.visibility   = View.INVISIBLE
            }
        })
    }

    private fun setNetworkAvailable(networkAvailable: Boolean) {
        val iconVisibility = splashNetworkNotAvailableIcon.visibility
        if ((!networkAvailable && iconVisibility == View.VISIBLE) || (networkAvailable && iconVisibility != View.VISIBLE))
            return

        AnimationUtils.loadAnimation(context, if (!networkAvailable) R.anim.fade_in else R.anim.fade_out).also { scaleSlideIn ->
            splashNetworkNotAvailableIcon.startAnimation(scaleSlideIn)
        }.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (!networkAvailable)
                    splashNetworkNotAvailableIcon.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                if (networkAvailable)
                    splashNetworkNotAvailableIcon.visibility = View.INVISIBLE
            }
        })
    }

    fun showLogoViews(useAnimation: Boolean) {
        if (!useAnimation) {
            splashLogo.visibility = View.VISIBLE
            splashLogoText.visibility = View.VISIBLE
            return
        }
        animateViews = false

        AnimationUtils.loadAnimation(context, R.anim.rotate_180_fade_in)
            .also { rotateFadeAnimation ->
                splashLogo.startAnimation(rotateFadeAnimation)
            }.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                AnimationUtils.loadAnimation(context, R.anim.scale_width_fade_in)
                    .also { scaleSlideIn ->
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