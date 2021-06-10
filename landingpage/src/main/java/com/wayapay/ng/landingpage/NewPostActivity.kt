package com.wayapay.ng.landingpage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wayapay.ng.landingpage.adapter.ImagePickerAdapter
import com.wayapay.ng.landingpage.databinding.ActivityNewPostBinding
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.post.PostViewModel
import com.wayapay.ng.landingpage.utils.ImageFilePath
import com.wayapay.ng.landingpage.utils.Permissions
import com.wayapay.ng.landingpage.utils.checkReadWritePermission
import com.wayapay.ng.landingpage.utils.getAbsolutePath
import java.io.File

class NewPostActivity : AppCompatActivity(), ImagePickerAdapter.ImagePickerClickListener {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)
        navController = this.findNavController(R.id.post_nav_host_fragment)

        val viewModelFactory = LandingViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)

        binding.votePost.setOnClickListener {
            if(navController.currentDestination?.id == R.id.votePostFragment)
                return@setOnClickListener
            navController.navigate(R.id.votePostFragment)
        }

        binding.imagePicker.setOnClickListener {
            if(navController.currentDestination?.id != R.id.postHomeFragment) {
                navController.navigate(R.id.postHomeFragment)
                return@setOnClickListener
            }
            //open file picker to pick image
            performFileSearch()
        }

        binding.navButton.setOnClickListener {
            finish()
        }

        binding.productPost.setOnClickListener {
            if(navController.currentDestination?.id == R.id.productPostFragment)
                return@setOnClickListener
            navController.navigate(R.id.productPostFragment)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    fun performFileSearch() {

        val list = viewModel.getListImageUrl().value ?: mutableListOf()

        //Check if user has selected 4 images already
        if(list.size > 3 || navController.currentDestination?.id == R.id.votePostFragment)
            return

        //Check if permission is granted
        if(!checkReadWritePermission(this))
            return

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

    private fun galleryAddPic(currentPhotoPath:String?) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
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
                performFileSearch()
            } else {
                //code for deny
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
//                if(Build.VERSION.SDK_INT < 28) {
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//                } else {
//                    val source = ImageDecoder.createSource(this.contentResolver, uri)
//                    val bitmap = ImageDecoder.decodeBitmap(source)
//                }
                //val absolutePath = getAbsolutePath(applicationContext, uri) ?: ""
                val absolutePath = ImageFilePath.getPath(applicationContext, uri)
                val list = viewModel.getListImageUrl().value ?: mutableListOf()
                list.add(uri.toString())
                viewModel.setListImageUrl(list)

                val listAbsolutePath = viewModel.getListAbsolutePath().value ?: mutableListOf()
                listAbsolutePath.add(absolutePath)
                viewModel.setListAbsolutePath(listAbsolutePath)
                Log.d("absolute_path", "IMG path = $absolutePath")
            }
        }
    }

    override fun removeImage(position: Int) {
        val list = viewModel.getListImageUrl().value ?: mutableListOf()
        list.removeAt(position)
        viewModel.setListImageUrl(list)

        val listAbsolutePath = viewModel.getListAbsolutePath().value ?: mutableListOf()
        listAbsolutePath.removeAt(position)
        viewModel.setListAbsolutePath(listAbsolutePath)
    }
}