package com.wayapay.profile.qrcode

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wayapay.profile.qrcode.databinding.ActivityQrProfileMainBinding
import com.wayapay.profile.qrcode.main.QrProfileViewModel

class QrProfileMainActivity : AppCompatActivity() {

    private lateinit var viewModel: QrProfileViewModel
    private lateinit var binding: ActivityQrProfileMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_profile_main)

        val viewModelFactory = QrProfileViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(QrProfileViewModel::class.java)

        val sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getInt(getString(R.string.user_id_key), -1)
        viewModel.getGramUserAsync(id.toString())

        binding.navButton.setOnClickListener {
            onBackPressed()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}