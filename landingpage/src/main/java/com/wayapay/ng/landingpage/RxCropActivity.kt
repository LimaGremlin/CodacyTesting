package com.wayapay.ng.landingpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.util.Logger
import com.wayapay.ng.landingpage.databinding.ActivityRxCropBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.RxCropActions
import com.wayapay.ng.landingpage.utils.Permissions
import io.reactivex.SingleSource
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

class RxCropActivity : AppCompatActivity() {
    private val mCompressFormat = Bitmap.CompressFormat.JPEG
    private var mSourceUri: Uri? = null
    private lateinit var binding : ActivityRxCropBinding
    private var mFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rx_crop)

        val intent = intent
        val bundle = intent.extras

        if(bundle != null){
            mSourceUri = Uri.parse(bundle.getString(IntentBundles.IMAGE_URI_KEY))

            val cropAction = bundle.run { getString(IntentBundles.INTENT_ACTION_KEY) }
            if (cropAction!!.compareTo(RxCropActions.RECTANGLE) == 0) {
                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)
            } else if (cropAction.compareTo(RxCropActions.SQUARE) == 0) {
                binding.cropImageView.setCropMode(CropImageView.CropMode.SQUARE)
            }
            //mCropView.load(mSourceUri).executeAsCompletable();
            Glide.with(applicationContext).load(mSourceUri).into(binding.cropImageView)
        }

        binding.cropImageView.setMinFrameSizeInDp(100)

        binding.cancelImageButton.setOnClickListener {
            onBackPressed()
        }

        binding.cropImageButton.setOnClickListener {
            val rc = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (rc == PackageManager.PERMISSION_GRANTED) {
                cropImage()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Permissions.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun cropImage(): Disposable {
        binding.cropImageButton.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        val saveUri = createSaveUri()

        return binding.cropImageView.crop(mSourceUri)
            .executeAsSingle()
            .flatMap(Function<Bitmap, SingleSource<Uri>> { bitmap ->
                binding.cropImageView.save(bitmap)
                    .compressFormat(mCompressFormat)
                    .executeAsSingle(saveUri)
            })
            .doOnSubscribe(Consumer<Disposable> {
                //showProgress();
            })
            .doFinally(Action {
                //dismissProgress();
                //mCropButton.setEnabled(true);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ uri ->
                binding.progressBar.visibility = View.GONE
                binding.cropImageButton.isEnabled = true
                returnResult(uri)
            }, { })
    }

    private fun returnResult(uri: Uri) {
        val returnIntent = Intent()
        returnIntent.putExtra("result", uri.toString())
        returnIntent.putExtra("filePath", mFilePath)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun createSaveUri(): Uri? {
        return createNewUri(applicationContext, mCompressFormat)
    }

    private fun getDirPath(): String {
        var dirPath = ""
        var imageDir: File? = null
        val extStorageDir = Environment.getExternalStorageDirectory()
        if (extStorageDir.canWrite()) {
            imageDir = File(extStorageDir.path + "/"+getString(R.string.app_name))
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.path
            }
        }
        return dirPath
    }

    @SuppressLint("SimpleDateFormat")
    fun createNewUri(context: Context, format: Bitmap.CompressFormat): Uri? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val title = dateFormat.format(today)
        val dirPath = getDirPath()
        val fileName = "scv" + title + "." + getMimeType(format)
        val path = "$dirPath/$fileName"
        mFilePath = path
        val file = File(path)
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format))
        values.put(MediaStore.Images.Media.DATA, path)
        val time = currentTimeMillis / 1000
        values.put(MediaStore.MediaColumns.DATE_ADDED, time)
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time)
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length())
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        Logger.i("SaveUri = " + uri!!)
        return uri
    }

    private fun getMimeType(format: Bitmap.CompressFormat): String {
        if (format == Bitmap.CompressFormat.JPEG) return "jpeg"
        else if (format == Bitmap.CompressFormat.PNG) return "png"
        return "png"
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
            Permissions.WRITE_EXTERNAL_STORAGE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cropImage()
                } else {
                //code for deny
                    finish()
                }
        }
    }
}