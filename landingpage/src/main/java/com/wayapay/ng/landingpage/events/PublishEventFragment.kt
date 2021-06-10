package com.wayapay.ng.landingpage.events

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.NewEventActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.RxCropActivity
import com.wayapay.ng.landingpage.databinding.FragmentPublishEventBinding
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.utils.Permissions
import com.wayapay.ng.landingpage.utils.checkReadWritePermission
import com.wayapay.ng.landingpage.utils.createImageFile
import java.io.File
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [PublishEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PublishEventFragment : Fragment() {

    private lateinit var binding: FragmentPublishEventBinding
    private lateinit var viewModel: EventViewModel

    private var userChosenTask = -1
    private var cameraImgUri:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_publish_event, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(EventViewModel::class.java)

        val wEvent = viewModel.getWayaEvent().value ?: WayaEvent()
        if(wEvent.tag == Tags.EDIT){
            viewModel.setButtonText(getString(R.string.edit))
        }else viewModel.setButtonText(getString(R.string.publish))

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewEventActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            if(event.tag == Tags.EDIT){
                viewModel.updateEvent(event)
            }else {
                val returnIntent = Intent()
                returnIntent.putExtra(IntentBundles.EVENT_KEY, event)
                requireActivity().setResult(Activity.RESULT_OK, returnIntent)
                requireActivity().finish()
            }
        }

        binding.imageButton.setOnClickListener {
            buildImagePicker()
        }

        binding.imageButton2.setOnClickListener {
            buildImagePicker()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner  = requireActivity()
        return binding.root
    }

    private fun buildImagePicker(){
        val types =
            arrayOf<CharSequence>(getString(R.string.camera)
                , getString(R.string.gallery))

        val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setItems(types, DialogInterface.OnClickListener { _, which ->

            val result = checkReadWritePermission(requireContext())

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
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d("shank_location", "permission not granted")
            requestPermissions(
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
            takePictureIntent.resolveActivity(requireActivity().applicationContext.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(requireActivity().applicationContext).apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        Log.d("shank", "file => $it")
                        Log.d("shank", "absolute path => $absolutePath")
                        val uri = Uri.fromFile(File(absolutePath))
                        cameraImgUri = uri.toString()
                    }
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("shank", "exception => $ex")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity().applicationContext,
                        getString(R.string.package_name),
                        it
                    )
                    Log.d("shank", "photo uri = ${photoURI.toString()}")
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
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }

    private fun cropImage(uri: Uri) {
        val intent = Intent(requireActivity(), RxCropActivity::class.java)
        intent.putExtra(IntentBundles.IMAGE_URI_KEY, uri.toString())
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
            val event = viewModel.getWayaEvent().value ?: WayaEvent()
            event.eventImageUri = uri.toString()
            event.filePath = filePath ?: ""
            viewModel.setWayaEvent(event)

        }
    }
}