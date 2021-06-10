package com.wayapaychat.ng.auth.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.litApp.litMechMerchantApp.commons.GetAddressesAsyncTask
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.EventObserver
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.navigation.utils.AppActivityNavCommands
import com.wayapaychat.ng.auth.BR
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.PinFragmentBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import com.wayapaychat.ng.auth.presentation.model.SaveLoginHistoryRequest
import com.wayapaychat.ng.auth.presentation.model.UserSignUpDetails

class PinFragment : BaseFragment<PinFragmentBinding, AuthenticationActivityViewModel>() {

    private lateinit var binding: PinFragmentBinding
    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var saveLoginHistoryRequest: SaveLoginHistoryRequest

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.pin_fragment

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutBinding(binding: PinFragmentBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        setObserver()
        setUpSharedPreference()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                var userLocation: Location? = null
                for (location in locationResult.locations) {
                    userLocation = location
                }
                userLocation?.let {
                    userLocationDetected(userLocation.latitude, userLocation.longitude)
                }
                stopLocationUpdates()
            }
        }

        binding.forgotPin.setOnClickListener {
            findNavController().navigate(R.id.action_pinFragment_to_resetPinFragment)
        }

        binding.signInBtn.setOnClickListener {
            authenticationActivityViewModel.verifyUserPinAsync(binding.pinInput.text.toString(), requireActivity(), findNavController())
        }
    }

    /**
     * This user Id is vital for WayaGram Activitiy
     */
    private fun setUpSharedPreference(){
        val user = authenticationActivityViewModel.getUser().value ?: UserSignUpDetails()
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            putInt(getString(R.string.user_id_key), user.userId)
            apply()
        }
    }

    private fun setClickListeners() {
        with(binding) {
            forgotPin.setOnClickListener {
                //ToDo navigate to forgot pin fragment
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authenticationActivityViewModel.userLandingPage.observe(viewLifecycleOwner, Observer{
            it?.let {
//                when (it) {
//                    0 -> {
//                        //startActivity(AppActivityNavCommands.getPaymentIntent(requireContext()))
//                        //Todo check before starting
//                        startActivity(AppActivityNavCommands.getLandingIntent(requireContext()))
//                        requireActivity().finish()
//                    }
//                    1 -> {
//                    }
//                    2 -> {
//                        startActivity(AppActivityNavCommands.getLandingIntent(requireContext()))
//                        requireActivity().finish()
//                    }
//                    else -> {
//                        startActivity(AppActivityNavCommands.getLandingIntent(requireContext()))
//                        requireActivity().finish()
//                    }
//                }
            }
        })
    }

    private fun setObserver() {
        with(authenticationActivityViewModel) {
            userData.observe(requireActivity(), Observer{
                it?.let {
                    binding.welcome.text = getString(R.string.welcome_back_concat, it.firstName)
                }
            })

            verifyUserPin.observe(requireActivity(), Observer{
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()
                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }
                    WayaAppState.SUCCESS -> {
//                        dismissLoadingDialog()
                        it.data?.let { pinValid ->
                            if (!pinValid) {
                                showSnackBar(binding.root, "Incorrect Pin", true)
                            } else {
                                fetchUserLocation()
                            }
                        }
                    }
                }
            })
            toastMessage.observe(viewLifecycleOwner, EventObserver {
                showSnackBar(binding.root, it, false)
            })

            saveLoginHistoryLiveData.observe(viewLifecycleOwner, Observer{
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()
                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle(R.string.error_title)
                            setMessage(it.message ?: "An Error occurred. Please try again.")
                            setPositiveButton(R.string.retry) { _, _ ->
                                saveLoginHistory()
                            }
                            setNegativeButton(R.string.cancel, null)
                        }.show()
                    }
                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                    }
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchUserLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocationDetected(location.latitude, location.longitude)
            } else {
                startLocationUpdates()
            }
        }.addOnFailureListener {
            dismissLoadingDialog()
            showSnackBar(binding.root, "An Error occurred", true)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            LocationRequest().apply {
//                priority =
            },
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun userLocationDetected(latitude: Double, longitude: Double) {
        val location = Pair(latitude, longitude)
        GetAddressesAsyncTask(
            requireContext()
        ) { address ->
            if (address == null) {
                dismissLoadingDialog()
                showSnackBar(
                    binding.root,
                    "An Error Occurred. Please try again.",
                    true
                )
            } else {
                saveLoginHistoryRequest =
                    SaveLoginHistoryRequest(
                        address.adminArea,
                        address.countryName,
                        Build.MODEL,
                        "String",
                        address.locality
                    )
                saveLoginHistory()
            }
        }.execute(location)
    }

    private fun saveLoginHistory() {
        authenticationActivityViewModel.saveLoginHistory(
            saveLoginHistoryRequest
        )
    }
}
