package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.utils.Helpers.hide
import com.wayapaychat.core.utils.Helpers.isValidPassword
import com.wayapaychat.core.utils.Helpers.reveal
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentEditPasswordBinding

class EditPasswordFragment : Fragment(), View.OnClickListener {

    private val binding: FragmentEditPasswordBinding by lazy {
        FragmentEditPasswordBinding.inflate(layoutInflater)
    }

    private var valid = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setupListeners()
        watchTextChange()
        binding.top.topText.text = "Edit your password"
        binding.top.topText.reveal()
        binding.top.pageNumber.hide()
        return binding.root
    }

    private fun setupListeners() {
        binding.saveLayout.container.setOnClickListener(this)
        binding.top.back.setOnClickListener(this)
    }

    private fun watchTextChange() {
        binding.prevPassword.addTextChangedListener {
            if (!it.toString().isValidPassword()) {
                valid = false
                binding.prevPassword.error = this.requireContext()
                    .resources.getString(R.string.explanation)
            } else {
                valid = true
            }
        }

        binding.newPassword.addTextChangedListener {
            if (!it.toString().isValidPassword()) {
                valid = false
                binding.newPassword.error = this.requireContext()
                    .resources.getString(R.string.explanation)
            } else {
                valid = true
            }
        }

        binding.confirmNewPassword.addTextChangedListener {
            if (!it.toString().isValidPassword()) {
                valid = false
                binding.confirmNewPassword.error = this.requireContext()
                    .resources.getString(R.string.explanation)
            } else {
                valid = true
            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.saveLayout.container -> {
                if (valid) {
                    val action =
                        EditUserPassFragmentDirections.actionEditUserPassFragmentToProfileLandingFragment()
                    Navigation.findNavController(binding.saveLayout.container).navigate(action)
                } else {
                    Toast.makeText(
                        this.requireContext(), getString(R.string.check_one_of_inputs),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding.top.back -> {
                findNavController().popBackStack()
            }
        }
    }
}
