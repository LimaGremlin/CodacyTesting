package com.wayapaychat.ng.presentation.splash

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.wayapaychat.core.base.BaseActivity
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.R
import com.wayapaychat.ng.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityViewModel>() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            setObservers()
        }
    }

    private val splashActivityViewModel: SplashActivityViewModel by viewModels()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    private fun setObservers() {
        with(splashActivityViewModel) {
            isUserLoggedInLiveData.observe(this@SplashActivity, Observer{
                when (it) {
                    false -> {}
                    else -> {}
                }
            })
            onBoardingStatus.observe(this@SplashActivity, Observer{
                when (it) {
                    true -> {
                        startActivity(AppActivityNavCommands.getAuthIntent(this@SplashActivity))
                        finish()
                        //startActivity(AppActivityNavCommands.getLandingIntent(this@SplashActivity))
                    }
                    false -> {
                        startActivity(AppActivityNavCommands.getOnBoardingIntent(this@SplashActivity))
                        finish()
                    }
                }
            })
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun getViewModel(): SplashActivityViewModel = splashActivityViewModel

    override fun getBindingVariable(): Int = 0

    override fun getBinding(binding: ActivitySplashBinding) {
        this.binding = binding
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
