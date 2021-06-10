package com.wayapaychat.ng.presentation

//import android.view.View
//import com.google.android.play.core.splitinstall.SplitInstallManager
//import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
//import com.google.android.play.core.splitinstall.SplitInstallRequest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wayapaychat.core.base.BaseActivity
import com.wayapaychat.core.navigation.WayaAppIntentKey
import com.wayapaychat.core.navigation.WayaAppIntentResolver
import com.wayapaychat.core.navigation.WayaAppNavigator
import com.wayapaychat.ng.R
import com.wayapaychat.ng.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    private lateinit var binding: ActivityMainBinding

//    //variables for dynamic module
//    lateinit var splitInstallManager: SplitInstallManager
//    lateinit var request: SplitInstallRequest

    @Inject
    lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject
    lateinit var wayaAppNavigator: WayaAppNavigator

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getBindingVariable(): Int = 0

    override fun getViewModel(): MainActivityViewModel {
        return mainActivityViewModel
    }

    override fun getBinding(binding: ActivityMainBinding) {
        this.binding = binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavigationView.selectedItemId = R.id.feature_home
        initBottomNavBar()

//        //initialize dynamic module
//        initDynamicModules()
    }

    private fun initBottomNavBar() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feature_account -> {
                }
            }
            false
        }
    }

    companion object : WayaAppIntentResolver<WayaAppIntentKey.Main> {

        override fun getIntent(context: Context, key: WayaAppIntentKey.Main?): Intent {
            return Intent(context, MainActivity::class.java)
        }

    }

//    /**
//     * Function to start PaymentActivity from payment module
//     */
//    fun startPaymentModule(){
//        //Intent for PaymentActivity
//        val intent = Intent().setClassName(this,
//            ModuleClassNames.PAYMENT_CLASS_NAME)
//        //check if module has been installed
//        if (isDynamicFeatureDownloaded(splitInstallManager, DynamicModuleName.PAYMENT)) {
//            //start activity if module has been installed
//            startActivity(intent)
//        } else {
//            //download feature if module has not been installed
//            downloadFeature(intent)
//        }
//    }

//    /**
//     * Initialize dynamic feature
//     */
//    private fun initDynamicModules() {
//        splitInstallManager = SplitInstallManagerFactory.create(this)
//        request = SplitInstallRequest
//            .newBuilder()
//            .addModule(DynamicModuleName.PAYMENT)
//            .build();
//    }

//    /**
//     * Download a dynamic feature and start activity using [intent]
//     */
//    private fun downloadFeature(intent: Intent) {
//        splitInstallManager.startInstall(request)
//            .addOnFailureListener {
//            }
//            .addOnSuccessListener {
//                //start required activity
//                startActivity(intent)
//            }
//            .addOnCompleteListener {
//            }
//    }
//
//    /**
//     * Uninstall list of dynamic module
//     */
//    fun uninstallDynamicFeature(list: List<String>) {
//        splitInstallManager.deferredUninstall(list)
//            .addOnSuccessListener {
//                //do som  ething on success
//            }
//    }
}
