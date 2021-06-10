package com.wayapaychat.ng.profile.presentation.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.loadImageFromUrl
import com.wayapaychat.core.utils.Helpers.replaceWithCountryCode
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.core.utils.show
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentEditProfileBinding
import com.wayapaychat.ng.profile.presentation.model.profile.EditCorporateProfileRequest
import com.wayapaychat.ng.profile.presentation.model.profile.EditPersonalProfileRequest
import com.wayapaychat.ng.profile.presentation.viewModels.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>(),
    View.OnClickListener {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var dialog: Dialog

    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun getViewModel(): EditProfileViewModel =
        editProfileViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_edit_profile

    override fun getBindingVariable(): Int = 0

    override fun getLayoutBinding(binding: FragmentEditProfileBinding) {
        this.binding = binding
    }

    val PICK_IMAGE_CODE = 5
    var imagePath = ""
    var imageString = ""

    var genderOptions = arrayListOf("Male", "Female", "Other")

    private var corporate = false

//    private val viewmodel: ProfileViewModel by navGraphViewModels(R.id.profile_nav_graph) {
//        defaultViewModelProviderFactory
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
    // Inflate the layout for this fragment
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
//        dialog = DialogHelper.customdailog(this.requireContext())!!
//        viewmodel.loading.observe(viewLifecycleOwner, loadingObserver)
//        viewmodel.message.observe(viewLifecycleOwner, messageObserver)
//        viewmodel.profileData.observe(viewLifecycleOwner, profileResponseObserver)
//        viewmodel.profilePicData.observe(viewLifecycleOwner, profilePicResponseObserver)
//        viewmodel.generalResponseData.observe(viewLifecycleOwner, updatePrpfileResponseObserver)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setViews()
        setupListeners()
        setupObservers()
        editProfileViewModel.getProfileDetails()
    }

    private fun setupListeners() {
//        binding.cancelBtn.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)
        binding.upload.setOnClickListener(this)
    }

    private fun setViews() {
        val genderAdapter = ArrayAdapter(
            requireActivity(), R.layout.spinner_adapter_item, genderOptions
        )
        binding.gender.setAdapter(genderAdapter)
    }

    private fun setupObservers() {
        with(editProfileViewModel) {
            editProfileLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        editProfileViewModel.getProfileDetails(true)
                        "Profile Updated".let { message ->
                            showSnackBar(
                                binding.root,
                                message,
                                false
                            )
                        }
                        findNavController().popBackStack()
                    }
                }
            })

            updateProfilePictureLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { showSnackBar(binding.root, it, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()
                        it.data?.let {
                            binding.profilePic.loadImageFromUrl(it, requireContext())
                        }
                        it.message?.let { showSnackBar(binding.root, it, true) }
                    }
                }
            })

            profileDetailsLiveData.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED, WayaAppState.VALIDATION_FAILED -> {
                        dismissLoadingDialog()
                        it.message?.let { message -> showSnackBar(binding.root, message, true) }
                    }

                    WayaAppState.SUCCESS -> {
                        dismissLoadingDialog()

                        it.data?.let { user ->
                            with(binding) {
                                firstname.setText(user.firstName)

                                if (user.surname.isNullOrEmpty() || user.surname == "N/A") {
                                    surname.setText(user.lastName)
                                } else {
                                    surname.setText(user.surname)
                                }

                                middleName.setText(user.middleName)
                                dob.setText(user.dateOfBirth)
                                gender.setText(user.gender)
                                email.setText(user.email)
                                number.setText(user.phoneNumber)
                                district.setText(user.district)
                                address.setText(user.address)

                                if (user.profileImage.isNotEmpty()) {
                                    binding.profilePic.loadImageFromUrl(
                                        user.profileImage,
                                        requireContext()
                                    )
                                }

                                corporate = user.corporate
                                if (corporate && user.otherDetails != null) {
                                    businessTitle.show()
                                    orgNameContainer.show()
                                    orgTypeContainer.show()
                                    businessTypeContainer.show()

                                    orgName.setText(user.otherDetails.organisationName)
                                    orgType.setText(user.otherDetails.organisationType)
                                    businessType.setText(user.otherDetails.businessType)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

//    private fun navigateAway() {
//        Prefs.putBoolean(Constants.FIRST_TIME_PROFILE_UPDATE, true)
//        if (this.requireArguments().getBoolean("fromInApp", false)) {
//            findNavController().navigate(R.id.action_editProfileFragment_to_profileLandingFragment)
//        } else {
//            handleNavigation(Prefs.getBoolean(Constants.FIRST_TIME_CHOOSE_LANDING, false))
//        }
//    }

    //    private fun cancel() {
//        Prefs.putBoolean(Constants.FIRST_TIME_PROFILE_UPDATE, false)
//        handleNavigation(Prefs.getBoolean(Constants.FIRST_TIME_CHOOSE_LANDING, false))
//    }
//
    override fun onClick(p0: View?) {
        when (p0) {
            binding.cancelBtn -> {
                navigate(WayaNavigationCommand.Back)
            }

            binding.saveBtn -> {
                updateProfile()
            }

            binding.upload -> {
//                choosePhoto()
                pickFile()
            }
        }
    }
//
//    private fun handleNavigation(chosen: Boolean) {
//        if (!chosen) {
//            val action =
//                EditProfileFragmentDirections.actionEditProfileFragmentToCustomizeLandingPages2()
//            Navigation.findNavController(binding.cancelBtn).navigate(action)
//        } else {
//            this.requireActivity().moveToAnother(LandingActivity())
//        }
//    }

//    var loadingObserver = Observer<Boolean> {
//        println(it)
//        when (it) {
//            true -> {
//                dialog = DialogHelper.customdailog(this.requireContext())!!
//            }
//            false -> {
//                dialog.dismiss()
//
//            }
//        }
//    }
//
//
//    var updateProfileResponseObserver = Observer<GeneralResponse> {
//        if (it != null) {
//            DialogHelper.showSnackBar(binding.district, it.message)
//            navigateAway()
//        }
//    }
//
//    var profileResponseObserver = Observer<ProfileUpdateRequest> {
//        if (it != null) {
//            binding.surname.setText(if (!it.surname.isNullOrEmpty()) it.surname else "")
//            binding.firstname.setText(if (!it.firstName.isNullOrEmpty()) it.firstName else "")
//            binding.middleName.setText(if (!it.middleName.isNullOrEmpty()) it.middleName else "")
//            binding.email.setText(if (!it.email.isNullOrEmpty()) it.email else "")
//            binding.number.setText(if (!it.phoneNumber.isNullOrEmpty()) it.phoneNumber else "")
//            binding.dob.setText(if (!it.dateOfBirth.isNullOrEmpty()) it.dateOfBirth else "")
//            binding.gender.setText(if (!it.gender.isNullOrEmpty()) it.gender else "")
//            imageString = it.profileImage.toString()
//            binding.profilePic.loadImageFromUrl(
//                if (!it.imageUrl.toString()
//                        .isNullOrEmpty()
//                ) it.imageUrl.toString() else it.profileImage.toString(), this.requireContext()
//            )
//            binding.address.setText(if (!it.address.isNullOrEmpty()) it.address else "")
//            binding.district.setText(if (!it.district.isNullOrEmpty()) it.district else "")
//        }
//    }
//
//    var profilePicResponseObserver = Observer<ProfileUpdateRequest> {
//        if (it != null) {
//            imageString = if (!it.imageUrl.toString()
//                    .isNullOrEmpty()
//            ) it.imageUrl.toString() else it.profileImage.toString()
//            binding.profilePic.loadImageFromUrl(it.imageUrl.toString(), this.requireContext())
//        }
//    }
//
//    var messageObserver = Observer<String> {
//        if (!it.isNullOrEmpty()) {
//            binding.errorMessageBar.warningText.text = it.toString()
//            DialogHelper.showMessage(binding.errorMessageBar.container, this.requireActivity())
//        }
//    }
//

    private fun updateProfile() {
        if (corporate) {
            var body =
                EditCorporateProfileRequest(
                    firstName = binding.firstname.text.toString(),
                    surname = binding.surname.text.toString(),
                    address = binding.address.text.toString(),
                    dateOfBirth = binding.dob.text.toString(),
                    district = binding.district.text.toString(),
                    email = binding.email.text.toString(),
                    gender = binding.gender.text.toString(),
                    phoneNumber = binding.number.text.toString().replaceWithCountryCode(),
                    middleName = binding.middleName.text.toString(),
                    businessType = binding.businessType.text.toString(),
                    organisationName = binding.orgName.text.toString(),
                    organisationType = binding.orgType.text.toString()
                )

            editProfileViewModel.editCorporateProfile(body)
        } else {
            var body =
                EditPersonalProfileRequest(
                    firstName = binding.firstname.text.toString(),
                    surname = binding.surname.text.toString(),
                    address = binding.address.text.toString(),
                    dateOfBirth = binding.dob.text.toString(),
                    district = binding.district.text.toString(),
                    email = binding.email.text.toString(),
                    gender = binding.gender.text.toString(),
                    phoneNumber = binding.number.text.toString().replaceWithCountryCode(),
                    middleName = binding.middleName.text.toString()
                )

            editProfileViewModel.editPersonalProfile(body)
        }
    }


    private fun pickFile() {
//        Dexter.withContext(this.requireContext())
//            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
//                    if (p0?.areAllPermissionsGranted()!!) {
//                        startActivityForResult(FilePickerHelpers().imagePicker(), PICK_IMAGE_CODE)
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
//                    p1: PermissionToken?
//                ) {
//                    TODO("Not yet implemented")
//                }
//
//            }).check()
        Pix.start(this, Options.init().setRequestCode(100))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val returnValues = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            println(returnValues)
            if (!returnValues.isNullOrEmpty()) {
                imagePath = returnValues.get(0)
//                val file = Uri.fromFile(File(imagePath))
//                viewmodel.updateProfilePic(requireActivity()?.contentResolver?.openInputStream(file)!!)
                editProfileViewModel.updateProfilePicture(File(imagePath))
            }
        }

//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE) {
//            if (data?.data != null) {
//                var uri = data.data
//                Glide.with(binding.profilePic.context).load(uri).into(binding.profilePic)
//                if (uri != null) {
//                    requireActivity()?.contentResolver?.openInputStream(uri)
//                        ?.let { viewmodel.updateProfilePic(it) }
//                }
//            }
//        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100))
                } else {
                    Toast.makeText(
                        this.requireActivity(),
                        "Approve permissions to be able to upload your profile picture",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}
