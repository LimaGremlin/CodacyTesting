package com.wayapaychat.ng.payment.managewallet

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
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.WriterException
import com.wayapaychat.core.models.TempWallet
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentGenerateQrBinding

class GenerateQrFragment : Fragment() {

    private lateinit var binding: FragmentGenerateQrBinding
    private lateinit var viewModel: ManageWalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_generate_qr, container, false)
        val viewModelFactory =
            PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(ManageWalletViewModel::class.java)

        //Todo Remove this observer here, cause the crash
//        viewModel.getTempWallet().observe(requireActivity(), {
//            generateQrCode()
//        })

        //ToDo call function out of observer
        generateQrCode()

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun generateQrCode() {

        val wallet = viewModel.getTempWallet().value ?: TempWallet()
        val inputValue = "001-${wallet.id}"

        val manager = requireActivity().applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        val qrgEncoder = QRGEncoder(
            inputValue.toString(), null,
            QRGContents.Type.TEXT,
            smallerDimension
        )

        try {
            val walletBitmap = qrgEncoder.encodeAsBitmap() ?: return
            binding.walletQrCode.setImageBitmap(walletBitmap)
        } catch (e: WriterException) {
            Log.d("qrcode generator", e.toString())
        }
    }


}