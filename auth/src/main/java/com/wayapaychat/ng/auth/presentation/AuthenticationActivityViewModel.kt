package com.wayapaychat.ng.auth.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.models.SelectedPlace
import com.wayapaychat.core.newtwork.*
import com.wayapaychat.core.utils.Event
import com.wayapaychat.core.utils.Helpers
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.ChangePasswordUseCase
import com.wayapaychat.domain.interactors.auth.CreatePinUseCase
import com.wayapaychat.domain.interactors.auth.ForgotPasswordUseCase
import com.wayapaychat.domain.interactors.auth.GetLoggedInUserDataUseCase
import com.wayapaychat.domain.interactors.auth.GetUserLandingUseCase
import com.wayapaychat.domain.interactors.auth.LoginUseCase
import com.wayapaychat.domain.interactors.auth.RegisterUseCase
import com.wayapaychat.domain.interactors.auth.ResendOtpToPhoneUseCase
import com.wayapaychat.domain.interactors.auth.SaveLoginHistoryUseCase
import com.wayapaychat.domain.interactors.auth.SaveUserLandingUseCase
import com.wayapaychat.domain.interactors.auth.VerifyPhoneOtpUseCase
import com.wayapaychat.domain.interactors.auth.VerifyUserPinUseCase
import com.wayapaychat.domain.interactors.profile.GetUserProfileUseCase
import com.wayapaychat.domain.interactors.profile.UpdateUserProfileUseCase
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.models.profile.UserDomainModel
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.dao.UserDataDao
import com.wayapaychat.local.room.dao.WayaGramDao
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.presentation.model.SaveLoginHistoryRequest
import com.wayapaychat.ng.auth.presentation.model.UserSignUpDetails
import com.wayapaychat.ng.auth.presentation.utils.toUserSignUpData
import com.wayapaychat.remote.services.wayagram.GramProfileApi
import dagger.hilt.android.lifecycle.HiltViewModel
import isValidEmailAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import util.uiModels.PersonalAccountUIModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationActivityViewModel @Inject constructor() : BaseViewModel() {

    lateinit var personalAccountModel: PersonalAccountUIModel

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var getLoggedInUserDataUseCase: GetLoggedInUserDataUseCase

    @Inject
    lateinit var saveUserLandingUseCase: SaveUserLandingUseCase

    @Inject
    lateinit var getUserLandingUseCase: GetUserLandingUseCase

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var createPinUseCase: CreatePinUseCase

    @Inject
    lateinit var forgotPasswordUseCase: ForgotPasswordUseCase

    @Inject
    lateinit var changePasswordUseCase: ChangePasswordUseCase

    @Inject
    lateinit var verifyUserPinUseCase: VerifyUserPinUseCase

    @Inject
    lateinit var resendOtpToPhoneUseCase: ResendOtpToPhoneUseCase

    @Inject
    lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Inject
    lateinit var verifyPhoneOtpUseCase: VerifyPhoneOtpUseCase

    @Inject
    lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase

    @Inject
    lateinit var saveLoginHistoryUseCase: SaveLoginHistoryUseCase

    private lateinit var userOtp: String
    private lateinit var enteredEmailAddress: String
    private lateinit var enteredPhoneNumber: String

    private val _registerUserLiveData = MutableLiveData<Event<WayaAppResource<String>>>()
    val registerUserLiveData: LiveData<Event<WayaAppResource<String>>>
        get() = _registerUserLiveData

    private val _loginLiveData = MutableLiveData<WayaAppResource<LoginUser>>()
    val loginLiveData: LiveData<WayaAppResource<LoginUser>>
        get() = _loginLiveData

    private val _userLandingPage = MutableLiveData<Int>()
    val userLandingPage: LiveData<Int>
        get() = _userLandingPage

    private val _resendOtpToPhoneLiveData = MutableLiveData<Event<WayaAppResource<Boolean>>>()
    val resendOtpToPhoneLiveData: LiveData<Event<WayaAppResource<Boolean>>>
        get() = _resendOtpToPhoneLiveData

    private val _verifyPhoneOtp = MutableLiveData<Event<WayaAppResource<Boolean>>>()
    val verifyPhoneOtp: LiveData<Event<WayaAppResource<Boolean>>>
        get() = _verifyPhoneOtp

    private val _getUserProfile = MutableLiveData<WayaAppResource<UserDomainModel>>()
    val getUserProfile: LiveData<WayaAppResource<UserDomainModel>>
        get() = _getUserProfile

    private val _updateUserProfile = MutableLiveData<WayaAppResource<Boolean>>()
    val updateUserProfile: LiveData<WayaAppResource<Boolean>>
        get() = _updateUserProfile

    private val _userPinCreated = MutableLiveData<Event<WayaAppResource<Boolean>>>()
    val userPinCreated: LiveData<Event<WayaAppResource<Boolean>>>
        get() = _userPinCreated

    private val _verifyUserPin = MutableLiveData<WayaAppResource<Boolean>>()
    val verifyUserPin: LiveData<WayaAppResource<Boolean>>
        get() = _verifyUserPin

    private val _forgotPassword = MutableLiveData<WayaAppResource<String>>()
    val forgotPassword: LiveData<WayaAppResource<String>>
        get() = _forgotPassword

    private val _changePassword = MutableLiveData<WayaAppResource<String>>()
    val changePassword: LiveData<WayaAppResource<String>>
        get() = _changePassword

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    private val _moveToNewPasswordScreen = MutableLiveData<Event<Boolean>>()
    val moveToNewPasswordScreen: LiveData<Event<Boolean>>
        get() = _moveToNewPasswordScreen

    private val _userData = MutableLiveData<LoginUser>()
    val userData: LiveData<LoginUser>
        get() = _userData

    private val _saveLoginHistoryLiveData = MutableLiveData<WayaAppResource<Nothing>>()
    val saveLoginHistoryLiveData: LiveData<WayaAppResource<Nothing>>
        get() = _saveLoginHistoryLiveData

    private var personalUserAsync: MutableLiveData<PersonalUser> = MutableLiveData()
    fun getPersonalUserAsync(): MutableLiveData<PersonalUser> = personalUserAsync
    fun setPersonalUserAsync(user: PersonalUser){personalUserAsync.value = user}

    private var isMerchant: MutableLiveData<Boolean> = MutableLiveData(false)
    fun setIsMerchant(boolean: Boolean){isMerchant.value = boolean}
    fun getIsMerchant():MutableLiveData<Boolean> = isMerchant

    private var user: MutableLiveData<UserSignUpDetails> = MutableLiveData()
    private fun setUser(value: UserSignUpDetails){user.value = value}
    fun getUser():MutableLiveData<UserSignUpDetails> = user

    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getProgressBarVisibility():MutableLiveData<Boolean> = progressBarVisibility
    fun setProgressBarVisibility(boolean: Boolean){progressBarVisibility.value = boolean}

    private var selectedPlace: MutableLiveData<SelectedPlace> = MutableLiveData()
    fun getSelectedPlace():MutableLiveData<SelectedPlace> = selectedPlace
    fun setSelectedPlace(place: SelectedPlace){selectedPlace.value = place}

    private var pin: MutableLiveData<String> = MutableLiveData()
    fun getPin():MutableLiveData<String> = pin
    fun setPin(p: String){pin.value = p}

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun resendOtpToPhone(email: String = "", phoneNumber: String = "") {
        enteredPhoneNumber = phoneNumber
        viewModelScope.launch {
            resendOtpToPhoneUseCase.buildUseCase(
                param = ResendOtpToPhoneUseCase.Parameter.make(
                    if (::personalAccountModel.isInitialized) personalAccountModel.phoneNumber else phoneNumber,
                    if (::personalAccountModel.isInitialized) personalAccountModel.email else email
                )
            ).onStart {
                _resendOtpToPhoneLiveData.postValue(Event(WayaAppResource.loading()))
            }.catch {
                _resendOtpToPhoneLiveData.postValue(Event(WayaAppResource.failed("An Error Occurred")))
            }.collect {
                if (it.successful) {
                    _resendOtpToPhoneLiveData.postValue(Event(WayaAppResource.success(it.data)))
                } else
                    _resendOtpToPhoneLiveData.postValue(Event(WayaAppResource.validationFailed(it.message)))
            }
        }
    }

    fun verifyUserOtp(enteredOtp: String, navController: NavController) {
        setProgressBarVisibility(true)
        val user = getPersonalUserAsync().value ?: PersonalUser()
        val map = hashMapOf(
                "otp" to enteredOtp,
                "phone" to user.phoneNumber
        )
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.verifyOTPAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        navController.navigate(R.id.action_verifyOtpFragment_to_accountCreationSuccessFragment)
                    }
                    else ->{
                        _registerUserLiveData.postValue(Event(WayaAppResource.validationFailed("Error verifying OTP")))
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun resetPinOTPAsync(navController: NavController) {
        setProgressBarVisibility(true)
        val user = getUser().value ?: UserSignUpDetails()

        uiScope.launch {
            val propertiesDifferConfig = PasswordApi.retrofitService.requestForgotChangePinAsync(user.email)

            try {
                val result = propertiesDifferConfig.await()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        navController.navigate(R.id.action_resetPinFragment_to_resetPinOTPFragment)
                    }
                    else ->{

                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun resetPinPAsync(otp:String, navController: NavController) {
        val pin = getPin().value ?: "0"
        setProgressBarVisibility(true)
        val user = getUser().value ?: UserSignUpDetails()
        val map = hashMapOf(
                "email" to user.email,
                "otp" to otp,
                "pin" to pin
        )

        uiScope.launch {
            val propertiesDifferConfig = PasswordApi.retrofitService.changeForgotPinAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        navController.navigate(R.id.action_resetPinOTPFragment_to_resetPinSuccessFragment)
                    }
                    else ->{

                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun resendOTPAsync(phoneNumber: String, email: String){
        setProgressBarVisibility(true)
        val user = getPersonalUserAsync().value ?: PersonalUser()
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.resendOTPAsync(phoneNumber, email)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        if(body?.get("status")?.asBoolean == true){

                        }else if(body?.get("code")?.asInt == -1){

                        }
                    }
                    else ->{
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun resendOTPAsync(phoneNumber: String, email: String, navController: NavController){
        setProgressBarVisibility(true)
        val user = getPersonalUserAsync().value ?: PersonalUser()
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.resendOTPAsync(phoneNumber, email)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        if(body?.get("status")?.asBoolean == true){
                            navController.navigate(R.id.action_authResendOTPFragment_to_verifyOtpFragment)
                            setPersonalUserAsync(PersonalUser(phoneNumber = phoneNumber))
                        }else if(body?.get("code")?.asInt == -1){

                        }
                    }
                    else ->{
                        _registerUserLiveData.postValue(Event(WayaAppResource.validationFailed("Error verifying OTP")))
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    private fun getUserDataFromDB() {
        viewModelScope.launch {
            getLoggedInUserDataUseCase.buildUseCase().let {
                it.data?.let { loggedInUserData ->
                    _userData.postValue(loggedInUserData)
                }
            }
        }
    }

    fun registerPersonalUser(userPassword: String, navController: NavController) {
        setProgressBarVisibility(true)
        val personalUser = getPersonalUserAsync().value ?: PersonalUser()
        personalUser.password = userPassword

        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.personalUserRegistrationAsync(personalUser)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)
                when(result.code()){
                    200, 201 -> {
                        if(body?.get("status")?.asBoolean == true){
                            navController.navigate(R.id.action_passwordSetupFragment_to_verifyOtpFragment)
                        }
                    }
                    400 ->{
                        _registerUserLiveData.postValue(Event(WayaAppResource.validationFailed("Error: Email or Phone number must have been taken")))
                    }
                    else -> {
                        _registerUserLiveData.postValue(Event(WayaAppResource.validationFailed("A terrible error occurred, check your internet")))
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    private fun saveLogInHistoryAsync(userId: String, token: String){
        val selectedPlace = getSelectedPlace().value ?: SelectedPlace()
        val map = hashMapOf(
            "city" to selectedPlace.city ,
        "country" to selectedPlace.country,
        "device" to Helpers.getDeviceName(),
        "id" to userId,
        "ip" to "",
        "province" to selectedPlace.state,
        "userId" to userId
        )

        uiScope.launch {
            val propertiesDifferConfig = LogInHistoryApi.retrofitService.saveLogInHistoryAsync(map, token)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                Log.d("save_log_in_history", "data = $result")

            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun loginUser(email: String, password: String, navController: NavController, wayaGramSqLiteDB: WayaGramDao) {
        setProgressBarVisibility(true)
        val map = hashMapOf(
            "admin" to "false",
            "email" to email,
            "password" to password
        )
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.logInAsync(map)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)
                when(result.code()){
                    200, 201 -> {
                        if(body?.get("status")?.asBoolean == true){
                            val user = body.get("data").asJsonObject.toUserSignUpData()
                            setUser(user)
                            //Get WayaGram user details from server and save
                            getWayaProfileByID(user.userId, wayaGramSqLiteDB)
                            //save user log in details
                            saveLogInHistoryAsync(user.userId.toString(), user.token)
                            Log.d("waya_user", "user = $user")
                            if(user.pinCreated)
                                navController.navigate(R.id.action_loginnFragment_to_pinFragment)
                            else {
                                navController.navigate(R.id.action_loginnFragment_to_createPinFragment)
                            }
                        }else if(body?.get("code")?.asInt == -2){
                            setUser(UserSignUpDetails(email = email))
                            navController.navigate(R.id.action_loginnFragment_to_authResendOTPFragment)
                        }
                    }
                    400 ->{
                        _loginLiveData.postValue(WayaAppResource.failed("Authentication failed"))
                    }
                    else -> {
                        _loginLiveData.postValue(WayaAppResource.failed("Authentication failed"))
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun saveUserLandingSelection(landingConstant: Int) {
        viewModelScope.launch {
            saveUserLandingUseCase.buildUseCase(
                SaveUserLandingUseCase.Parameter(
                    landingConstant
                )
            ).collect {}
        }
    }

    fun createPin(userPin: String, theActivity: Activity, navController: NavController) {
        setProgressBarVisibility(true)
        val user = getUser().value ?: UserSignUpDetails()
        val pin = CreatePin(
            pin = userPin.toIntOrNull() ?: -1,
            email = user.email,
            userId = user.userId
        )
        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.createPinAsync(pin, user.token)
            val sqLiteDB = WayaDatabase.invoke(theActivity.applicationContext).getUserDataDao()

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        saveUserData(sqLiteDB, UserDataLocalModel(
                                id = user.userId,
                                phoneNumber = user.phoneNumber,
                                email = user.email,
                                roles = listOf(),
                            token = user.token,
                                corporate = user.isCorporate,
                                firstName = user.firstName,
                                lastName = user.lastName,
                                surname = user.lastName
                        ))
                        if(body?.get("status")?.asBoolean == true){
                            navController.navigate(R.id.action_createPinFragment_to_pinSetFragment)
                        }
                    }
                    else ->{
                        _userPinCreated.postValue(Event(WayaAppResource.validationFailed("Error setting up pin")))
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)
                _userPinCreated.postValue(Event(WayaAppResource.validationFailed("A terrible error occured")))

            }
        }
    }

    //add a new item to sqlite db using room
    private fun saveUserData(sqLiteDB: UserDataDao, user: UserDataLocalModel) {
        uiScope.launch {
            sqLiteDB.saveUserDetails(user)
        }
    }

    //Save WayaGram user In DB
    private suspend fun saveUserInDB(wayaGramSqLiteDB: WayaGramDao, user: WayaGramUser){
        wayaGramSqLiteDB.insert(user)
    }

    fun getWayaProfileByID(userId: Int, wayaGramSqLiteDB: WayaGramDao){

        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")

                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                //save User Data in Room DB
                saveUserInDB(wayaGramSqLiteDB, user)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    fun verifyUserPinAsync(userPin: String, theActivity: Activity, navController: NavController){
        setProgressBarVisibility(true)
        val user = getUser().value ?: UserSignUpDetails()
        val sqLiteDB = WayaDatabase.invoke(theActivity.applicationContext).getUserDataDao()

        uiScope.launch {
            val propertiesDifferConfig = WayaPayApi.retrofitService.validatePinAsync(userPin, user.token)

            try {
                val result = propertiesDifferConfig.await()
                val body = result.body()
                setProgressBarVisibility(false)

                when(result.code()){
                    200, 201 -> {
                        if(body?.get("status")?.asBoolean == true){
                            saveUserData(sqLiteDB, UserDataLocalModel(
                                    id = user.userId,
                                    phoneNumber = user.phoneNumber,
                                    email = user.email,
                                    roles = listOf(),
                                token = user.token,
                                    corporate = user.isCorporate,
                                    firstName = user.firstName,
                                    lastName = user.lastName,
                                    surname = user.lastName
                            ))
                            val sharedPref = theActivity.getSharedPreferences(
                                theActivity.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                            )
                            val pageInt = sharedPref.getInt(theActivity.getString(R.string.landing_page_key), -1)
                            theActivity.startActivity(AppActivityNavCommands.getLandingIntent(theActivity.applicationContext))
                            theActivity.finish()
                        }else {
                            Toast.makeText(theActivity.applicationContext, theActivity.getString(R.string.invalid_pin), Toast.LENGTH_LONG).show()
                        }
                    }
                    else ->{
                        Toast.makeText(theActivity.applicationContext, theActivity.getString(R.string.error_verifying_pin), Toast.LENGTH_LONG).show()
                    }
                }


            } catch (e: Exception) {
                setProgressBarVisibility(false)
                e.printStackTrace()
                Log.d("waya_gram", "error", e)
                Toast.makeText(theActivity.applicationContext, theActivity.getString(R.string.an_error_occured), Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun forgotPassword(email: String) {
        enteredEmailAddress = email
        viewModelScope.launch {
            forgotPasswordUseCase.buildUseCase(
                param = ForgotPasswordUseCase.Parameter.make(
                    email
                )
            ).onStart {
                _forgotPassword.postValue(WayaAppResource.loading())
            }.catch {
                _forgotPassword.postValue(WayaAppResource.failed("An Error Occurred"))
            }.collect {
                if (it.successful)
                    _forgotPassword.postValue(WayaAppResource.success(it.data))
                else
                    _forgotPassword.postValue(WayaAppResource.validationFailed(it.message))
            }
        }
    }

    fun validateEmail(emailAddress: String) {
        if (emailAddress.isEmpty())
            _toastMessage.postValue(Event("Email address cannot be empty"))
        else if (!emailAddress.isValidEmailAddress())
            _toastMessage.postValue(Event("Email address is not valid"))
        else
            forgotPassword(emailAddress)
    }

    fun checkOtp(enteredOtp: String) {
        if (enteredOtp.length != 6) {
            _toastMessage.postValue(Event("Invalid OTP code entered"))
        } else
            validateOTp(enteredOtp)
    }

    private fun validateOTp(enteredOtp: String) {
        this.userOtp = enteredOtp
        _moveToNewPasswordScreen.postValue(Event(true))
    }

    fun verifyUserPin(userPin: String) {
        if (userPin.length != 4) {
            _toastMessage.postValue(Event("Invalid OTP Code entered"))
            return
        }
        viewModelScope.launch {
            verifyUserPinUseCase.buildUseCase(
                param = VerifyUserPinUseCase.Parameter.make(
                    userPin = userPin,
                    userId = _userData.value?.userId.toString()
                )
            ).onStart {
                _verifyUserPin.postValue(WayaAppResource.loading())
            }.catch {
                _verifyUserPin.postValue(WayaAppResource.failed("An Error Occurred"))
            }.collect {
                if (it.successful) {
                    it.data?.let {
//                        getUserLandingPage()
                        _verifyUserPin.postValue(WayaAppResource.success(it))
                    }
                } else
                    _verifyUserPin.postValue(WayaAppResource.validationFailed(it.message))
            }
        }
    }

    fun getUserLandingPage() {
        viewModelScope.launch {
            getUserLandingUseCase.buildUseCase().collect {
                it.data?.let { landingConstant ->
                    _userLandingPage.postValue(landingConstant)
                }
            }
        }
    }

    fun checkEnteredPasswordRegex(password: String, confirmPassword: String) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            _toastMessage.postValue(Event("Invalid Password or confirm Password entered"))
        } else if (password.isNotEmpty() && !doesPasswordMeetRegex(password))
            _toastMessage.postValue(Event("Password not not meet requirement"))
        else if (doesPasswordMeetRegex(password)) {
            if (confirmPassword == password)
                changeUserPassword(password)
            else
                _toastMessage.postValue(Event("Passwords do not match"))
        }
    }

    private fun changeUserPassword(password: String) {
        viewModelScope.launch {
            changePasswordUseCase.buildUseCase(
                param = ChangePasswordUseCase.Parameter.make(
                    enteredEmailAddress,
                    password,
                    userOtp
                )
            ).onStart {
                _changePassword.postValue(WayaAppResource.loading())
            }.catch {
                _changePassword.postValue(WayaAppResource.failed("An Error Occurred"))
            }.collect {
                if (it.successful) {
                    it.data?.let {
                        _changePassword.postValue(WayaAppResource.success(it))
                    }
                } else
                    _changePassword.postValue(WayaAppResource.validationFailed(it.message))
            }
        }
    }

    private fun doesPasswordMeetRegex(password: String): Boolean {
        val pattern =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$".toRegex()
        return pattern.matches(password)
    }

    fun getUserProfileDetails() {
        _getUserProfile.postValue(WayaAppResource.loading())

        viewModelScope.launch {
            try {
                val response = getUserProfileUseCase.buildUseCase(
                    param = GetUserProfileUseCase.Parameter.make(
                        _userData.value?.userId!!
                    )
                )
                if (response.successful) {
                    response.data?.let {
                        _getUserProfile.postValue(WayaAppResource.success(it))
                    }
                } else
                    _getUserProfile.postValue(WayaAppResource.validationFailed(response.message))
            } catch (e: Exception) {
                _getUserProfile.postValue(WayaAppResource.failed("An Error Occurred"))
            }
        }
    }

    fun updateUserProfile(
        firstName: String,
        lastName: String,
        dob: String,
        gender: String,
        district: String,
        address: String,
        emailAddress: String,
        phoneNumber: String,
        middleName: String
    ) {
        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || gender.isEmpty() || district.isEmpty()
            || address.isEmpty() || emailAddress.isEmpty() || phoneNumber.isEmpty() || middleName.isEmpty()
        ) {
            _toastMessage.postValue(Event("Please ensure no field is empty"))
            return
        }
        viewModelScope.launch {
            updateUserProfileUseCase.buildUseCase(
                param = UpdateUserProfileUseCase.Parameter.make(
                    _userData.value?.userId!!,
                    address,
                    dob,
                    district,
                    emailAddress,
                    firstName,
                    gender,
                    middleName,
                    phoneNumber,
                    lastName
                )
            ).onStart {
                _updateUserProfile.postValue(WayaAppResource.loading())
            }.catch {
                _updateUserProfile.postValue(WayaAppResource.failed("An Error Occurred"))
            }.collect {
                if (it.successful) {
                    it.data?.let {
                        _updateUserProfile.postValue(WayaAppResource.success(it))
                    }
                } else
                    _updateUserProfile.postValue(WayaAppResource.validationFailed(it.message))
            }
        }
    }

    fun saveLoginHistory(request: SaveLoginHistoryRequest) {
        _saveLoginHistoryLiveData.postValue(WayaAppResource.loading())

        viewModelScope.launch {
            try {
                val response = saveLoginHistoryUseCase.buildUseCase(
                    SaveLoginHistoryUseCase.Parameter.make(
                        request.city,
                        request.country,
                        request.device,
                        request.ip,
                        request.province
                    )
                )
                if (response.successful) {
                    _saveLoginHistoryLiveData.postValue(WayaAppResource.success())
                    getUserLandingPage()
                } else
                    _saveLoginHistoryLiveData.postValue(WayaAppResource.validationFailed(response.message))
            } catch (e: Exception) {
                _saveLoginHistoryLiveData.postValue(WayaAppResource.failed("An Error Occurred"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }

}
