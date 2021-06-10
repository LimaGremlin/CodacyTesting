package com.wayapaychat.ng.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wayapaychat.core.base.BaseViewModel
import com.wayapaychat.core.utils.Event
import com.wayapaychat.core.utils.getLastFourDigits
import com.wayapaychat.core.utils.getMonthDigits
import com.wayapaychat.core.utils.state.WayaAppResource
import com.wayapaychat.domain.interactors.auth.GetLoggedInUserDataUseCase
import com.wayapaychat.domain.interactors.payment.AddBankCardUseCase
import com.wayapaychat.domain.interactors.payment.DeleteBankCardUseCase
import com.wayapaychat.domain.interactors.payment.GetUserBankCardsUseCase
import com.wayapaychat.domain.interactors.payment.SubmitOtpUseCase
import com.wayapaychat.domain.interactors.payment.SubmitPhoneNumberUseCase
import com.wayapaychat.domain.interactors.profile.GetReferralCodeUseCase
import com.wayapaychat.domain.interactors.wallet.GetWalletsUseCase
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.models.payment.AddCard
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.local.room.dao.WayaGramDao
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.ng.payment.model.Wallet
import com.wayapaychat.ng.payment.utils.getWayaGramUser
import com.wayapaychat.ng.payment.utils.toPresentation
import com.wayapaychat.remote.services.wayagram.GramProfileApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

public class PaymentActivityViewModel @Inject constructor(
    private val getWalletsUseCase: GetWalletsUseCase,
    private val getLoggedInUserDataUseCase: GetLoggedInUserDataUseCase
) : BaseViewModel() {

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    @Inject
    lateinit var getUserBankCardsUseCase: GetUserBankCardsUseCase

    @Inject
    lateinit var getBankAccountUseCase: GetReferralCodeUseCase

    @Inject
    lateinit var submitPhoneNumberUseCase: SubmitPhoneNumberUseCase

    @Inject
    lateinit var addBankCardUseCase: AddBankCardUseCase

    @Inject
    lateinit var submitOtpUseCase: SubmitOtpUseCase

    @Inject
    lateinit var deleteCardUseCase: DeleteBankCardUseCase

    lateinit var clickedCard: UserBankCard

    private val _showEmptyCardsContainer = MutableLiveData<Boolean>()
    val showEmptyCardsContainer = _showEmptyCardsContainer as LiveData<Boolean>

    private val _moveToClickedCard = MutableLiveData<Event<Boolean>>()
    val moveToClickedCard = _moveToClickedCard as LiveData<Event<Boolean>>

    private val _addBankCard = MutableLiveData<Event<WayaAppResource<AddCard>>>()
    val addBankCard = _addBankCard as LiveData<Event<WayaAppResource<AddCard>>>

    private val _userWallets = MutableLiveData<WayaAppResource<List<Wallet>>>()
    val userWallets: LiveData<WayaAppResource<List<Wallet>>>
        get() = _userWallets

    private lateinit var defaultWallet: Wallet

    private val _userBankCards = MutableLiveData<WayaAppResource<List<UserBankCard>>>()
    val userBankCards: LiveData<WayaAppResource<List<UserBankCard>>>
        get() = _userBankCards

    private val _deleteCard = MutableLiveData<WayaAppResource<Boolean>>()
    val deleteCard: LiveData<WayaAppResource<Boolean>>
        get() = _deleteCard

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    private val _showConfirmationDialog = MutableLiveData<Event<Boolean>>()
    val showConfirmationDialog: LiveData<Event<Boolean>>
        get() = _showConfirmationDialog

    private val _showNoMatchBvn = MutableLiveData<Event<Boolean>>()
    val showNoMatchBvn: LiveData<Event<Boolean>>
        get() = _showNoMatchBvn

    private val _cardAdded = MutableLiveData<Event<Boolean>>()
    val cardAdded: LiveData<Event<Boolean>>
        get() = _cardAdded

    private val _navigateToOTPScreen = MutableLiveData<Event<Boolean>>()
    val navigateToOTPScreen: LiveData<Event<Boolean>>
        get() = _navigateToOTPScreen

    private val _showPhoneDialog = MutableLiveData<Event<Boolean>>()
    val showPhoneDialog: LiveData<Event<Boolean>>
        get() = _showPhoneDialog

    private val _submitOTP = MutableLiveData<WayaAppResource<Boolean>>()
    val submitOTP: LiveData<WayaAppResource<Boolean>>
        get() = _submitOTP

    private val _submitPhone = MutableLiveData<WayaAppResource<Boolean>>()
    val submitPhone: LiveData<WayaAppResource<Boolean>>
        get() = _submitPhone

    private val _userData = MutableLiveData<LoginUser>()

    private var wayaGramUser: MutableLiveData<WayaGramUser> = MutableLiveData()
    private fun setWayaGramUser(value: WayaGramUser) {wayaGramUser.value = value }
    fun getWayaGramUser(): MutableLiveData<WayaGramUser> = wayaGramUser

    init {
        getUserWallets()
        getUserDataFromDB()
    }

    fun getWayaProfileByID(userId: Int, dao: WayaGramDao){

        uiScope.launch {
            val propertiesDifferConfig = GramProfileApi.RETROFIT_SERVICE.getProfileByUIAsync(userId)

            try {
                val result = propertiesDifferConfig.await()
                val data = result.getAsJsonObject("data")
                val userData = data.getAsJsonObject("user")

                Log.d("waya_gram", "Data = $data")

                /**
                 * NOTE!! All stings values gotten from server comes with double quote.
                 * You are to remove those quiotes
                 */
                val user = data.getWayaGramUser()
                setWayaGramUser(user)
                //save User Data in Room DB
                saveUserInDB(user, dao)

            }catch (e: Exception){
                e.printStackTrace()
                Log.d("waya_gram", "error", e)

            }
        }
    }

    //Save WayaGram user In DB
    private suspend fun saveUserInDB(user: WayaGramUser, dao: WayaGramDao){
        dao.insert(user)
    }

    fun getGramUserAsync(id: String, dao: WayaGramDao) {
        uiScope.launch {
            val user = getGramUserFromDB(id, dao)
            if(user == null)getWayaProfileByID(id.toIntOrNull() ?: -1, dao)
            else setWayaGramUser(user)
        }
    }

    private suspend fun getGramUserFromDB(id: String, dao: WayaGramDao): WayaGramUser?{
        return dao.getUserByAuthId(id)
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

    fun getUserWallets() {
        viewModelScope.launch {
            _userWallets.value = WayaAppResource.loading()
            try {
                val response = getWalletsUseCase.buildUseCase()

                if (response.successful) {
                    _userWallets.value =
                        WayaAppResource.success(response.data?.map { it.toPresentation() })
                    defaultWallet =
                        _userWallets.value?.data?.first { it.accountName == "Default Wallet" }!!
                } else {
                    _userWallets.value = WayaAppResource.failed(response.message)
                }
            } catch (e: Exception) {
                _userWallets.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun getUserBankCards() {
        viewModelScope.launch {
            _userBankCards.value = WayaAppResource.loading()
            try {
                val response = getUserBankCardsUseCase.buildUseCase()

                if (response.successful) {
                    _userBankCards.value = WayaAppResource.success(response.data)

                    if (response.data?.isEmpty()!!)
                        _showEmptyCardsContainer.postValue(true)
                    else
                        _showEmptyCardsContainer.postValue(false)
                } else {
                    _userBankCards.value = WayaAppResource.failed(response.message)
                }
            } catch (e: Exception) {
                _userBankCards.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun deleteCard() {
        viewModelScope.launch {
            _deleteCard.value = WayaAppResource.loading()
            try {
                val response =
                    deleteCardUseCase.buildUseCase(DeleteBankCardUseCase.Parameter.make(clickedCard.cardNumber))

                if (response.successful) {
                    _deleteCard.value = WayaAppResource.success(response.data)
                } else {
                    _deleteCard.value = WayaAppResource.failed(response.message)
                }
            } catch (e: Exception) {
                _deleteCard.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun validateCardDetails(
        cardName: String,
        cardNumber: String,
        cardCvv: String,
        expiryMonthPosition: Int,
        cardExpiryYear: String
    ) {
        when {
            cardName.isEmpty() -> {
                _toastMessage.postValue(Event("Card name cannot be empty"))
                return
            }
            cardNumber.isEmpty() -> {
                _toastMessage.postValue(Event("Card number cannot be empty"))
                return
            }
            cardCvv.isEmpty() -> {
                _toastMessage.postValue(Event("Card cvv cannot be empty"))
                return
            }
            cardExpiryYear.isEmpty() -> {
                _toastMessage.postValue(Event("Card expiry year cannot be empty"))
                return
            }
            expiryMonthPosition == 0 -> {
                _toastMessage.postValue(Event("Please select an expiry month"))
                return
            }
        }
        _showConfirmationDialog.postValue(Event(true))
    }

    fun checkBvnEntries(bvn: String, confirmationBvn: String) {
        when {
            bvn.length != 11 -> {
                _toastMessage.postValue(Event("Invalid BVN length"))
            }
            bvn != confirmationBvn -> {
                _showNoMatchBvn.postValue(Event(true))
            }
        }
    }

    fun setItemAndNavigate(item: UserBankCard) {
        this.clickedCard = item
        _moveToClickedCard.postValue(Event(true))
    }

    fun addCard(
        cardName: String, cardNumber: String,
        expiryMonth: String, expiryYear: String,
        cardPin: String, cvvNumber: String
    ) {
        viewModelScope.launch {
            _addBankCard.value = Event(WayaAppResource.loading())
            try {
                val response = addBankCardUseCase.buildUseCase(
                    AddBankCardUseCase.Parameter.make(
                        cardNumber,
                        cvvNumber,
                        _userData.value?.email!!,
                        expiryMonth.getMonthDigits(),
                        expiryYear.substring(2),
                        cardNumber.getLastFourDigits(),
                        cardName,
                        cardPin,
                        _userData.value?.userId!!.toString(),
                        defaultWallet.accountNo
                    )
                )

                if (response.successful) {
                    _addBankCard.value = Event(WayaAppResource.success(response.data))
                    when (response.message) {
                        "Operation Successful" -> {
                            _cardAdded.postValue(Event(true))
                        }
                        "send_otp" -> {
                            _navigateToOTPScreen.postValue(Event(true))
                        }
                        "send_phone" -> {
                            _showPhoneDialog.postValue(Event(true))
                        }
                        "Incorrect PIN" -> {
                            _toastMessage.postValue(Event(response.message!!))
                        }
                    }
                } else {
                    _addBankCard.value = Event(WayaAppResource.failed(response.message))
                }
            } catch (e: Exception) {
                _addBankCard.value = Event(WayaAppResource.failed("An Error occurred"))
            }
        }
    }

    fun verifyOtp(otpEntered: String) {
        if (otpEntered.length != 6) {
            _toastMessage.postValue(Event("Incorrect OTP length entered"))
            return
        }
        submitOTP(otpEntered)
    }


    private fun submitOTP(otp: String) {
        viewModelScope.launch {
            _submitOTP.value = WayaAppResource.loading()
            try {
                val response = submitOtpUseCase.buildUseCase(
                    SubmitOtpUseCase.Parameter.make(
                        _addBankCard.value?.peekContent()?.data?.reference!!, otp
                    )
                )

                if (response.successful) {
                    _submitOTP.value = WayaAppResource.success(response.data)
                } else {
                    _submitOTP.value = WayaAppResource.failed(response.message)
                }
            } catch (e: Exception) {
                _submitOTP.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }

    fun verifyAssociatedPhoneNumber(enteredPhoneNumber: String) {
        viewModelScope.launch {
            _submitPhone.value = WayaAppResource.loading()
            try {
                val response = submitPhoneNumberUseCase.buildUseCase(
                    SubmitPhoneNumberUseCase.Parameter.make(
                        _addBankCard.value?.peekContent()?.data?.reference!!,
                        enteredPhoneNumber
                    )
                )

                if (response.successful) {
                    _submitPhone.value = WayaAppResource.success(response.data)
                    when (response.message) {
                        "send_otp" -> {
                            _submitPhone.postValue(WayaAppResource.success(true))
                        }
                    }
                } else {
                    _submitPhone.value = WayaAppResource.failed(response.message)
                }
            } catch (e: Exception) {
                _submitPhone.value = WayaAppResource.failed("An Error occurred")
            }
        }
    }




    override fun onCleared() {
        super.onCleared()

        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}


