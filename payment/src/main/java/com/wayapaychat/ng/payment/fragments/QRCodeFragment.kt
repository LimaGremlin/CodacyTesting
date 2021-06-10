package com.wayapaychat.ng.payment.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.wayapaychat.core.base.BaseFragment
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.showShortToast
import com.wayapaychat.core.utils.state.WayaAppState
import com.wayapaychat.ng.payment.PaymentActivityViewModel
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.FragmentQRCodeBinding
import com.wayapaychat.ng.payment.fragments.completeTransaction.ARG_TRANSACTION_DETAILS
import com.wayapaychat.ng.payment.model.Wallet
import com.wayapaychat.ng.payment.utils.*
import com.wayapaychat.ng.payment.viewModels.QRCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class QRCodeFragment : BaseFragment<FragmentQRCodeBinding, QRCodeViewModel>(), View.OnClickListener {

    private val qrCodeViewModel by viewModels<QRCodeViewModel>()
    private val activityViewModel by activityViewModels<PaymentActivityViewModel>()
    private lateinit var binding: FragmentQRCodeBinding
    private lateinit var qrCodeBitmap: Bitmap
    private lateinit var wallets: List<Wallet>
    private var userName: String? = null

    override fun getViewModel(): QRCodeViewModel =
        qrCodeViewModel

    override fun getLayoutId(): Int =
        R.layout.fragment_q_r_code

    override fun getBindingVariable(): Int =
        0

    override fun getLayoutBinding(binding: FragmentQRCodeBinding) {
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.deactivate()
        binding.share.deactivate()

        setupObservers()
        setupClickListeners()
        qrCodeViewModel.getLoggedInUserName()
    }

    private fun setupObservers(){
        with(activityViewModel){
            userWallets.observe(viewLifecycleOwner, {
                when (it.state) {
                    WayaAppState.LOADING -> showLoadingDialog()

                    WayaAppState.FAILED -> {

                        dismissLoadingDialog()

                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle(R.string.error_title)
                            setMessage(it.message)
                            setPositiveButton(R.string.try_again){_,_ ->
                                getUserWallets()
                            }
                            setNegativeButton(R.string.cancel){_,_ ->
                                findNavController().popBackStack()
                            }
                            show()
                        }

                    }

                    WayaAppState.SUCCESS -> {
                        it.data?.let { wallets ->
                            val walletNames = mutableListOf(getString(R.string.select_wallet))

                            this@QRCodeFragment.wallets = wallets
                            for (wallet in wallets){
                                var text = wallet.accountName

                                if (wallet.default){
                                    text += "(default)"
                                }

                                walletNames.add(text)
                            }

                            binding.walletsSP.adapter =
                                ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    walletNames
                                )
                        }

                        dismissLoadingDialog()
                    }
                }
            })
        }
        with(qrCodeViewModel){
            getLoggedInUserNameLiveData.observe(viewLifecycleOwner, {
                userName = it
            })
        }
    }

    private fun setupClickListeners(){
        with(binding){
            generateQRCodeBtn.setOnClickListener(this@QRCodeFragment)
            scanIV.setOnClickListener(this@QRCodeFragment)
            backIV.setOnClickListener(this@QRCodeFragment)
        }
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.save -> {
                saveQRCodeAsPDF()
            }

            R.id.share -> {
                shareQRCode()
            }

            R.id.generate_q_r_code_btn ->
                with(requireContext()){
                    with(binding){
                        val selectedWalletIndex = walletsSP.selectedItemPosition
                        val amount = amountET.text.toString()
                        val description = descriptionET.text.toString()

                        when {

                            selectedWalletIndex == 0 ->
                                showShortToast(getString(R.string.error_select_wallet))

                            amount.isEmpty() ->
                                showShortToast(getString(R.string.error_enter_amount))

                            description.isEmpty() ->
                                showShortToast(getString(R.string.error_enter_description))

                            else -> {
                                val accountNumber = wallets[selectedWalletIndex - 1].accountNo
                                generateTransactionQRCode(accountNumber, amount, description)
                            }

                        }
                    }
                }

            R.id.scan_IV -> scanQRCode()

            R.id.back_IV -> findNavController().popBackStack()

        }
    }

    private fun generateTransactionQRCode(accountNumber: String, amount: String, description: String){
        val transactionJsonObject = JSONObject()
        transactionJsonObject.put(TRANSACTION_ACCOUNT_NUMBER, accountNumber)
        transactionJsonObject.put(TRANSACTION_AMOUNT, amount.toInt())
        transactionJsonObject.put(TRANSACTION_DESCRIPTION, description)
        transactionJsonObject.put(TRANSACTION_USER_NAME, userName ?: "")

        val qrCodeBitmap = generateQRCode(transactionJsonObject.toString())

        if (qrCodeBitmap == null){
            requireContext().showShortToast(getString(R.string.default_error_message))
        }else{
            this.qrCodeBitmap = qrCodeBitmap
            with(binding){
                qRCodeIV.setImageBitmap(qrCodeBitmap)
                save.activate()
                share.activate()
                save.setOnClickListener(this@QRCodeFragment)
                share.setOnClickListener(this@QRCodeFragment)
            }
        }
    }

    private fun saveQRCodeAsPNG(){
        try {
            val path = getQRCodeJPGPath(requireContext())
            FileOutputStream(path).use { out ->
                qrCodeBitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        out
                ) // bmp is your Bitmap instance
            }
            requireContext().showShortToast("QR Code Saved")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveQRCodeAsPDF(){

        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(qrCodeBitmap.width, qrCodeBitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        val canvas: Canvas = page.canvas


        val paint = Paint()
        paint.color = Color.parseColor("#ffffff")
        canvas.drawPaint(paint)



        qrCodeBitmap = Bitmap.createScaledBitmap(qrCodeBitmap, qrCodeBitmap.width, qrCodeBitmap.height, true)

        paint.color = Color.BLUE
        canvas.drawBitmap(qrCodeBitmap, 0F, 0F, null)
        document.finishPage(page)


        // write the document content


        // write the document content
        val filePath = File(getQRCodePDFPath(requireContext()))
        try {
            document.writeTo(FileOutputStream(filePath))
            requireContext().showShortToast("QR Code saved")
        } catch (e: IOException) {
            e.printStackTrace()
            requireContext().showShortToast("Something went wrong")
        }

        // close the document

        // close the document
        document.close()
    }

    private fun shareQRCode(){
        val fileWithinMyDir: File = File(getQRCodePDFPath(requireContext()))
        if(!fileWithinMyDir.exists()){
            saveQRCodeAsPDF()
        }
        val intentShareFile = Intent(Intent.ACTION_SEND)
        val uri = FileProvider.getUriForFile(requireContext(), "com.wayapaychat", fileWithinMyDir)
        println(uri.path)
        if (fileWithinMyDir.exists()) {
            intentShareFile.type = "image/jpeg"
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Here's my Waya QR Code")
//            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
            startActivity(Intent.createChooser(intentShareFile, "Share Waya QR Code"))
        }
    }

    private fun scanQRCode(){
        val scanIntegrator = IntentIntegrator.forSupportFragment(this)
        scanIntegrator.setPrompt("Scan")
        scanIntegrator.setBeepEnabled(true)
        //The following line if you want QR code
        //The following line if you want QR code
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        scanIntegrator.captureActivity = CaptureActivityAnyOrientation::class.java
        scanIntegrator.setOrientationLocked(true)
        scanIntegrator.setBarcodeImageEnabled(false)
        scanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanningResult != null) {
            if (scanningResult.contents != null) {
                val scanContent = scanningResult.contents.toString()
                findNavController()
                    .navigate(
                        R.id.action_QRCodeFragment_to_confirmTransactionFragment,
                        Bundle().apply {
                            putString(ARG_TRANSACTION_DETAILS, scanContent)
                        }
                    )
            }
        } else {
            Toast.makeText(activity, "Nothing scanned", Toast.LENGTH_SHORT).show()
        }
    }

}
