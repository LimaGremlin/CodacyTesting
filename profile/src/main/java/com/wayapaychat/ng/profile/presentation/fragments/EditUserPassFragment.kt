package com.wayapaychat.ng.profile.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wayapaychat.core.utils.Helpers.isValidPassword
import com.wayapaychat.core.utils.Helpers.makeEditable
import com.wayapaychat.ng.profile.R
import com.wayapaychat.ng.profile.databinding.FragmentEditUserPassBinding

class EditUserPassFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditUserPassBinding
    private var valid = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_user_pass, container, false)
        setupListeners()
        watchTextChange()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditUserPassFragment()
    }

    private fun setupListeners() {
        binding.saveLayout.setOnClickListener(this)
        binding.plusIcon.setOnClickListener(this)
        binding.editUsername.setOnClickListener(this)
        binding.editPhone.setOnClickListener(this)
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
            binding.saveLayout -> {

                if (valid) {
                    val action = EditUserPassFragmentDirections.actionEditUserPassFragmentToProfileLandingFragment()
                    Navigation.findNavController(binding.saveLayout).navigate(action)
                } else {
                    Toast.makeText(this.requireContext(), getString(R.string.check_one_of_inputs),
                        Toast.LENGTH_LONG).show()
                }
            }

            binding.plusIcon -> {

            }

            binding.editUsername -> {
                binding.usernameContainer.makeEditable()
            }

            binding.editPhone -> {
                binding.phoneContainer.makeEditable()
            }
        }
    }
}
