package com.wayapaychat.ng.auth.presentation.fragments

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.contracts.InterModuleIntent
import com.wayapaychat.core.sync.FetchAddressIntentService
import com.wayapaychat.core.utils.DialogHelper
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.isEmailValid
import com.wayapaychat.core.utils.Helpers.isValidPassword
import com.wayapaychat.core.utils.Helpers.isValidPhone
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentLoginnBinding
import com.wayapaychat.ng.auth.presentation.AuthenticationActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginnFragment : BaseFragment<FragmentLoginnBinding, AuthenticationActivityViewModel>() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentLoginnBinding
    private lateinit var dialog: Dialog

    private val authenticationActivityViewModel: AuthenticationActivityViewModel by activityViewModels()

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationActivityViewModel

    override fun getLayoutId(): Int = R.layout.fragment_loginn

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentLoginnBinding) {
        this.binding = binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        authenticationActivityViewModel.loginLiveData.observe(viewLifecycleOwner, Observer{
            when (it.state) {
                WayaAppState.LOADING -> showLoadingDialog()
                WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                    dismissLoadingDialog()
                    it.message?.let { message ->
                        showSnackBar(binding.root, message, true)
                        if (message.contains("Your Phone number has not been verified"))
                            navigate(WayaNavigationCommand.To(LoginnFragmentDirections.actionLoginnFragmentToResendOtpFragment()))
                    }
                }
                WayaAppState.SUCCESS -> {
                    dismissLoadingDialog()
                    it.data?.let { loginUser ->
                        if (loginUser.pinCreated) {
                            navigate(WayaNavigationCommand.To(LoginnFragmentDirections.actionLoginnFragmentToPinFragment()))
                        } else {
                            navigate(WayaNavigationCommand.To(LoginnFragmentDirections.actionLoginnFragmentToCreatePinFragment()))
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListener()
        watchTextChange()
        checkFormState()
    }

    private fun setupListener() {
        with(binding) {
            loginBtn.actionBtn.setOnClickListener {
                askForLocationPermission()
            }

            forgotPassword.setOnClickListener {
                navigate(
                    WayaNavigationCommand.To(
                        LoginnFragmentDirections.actionLoginnFragmentToForgotPasswordFragment()
                    )
                )
            }
        }
    }

    private fun checkFormState() {
        if (!binding.identifier.text.toString()
                .isValidPhone() && !binding.identifier.text.toString().isEmailValid()
            && binding.identifier.text.toString().length < 8 ||
            !binding.password.text.toString().isValidPassword()
        ) {
            binding.loginBtn.actionBtn.deactivate()
        } else {
            binding.loginBtn.actionBtn.activate()
        }
    }

    private fun watchTextChange() {
        binding.identifier.addTextChangedListener {
            checkFormState()
            if (!it.toString().isValidPhone() && !it.toString()
                    .isEmailValid() && it.toString().length < 8
            ) {
                binding.identifier.error = "Not a valid identifier"
            }
        }

        binding.password.addTextChangedListener {
            checkFormState()
            if (!it.toString().isValidPassword()) {
                binding.password.error = "Not a valid password"
            }
        }
    }

    private fun askForLocationPermission(){
        val wayaGramSqLiteDB = WayaDatabase.invoke(requireActivity().applicationContext).getWayaGramDao()
        Dexter.withContext(this.requireContext())
            .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted()!!) {
                        getLastKnownLocation()
                        authenticationActivityViewModel.loginUser(
                            binding.identifier.text.toString(),
                            binding.password.text.toString(),
                            findNavController(),
                            wayaGramSqLiteDB
                        )
                    }else{
                        showSnackBar(binding.root, "Accept Location Permissions to log in", false)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showSnackBar(binding.root, "Accept Location Permissions to log in", false)
                }

            }).check()
    }

    private fun getLastKnownLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.d("location_receiver", "Location = $location")
                if(location == null) return@addOnSuccessListener
                // Got last known location. In some rare situations this can be null.
                //Start FetchAddressIntentService
                Intent(requireActivity(), FetchAddressIntentService::class.java).also { intent ->
                    intent.putExtra(InterModuleIntent.LATITUDE, location.latitude)
                    intent.putExtra(InterModuleIntent.LONGITUDE, location.longitude)
                    intent.putExtra(InterModuleIntent.ORIGIN, "AuthenticationActivity")
                    requireActivity().startService(intent)
                }
            }
    }

}
