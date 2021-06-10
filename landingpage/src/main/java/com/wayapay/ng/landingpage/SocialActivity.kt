package com.wayapay.ng.landingpage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.adapter.SideNavAdapter
import com.wayapay.ng.landingpage.adapter.StringAdapter
import com.wayapay.ng.landingpage.chat.ChatApplication
import com.wayapay.ng.landingpage.databinding.ActivitySocialBinding
import com.wayapay.ng.landingpage.databinding.LandingNavHeaderBinding
import com.wayapay.ng.landingpage.dialog.NewPostBottomSheet
import com.wayapay.ng.landingpage.main.LandingViewModel
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.utils.getListNavItems
import com.wayapaychat.core.contracts.InterModuleIntent
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.remote.servicesutils.WayaDonation
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject


class SocialActivity : ChatApplication(), SideNavAdapter.OnClickListener,
    NewPostBottomSheet.OnItemClicked, StringAdapter.ListOnClickListener {

    private lateinit var binding: ActivitySocialBinding
    private lateinit var bindingHeader: LandingNavHeaderBinding
    private lateinit var viewModel: LandingViewModel
    private lateinit var navAdapter: SideNavAdapter
    private lateinit var optionsAdapter: StringAdapter
    private lateinit var navController: NavController
    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social)
        bindingHeader = DataBindingUtil.inflate(
            layoutInflater, R.layout.landing_nav_header,
            binding.navView as ViewGroup, true
        )

        //Get user data from database
        //authenticationActivityViewModel.getUserDataFromDB()

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LandingViewModel::class.java)
        navController = this.findNavController(R.id.nav_host_fragment)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        bindingHeader.lifecycleOwner = this
        bindingHeader.viewModel = viewModel

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        val landingPage = sharedPref.getString(getString(R.string.selected_page_key), "default")
        val email = sharedPref.getString(getString(R.string.email_key), "")
        Log.d("waya_gram", "User ID = $id")
        //Get user data saved in room db
        viewModel.getUserRoom()

        viewModel.getUserData().observe(this, Observer {
            val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
            setUpSocket(user.id, it.token)
            openSocket()
        })

        //Set SideNav Menu Items Using Recycler View
        navAdapter = SideNavAdapter(this)
        navAdapter.setListItems(getListNavItems(applicationContext))
        val manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        bindingHeader.recyclerView.apply {
            layoutManager = manager
            adapter = navAdapter
        }

        bindingHeader.profileImage.setOnClickListener {
            val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
            val intent = Intent(this, GramProfileActivity::class.java)
            intent.putExtra(IntentBundles.PROFILE_KEY, user)
            intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
            startActivity(intent)
            //close nav drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        bindingHeader.logOutButton.setOnClickListener {
            finish()
        }

        bindingHeader.privacyPlicyTextButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacy_policy_url))
            startActivity(browserIntent)
        }

        //initialize options adapter
        optionsAdapter = StringAdapter(this)
        val optionsManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding.optionsRecyclerview.apply {
            layoutManager = optionsManager
            adapter = optionsAdapter
        }
        //Observe changes on Options List and set in Options Adapter
        viewModel.getListOptions().observe(this, Observer {
            optionsAdapter.setList(it)
        })

        binding.navButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.fab.setOnClickListener {
            val newFragment = NewPostBottomSheet(this)
            newFragment.show(this.supportFragmentManager, "QuantityFragment")
        }

        binding.addPeople.setOnClickListener {
            navController.navigate(R.id.action_landingHomeFragment_to_interestFragment)
        }

        //On Search on EditText
        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    if (!TextUtils.isEmpty(binding.searchEditText.text.toString().trim())) {
                        val intent = Intent(this, SearchActivity::class.java)
                        intent.putExtra(InterModuleIntent.SEARCH_EXTRA, binding.searchEditText.text.toString())
                        intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
                        startActivity(intent)
                    }
                    true
                }
            }
            false
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_home -> {
                    if (navController.currentDestination?.id != R.id.landingHomeFragment)
                        onBackPressed()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_discover -> {
                    navigateToDiscoverFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_wallet -> {
                    //Navigate to Payment Module
                    startActivity(AppActivityNavCommands.getPaymentIntent(this))
                }
                R.id.page_notifications -> {
                    navigateToNotificationFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.page_chat -> {
                    navigateToChatFragment()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding.bottomNavigation.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.page_home -> Toast.makeText(
                    applicationContext,
                    "reselected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Hide list of options
        binding.optionsParent.setOnClickListener {
            viewModel.setOptionsParentVisibility(false)
        }

        //Display list of options
        binding.options.setOnClickListener {
            val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
            optionsClicked(user.id)
            viewModel.setOptionsParentVisibility(true)
        }
    }

    private fun openSocket(){
        mSocket = getSocket()
        Log.d("chat_server", "socket = $mSocket")
        mSocket.on("user_connected", onUserConnect)
        mSocket.on("load_chat_overview", onChatOverview)
        mSocket.connect()
    }

    private fun navigateToDiscoverFragment(){
        when(navController.currentDestination?.id){
            R.id.landingHomeFragment -> navController.navigate(R.id.action_landingHomeFragment_to_discoverFragment)
            R.id.notificationFragment -> navController.navigate(R.id.action_notificationFragment_to_discoverFragment)
            R.id.chatsFragment -> navController.navigate(R.id.action_chatsFragment_to_discoverFragment)
        }
    }

    private fun navigateToChatFragment(){
        when(navController.currentDestination?.id){
            R.id.landingHomeFragment -> navController.navigate(R.id.action_landingHomeFragment_to_chatsFragment)
            R.id.discoverFragment -> navController.navigate(R.id.action_discoverFragment_to_chatsFragment)
            R.id.notificationFragment -> navController.navigate(R.id.action_notificationFragment_to_chatsFragment)
        }
    }

    private fun navigateToNotificationFragment(){
        when(navController.currentDestination?.id){
            R.id.landingHomeFragment -> navController.navigate(R.id.action_landingHomeFragment_to_notificationFragment)
            R.id.discoverFragment -> navController.navigate(R.id.action_discoverFragment_to_notificationFragment)
            R.id.chatsFragment -> navController.navigate(R.id.action_chatsFragment_to_notificationFragment)
        }
    }

    private fun optionsClicked(userId: String) {
        when (navController.currentDestination?.id) {
            R.id.viewEventFragment -> {
                val event = viewModel.getSelectedEvent().value
                if (event?.creatorId == userId) viewModel.setListOptions(listOwnerEventOption)
                else viewModel.setListOptions(listUserEventOption)
            }
        }
    }

    /**
     * Side Nav Drawer on click listener
     */
    override fun onItemClicked(position: Int) {
        //close nav drawer
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val item = navAdapter.getItem(position)
        when (item.item) {
            getString(R.string.my_account) -> {
                val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                val intent = Intent(this, GramProfileActivity::class.java)
                intent.putExtra(IntentBundles.PROFILE_KEY, user)
                intent.putExtra(IntentBundles.USER_ID_KEY, user.id)
                startActivity(intent)
            }
            //Navigate to Group and Pages Activity
            getString(R.string.groups_and_pages) -> {
                val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                val intent = Intent(this, GroupAndPagesActivity::class.java)
                intent.putExtra(IntentBundles.GRAM_PROFILE_KEY, user)
                startActivity(intent)
            }
            getString(R.string.discover_people) -> {
            }
            getString(R.string.qr_code) -> {
                startActivity(AppActivityNavCommands.getQrCodeProfileIntent(this))
            }
            getString(R.string.interests) -> {

            }
            getString(R.string.settings) ->{
                startActivity(AppActivityNavCommands.getProfileIntent(this))
            }

            else -> Toast.makeText(applicationContext, item.item, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * When bottom sheet dialog is selected
     */
    override fun onOptionClicked(option: String) {
        when (option) {
            SelectOptions.EVENTS -> {
                val intent = Intent(this, NewEventActivity::class.java)
                startActivityForResult(intent, RequestCodes.EVENT_ACTIVITY_RESULT)
            }
            SelectOptions.POST -> {
                val intent = Intent(this, NewPostActivity::class.java)
                startActivityForResult(intent, RequestCodes.POST_ACTIVITY_RESULT)
            }
            SelectOptions.PAGES -> {
                val intent = Intent(this, NewPageActivity::class.java)
                startActivityForResult(intent, RequestCodes.PAGE_ACTIVITY_RESULT)
            }
            SelectOptions.GROUP -> {
                val intent = Intent(this, NewGroupActivity::class.java)
                startActivityForResult(intent, RequestCodes.GROUP_ACTIVITY_RESULT)
            }
            SelectOptions.DONATION -> {
                val intent = Intent(this, NewDonationActivity::class.java)
                startActivityForResult(intent, RequestCodes.DONATION_ACTIVITY_RESULT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCodes.EVENT_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val event = data?.extras?.getSerializable(IntentBundles.EVENT_KEY) as WayaEvent
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    event.creatorEmail = user.email
                    event.creatorId = user.id
                    event.creatorNumber = user.phoneNumber
                    viewModel.publishEvent(event)
                }
            }
            RequestCodes.POST_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val post = data?.extras?.getSerializable(IntentBundles.POST_KEY) as WayaPost
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    post.username = user.username
                    post.userId = user.id
                    viewModel.publishPost(post)
                }
            }
            RequestCodes.PAGE_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val page = data?.extras?.getSerializable(IntentBundles.PAGES_KEY) as WayaPages
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    page.ownerId = user.id
                    viewModel.publishPage(page)
                }
            }
            RequestCodes.GROUP_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val group = data?.extras?.getSerializable(IntentBundles.GROUP_KEY) as WayaGroup
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    group.ownerId = user.id
                    viewModel.publishGroup(group)
                }
            }
            RequestCodes.DONATION_ACTIVITY_RESULT ->{
                if (resultCode == Activity.RESULT_OK) {
                    val donation = data?.extras?.getSerializable(IntentBundles.DONATION_KEY) as WayaDonation
                    val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
                    donation.ownerId = user.id
                    viewModel.publishDonation(donation)
                }
            }
        }
    }

    /***
     * When option is selected
     */
    override fun itemClicked(position: Int) {
        val event = viewModel.getSelectedEvent().value ?: WayaEvent()
        //hide list options on click
        viewModel.setOptionsParentVisibility(false)
        when (optionsAdapter.getItem(position)) {
            LandingOptions.EDIT_EVENT -> {
                event.tag = Tags.EDIT
                val intent = Intent(this, NewEventActivity::class.java)
                intent.putExtra(IntentBundles.EVENT_KEY, event)
                startActivityForResult(intent, RequestCodes.EVENT_EDIT_ACTIVITY_RESULT)
            }
        }
    }

    override fun onBackPressed() {
        //check if options is visible
        if (viewModel.getOptionsParentVisibility().value != false) {
            //hide options if visible
            viewModel.setOptionsParentVisibility(false)
            return
        }
        super.onBackPressed()

        //Make home option highlighted if selected
        if(navController.currentDestination?.id == R.id.landingHomeFragment)
            binding.bottomNavigation.menu[0].isChecked = true
    }

    private val onUserConnect = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            Log.d("chat_server", "data = $data")
            val username: String
            val message: String
            try {
                username = data.getString("username")
                message = data.getString("message")
            } catch (e: JSONException) {
                Log.d("chat_server", "error ", e)
                return@Runnable
            }
        })
    }

    private val onChatOverview =
        Emitter.Listener { args ->
            runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                Log.d("chat_server", "chat data = $data")
                val username: String
                val message: String
                try {
                    username = data.getString("username")
                    message = data.getString("message")
                } catch (e: JSONException) {
                    Log.d("chat_server", "chat error ", e)
                    return@Runnable
                }
            })
        }
}
