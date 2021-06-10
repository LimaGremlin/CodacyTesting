package com.wayapaychat.ng.auth.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wayapaychat.core.utils.Helpers
import com.wayapaychat.core.utils.Helpers.activate
import com.wayapaychat.core.utils.Helpers.deactivate
import com.wayapaychat.core.utils.Helpers.isShort
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.databinding.FragmentMerchantSecondSignupBinding

class MerchantSecondSignupFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentMerchantSecondSignupBinding
    private var isMerchant = true
    private var valid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_merchant_second_signup,
            container, false)
        isMerchant = true
        makeSelection(binding.merchantBtn)
        val text = binding.termCondition.text.toString()
        Helpers.makeClickableSpan({ openterms() },
            R.color.colorPrimary, false, binding.termCondition,
            text.length - 19, text.length, text)
        setViewListeners()
        watchTextChange()
        checkFormState()
        return binding.root
    }

    private fun setViewListeners() {
        binding.signUpBtn.actionBtn.setOnClickListener(this)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    fun openterms() {

    }


    fun selectButton(button: Button) {
        button.setTextColor(resources.getColor(R.color.black))
//        button.background = resources.getDrawable(R.drawable.button_plain_bg_white_background)
    }

    fun unSelectButton(button: Button) {
        button.setTextColor(resources.getColor(R.color.black))
//        button.background = resources.getDrawable(R.drawable.button_plain_bg_light_blue_background)
    }

    fun makeSelection(button: Button) {
        unSelectButton(binding.merchantBtn)
        unSelectButton(binding.userBtn)
        selectButton(button)
    }

    override fun onClick(p0: View?) {
        when(p0) {

            binding.signUpBtn.actionBtn -> {
//                val action = MerchantSecondSignupFragmentDirections.actionMerchantSecondSignupFragmentToPasswordSetupFragment()
//                Navigation.findNavController(binding.signUpBtn.actionBtn).navigate(action)
            }
        }
    }

    fun checkFormState(){
        if (!binding.firstnameBusinessName.text.toString().isShort() ||
            !binding.surname.text.toString().isShort() ||
            !binding.state.text.toString().isShort() ||
            !binding.city.text.toString().isShort() ||
            !binding.officeAddress.text.toString().isShort()) {
            binding.signUpBtn.actionBtn.deactivate()
            valid = false
        } else {
            valid = true
            binding.signUpBtn.actionBtn.activate()
        }
    }

    fun watchTextChange() {
        binding.firstnameBusinessName.addTextChangedListener {
            checkFormState()
            if (!it.toString().isShort()) {
                binding.firstnameBusinessName.error = "This should not be empty"
            }
        }

        binding.surname.addTextChangedListener {
            checkFormState()
            if (!it.toString().isShort()) {
                binding.surname.error = "This should not be empty"
            }
        }

        binding.state.addTextChangedListener {
            checkFormState()
            if (!it.toString().isShort()) {
                binding.state.error = "Not a valid input"
            }
        }

        binding.city.addTextChangedListener {
            checkFormState()
            if (!it.toString().isShort()) {
                binding.city.error = "Not a valid input"
            }
        }

        binding.officeAddress.addTextChangedListener {
            checkFormState()
            if (!it.toString().isShort()) {
                binding.officeAddress.error = "Not a valid input"
            }
        }
    }
}
