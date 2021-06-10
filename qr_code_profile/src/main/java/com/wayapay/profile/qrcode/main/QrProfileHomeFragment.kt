package com.wayapay.profile.qrcode.main

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.WriterException
import com.wayapay.profile.qrcode.QrProfileViewModelFactory
import com.wayapay.profile.qrcode.R
import com.wayapay.profile.qrcode.databinding.FragmentQrProfileHomeBinding
import com.wayapaychat.local.room.models.WayaGramUser

/**
 * A simple [Fragment] subclass.
 * Use the [QrProfileHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrProfileHomeFragment : Fragment() {
    
    private lateinit var binding: FragmentQrProfileHomeBinding
    private lateinit var viewModel: QrProfileViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_qr_profile_home, container, false)

        val viewModelFactory = QrProfileViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(QrProfileViewModel::class.java)
        
        viewModel.getWayaGramUser().observe(requireActivity(), Observer { 
            generateBarcode()
        })
        
        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    /**
     * Code to generate an show barcode**/
    private fun generateBarcode(){
        val user = viewModel.getWayaGramUser().value ?: WayaGramUser()
        
        val inputValue = user.id

        val manager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        val qrgEncoder = QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension
        )
        try {
            val bitmap = qrgEncoder.encodeAsBitmap() ?: return
            binding.profileBarcodeImg.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.d("qrcode generator", e.toString())
        }
    }
}