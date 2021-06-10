package com.wayapaychat.ng.auth.presentation.activities

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.wayapaychat.core.utils.DialogHelper
import com.wayapaychat.core.utils.Helpers.hide
import com.wayapaychat.core.utils.Helpers.reveal
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.R
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class LandingActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomeView: BottomNavigationView
    private lateinit var hView: View
    private lateinit var pageTitle: MaterialTextView
    private lateinit var historyIcon: ImageView
    private lateinit var menuIcon: ImageView
    private lateinit var filterIcon: ImageView
    private lateinit var navigationView: NavigationView

    private lateinit var name: MaterialTextView
    private lateinit var profilePic: CircleImageView
    private lateinit var accountNumber: MaterialTextView
    private lateinit var earnText: MaterialTextView
    private lateinit var inviteText: MaterialTextView
    private lateinit var logout: MaterialTextView

//    val viewmodel: WayaPayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarLayout = findViewById(R.id.tool_bar_layout)
        toolbar = findViewById(R.id.tool_bar)
        pageTitle = toolbar.findViewById(R.id.tool_bar_text)
        bottomeView = findViewById(R.id.bottom_navigation)
        navigationView = findViewById(R.id.business_nav_view)
        historyIcon = findViewById(R.id.history)
        menuIcon = findViewById(R.id.menu)
        filterIcon = findViewById(R.id.filter)
        logout = findViewById(R.id.logout)
        hView = navigationView.getHeaderView(0)
        navigationView.itemIconTintList = null
        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        setLandingPage()
        navigationView.setNavigationItemSelectedListener(this)
        NavigationUI.setupWithNavController(toolbar, navController)
        NavigationUI.setupWithNavController(bottomeView, navController)

        populateNavigationHeader()

        showNotification()
        getUserData()
//        viewmodel.getMyInfo()


        val profileIcon: CircleImageView = findViewById(R.id.icon_opener)
        profileIcon.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }
//
        navController.addOnDestinationChangedListener { _, destination, _ ->
            manageOptions(destination.id)
            toolbar.title = ""
            when (destination.id) {
                R.id.landingFragment2 -> {
                    setPageTitle("Wayagram")
                }

//                R.id.wayaPayFragment -> {
//                    setPageTitle("WayaPay")
//                }
//
//                R.id.notificationFragment -> {
//                    setPageTitle("Notifications")
//                }
//
//                R.id.transactionHistoryFragment -> {
//                    setPageTitle("Transaction History")
//                }

                else -> {
                    setPageTitle("")
                }
            }
        }
//
        bottomeView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notification -> {
                    navController.popBackStack()
//                    navController.navigate(R.id.notificationFragment)
                    true
                }

                R.id.landingFragment -> {
//                    navController.navigate(R.id.landingFragment)
                    true
                }

                R.id.chat -> {
                    true
                }

                R.id.wallet -> {
                    navController.popBackStack()
//                    navController.navigate(R.id.wayaPayFragment)
                    true
                }

                R.id.discover -> {
                    true
                }
                else -> false
            }
        }

        setupListeners()

    }

    private fun setLandingPage() {
        val graph = navController.navInflater.inflate(R.navigation.main_nav_graph)
//        when (Prefs.getString(Constants.INTEREST, "")) {
//            "connect" -> {
//                graph.startDestination = R.id.landingFragment
//            }
//            "transact" -> {
//                graph.startDestination = R.id.wayaPayFragment
//            }
//            "discover" -> {
//                graph.startDestination = R.id.landingFragment
//
//            }
//            else -> graph.startDestination = R.id.wayaPayFragment
//        }
        graph.startDestination = R.id.landingFragment2
        bottomeView.selectedItemId = R.id.wallet
        navController.graph = graph
    }

    private fun setupListeners() {
        menuIcon.setOnClickListener {
            showWalletOptions(it)
        }

        historyIcon.setOnClickListener {
            navigateToHistory()
        }

        logout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
//        Prefs.remove(Constants.USER_TOKEN)
//        this.moveToAnother(AuthActivity())
    }

    private fun showNotification() {
        var badge = bottomeView.getOrCreateBadge(R.id.notification)
        badge.isVisible = true
        badge.number = 3
    }

    private fun setPageTitle(string: String) {
        pageTitle.text = string
    }

    private fun manageOptions(id: Int) {
        when (id) {
            R.id.landingFragment2 -> {
                historyIcon.hide()
                menuIcon.reveal()
                menuIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_group_add))
                filterIcon.hide()
                appBarLayout.reveal()
                bottomeView.reveal()
            }
//            R.id.wayaPayFragment -> {
//                historyIcon.reveal()
//                menuIcon.reveal()
//                filterIcon.hide()
//                appBarLayout.reveal()
//                bottomeView.reveal()
//            }

//            R.id.notificationFragment -> {
//                historyIcon.hide()
//                menuIcon.hide()
//                filterIcon.reveal()
//                appBarLayout.reveal()
//                bottomeView.reveal()
//            }

            else -> {
//                historyIcon.hide()
//                menuIcon.hide()
//                filterIcon.hide()
//                appBarLayout.hide()
//                bottomeView.hide()
            }
        }
    }


    private fun populateNavigationHeader() {
        name = hView.findViewById(R.id.name)
        profilePic = hView.findViewById(R.id.profile_pic)
        accountNumber = hView.findViewById(R.id.account_number)
        earnText = hView.findViewById(R.id.earn_with_waya)
        inviteText = hView.findViewById(R.id.invite)
        earnText.setOnClickListener {
//            navController.navigate(R.id.earnFragment)
        }
        inviteText.setOnClickListener {
//            navController.navigate(R.id.earnFragment)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(navigationView)
        when (item.itemId) {
            R.id.my_account -> {
                drawerLayout.closeDrawer(navigationView)
                startActivity(AppActivityNavCommands.getProfileIntent(this))
            }
            R.id.discover_people -> {

            }
            R.id.menu_bookmark_post -> {
                drawerLayout.closeDrawer(navigationView)
//                navController.navigate(Uri.parse("wayapay://wayagram/wayagram-bookmark"))
            }
            R.id.create_group_and_page -> {
            }
            R.id.qr_code -> {
//                navController.navigate(R.id.qrCodeFragment)
            }
            R.id.interests -> {
            }
            R.id.settings -> {
            }
            R.id.faq -> {
            }
            R.id.privacy_policy -> {
            }
            R.id.terms_and_condition -> {
            }
        }
        return true
    }

    private fun showWalletOptions(view: View) {
        val popUp = PopupMenu(this, view)
        popUp.inflate(R.menu.wallet_options)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.manage_wallets -> {
                    navController.navigate(Uri.parse("wayapay://payment/manage_wallets"))
                    return@setOnMenuItemClickListener true
                }

                R.id.manage_card -> {
                    navController.navigate(Uri.parse("wayapay://payment/manage_card"))
                    return@setOnMenuItemClickListener true
                }

                R.id.manage_bank_account -> {
                    navController.navigate(Uri.parse("wayapay://payment/manage_bank"))
                    return@setOnMenuItemClickListener true
                }

                R.id.payment_settings -> {
                    return@setOnMenuItemClickListener true
                }

                R.id.create_new_wallet -> {
                    createWalletDialog()
                    return@setOnMenuItemClickListener true
                }

                R.id.link_card -> {
                    navController.navigate(Uri.parse("wayapay://payment/add_card"))
                    return@setOnMenuItemClickListener true
                }

                R.id.link_bank -> {
                    navController.navigate(Uri.parse("wayapay://payment/add_bank"))
                    return@setOnMenuItemClickListener true
                }

                R.id.disable_wallet -> {
                    return@setOnMenuItemClickListener true
                }

                R.id.link_bvn -> {
                    navController.navigate(Uri.parse("wayapay://payment/link_bvn"))
                    return@setOnMenuItemClickListener true
                }

                R.id.all_wallet -> {
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener true
            }
        }

        popUp.show()
    }

    private fun navigateToHistory() {
//        val action = WayaPayFragmentDirections
//            .actionWayaPayFragmentToTransactionHistoryFragment()
//        navController.navigate(action)
    }

    private fun createWalletDialog() {
        DialogHelper.showDialog(
            this, true,
            this.resources.getString(R.string.create_wallet_message),
            this.resources.getString(R.string.create_new_wallet), 1
        ) {
            val message = this.resources.getString(
                R.string.single_string_concat,
                "Please input your 4 digit pin to create a new wallet"
            )
            val action = resources.getString(R.string.single_string_concat, "Create Wallet")
            navController.navigate(Uri.parse("wayapay://payment/pin/$message/$action"))
        }
    }

    private fun getUserData() {
//        viewmodel.userLiveData.observe(this, {
//            it?.let {
//                name.text = "${it.firstName} ${it.surname}"
//            }
//        })
    }
}
