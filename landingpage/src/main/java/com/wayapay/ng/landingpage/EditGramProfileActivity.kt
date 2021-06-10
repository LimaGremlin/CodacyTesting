package com.wayapay.ng.landingpage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.databinding.ActivityEditGramProfileBinding
import com.wayapay.ng.landingpage.editprofile.EditGramProfile
import com.wayapay.ng.landingpage.editprofile.EditGramProfileViewModel
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.utils.*
import com.wayapaychat.core.utils.hideKeyboard
import java.io.File
import java.io.IOException

class EditGramProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGramProfileBinding
    private lateinit var viewModel: EditGramProfileViewModel

    private var userChosenTask = -1
    private var cameraImgUri:String = ""
    private var whichImage = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_gram_profile)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditGramProfileViewModel::class.java)

        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        viewModel.getGramUserAsync(id.toString())

        binding.headerButton.setOnClickListener {
           whichImage = 2
           buildImagePicker()
        }
        binding.headerChoseButton.setOnClickListener {
            whichImage = 2
            buildImagePicker()
        }
        binding.profileButton.setOnClickListener {
            whichImage = 1
            buildImagePicker()
        }
        binding.profileChooseButton.setOnClickListener {
            whichImage = 1
            buildImagePicker()
        }

        binding.saveButton.setOnClickListener {
            hideKeyboard(this)
            if(check())viewModel.updateUserProfile()
        }

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun check():Boolean{

        val profile = viewModel.getEditGramProfile().value ?: EditGramProfile()

        if (TextUtils.isEmpty(binding.userName.text.toString())){
            binding.userName.requestFocus()
            binding.userName.error = ""
            return false
        }

        if(!userNameIsConfirmed(binding.userName.text.toString())){
            binding.userName.error = "Check username"
            binding.userName.requestFocus()
            return false
        }

        profile.username = binding.userName.text.toString()
        profile.bio = binding.bioText.text.toString()
        profile.isPrivate = binding.checkBox2.isChecked
        viewModel.setEditGramProfile(profile)

        return true
    }

    private fun buildImagePicker(){
        val types =
            arrayOf<CharSequence>(getString(R.string.camera)
                , getString(R.string.gallery))

        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setItems(types, DialogInterface.OnClickListener { _, which ->

            val result = checkReadWritePermission(this)

            when(types[which]){
                getString(R.string.camera) -> {
                    userChosenTask = 1
                    if(result)checkForCameraPermission()
                    return@OnClickListener
                }
                getString(R.string.gallery) ->{
                    userChosenTask = 2
                    if(result)performFileSearch()
                    return@OnClickListener
                }
            }
        })

        builder.show()
    }

    private fun checkForCameraPermission(){
        val locationPermission =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                Permissions.CAMERA_REQUEST_CODE
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    private fun performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "image/*"
        }

        startActivityForResult(intent, RequestCodes.READ_REQUEST_CODE)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(applicationContext.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(applicationContext).apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        val uri = Uri.fromFile(File(absolutePath))
                        cameraImgUri = uri.toString()
                    }
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        getString(R.string.package_name),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RequestCodes.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun galleryAddPic(currentPhotoPath:String?) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun cropImage(uri: Uri) {
        val intent = Intent(this, RxCropActivity::class.java)
        intent.putExtra(IntentBundles.IMAGE_URI_KEY, uri.toString())
        //Check which of the images the user is picking for
        if(whichImage == 1)
        //if it is for profile, make crop square
            intent.putExtra(IntentBundles.INTENT_ACTION_KEY, RxCropActions.SQUARE)
        else
        //if it is for background make rectangle
            intent.putExtra(IntentBundles.INTENT_ACTION_KEY, RxCropActions.RECTANGLE)
        startActivityForResult(intent, RequestCodes.CROP_IMAGE)
    }

    /**
     * Request for permission on runtime
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Permissions.WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChosenTask == 1)
                    checkForCameraPermission()
                else if (userChosenTask == 2)
                    performFileSearch()
            } else {
                //code for deny
            }
            Permissions.CAMERA_REQUEST_CODE -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //viewModel.setProgressBarVisibility(false) //hide progress bar

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == RequestCodes.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            data?.data?.also { uri ->
                Log.i("ImagePicker", "Uri: $uri")
                cropImage(uri)
            }
        }

        if (requestCode == RequestCodes.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //val imageBitmap = data.extras.get("data") as Bitmap
            //binding.imageButton.setImageBitmap(imageBitmap)
            if(!TextUtils.isEmpty(cameraImgUri)){
                Log.d("CameraCall", cameraImgUri)
                //viewModel.setCurrentPhotoUrl(uri.toString())
                galleryAddPic(cameraImgUri)
                cropImage(Uri.parse(cameraImgUri))
                //viewModel.setCurrentPhotoUrl(cameraImgUri)
            }
        }

        if (requestCode == RequestCodes.CROP_IMAGE && resultCode == Activity.RESULT_OK){

            val result = data?.getStringExtra("result")
            val filePath = data?.getStringExtra("filePath")
            val uri = Uri.parse(result)
            val group = viewModel.getEditGramProfile().value ?: EditGramProfile()
            if(whichImage == 1) {
                group.profileImgPath = filePath ?: ""
                binding.profileButton.setProfileUri(uri.toString())
            }
            if(whichImage == 2) {
                group.headerImgPath = filePath ?: ""
                binding.headerButton.setImageUri(uri.toString())
            }
            viewModel.setEditGramProfile(group)

        }
    }
}