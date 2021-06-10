package com.wayapay.ng.landingpage.group

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewGroupActivity
import com.wayapay.ng.landingpage.NewPageActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentCreateGroupBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaGroup

/**
 * A simple [Fragment] subclass.
 * Use the [CreateGroupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateGroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateGroupBinding
    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_group, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(GroupViewModel::class.java)
        viewModel.setButtonText(getString(R.string.continue_))

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewGroupActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            if(check())
                findNavController().navigate(R.id.action_createGroupFragment_to_createGroupImageFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    fun check():Boolean {

        val group = viewModel.getWayaGroup().value ?: WayaGroup()

        if(TextUtils.isEmpty(binding.groupName.text.toString())){
            binding.groupName.error = ""
            binding.groupName.requestFocus()
            return false
        }else group.groupName = binding.groupName.text.toString()

        if(TextUtils.isEmpty(binding.groupDescription.text.toString())){
            binding.groupDescription.error = ""
            binding.groupDescription.requestFocus()
            return false
        }else group.description = binding.groupDescription.text.toString()

        group.isPublic = !binding.privateCheckBox.isChecked

        viewModel.setWayaGroup(group)

        return true
    }
}