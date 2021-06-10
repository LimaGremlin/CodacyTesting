package com.wayapay.ng.landingpage.pages

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wayapay.ng.landingpage.NewPageActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.databinding.FragmentCreatePageBinding
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.WayaPages

/**
 * A simple [Fragment] subclass.
 * Use the [CreatePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePageFragment : Fragment() {

    private lateinit var binding: FragmentCreatePageBinding
    private lateinit var viewModel: PagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_page, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PagesViewModel::class.java)
        viewModel.setButtonText(getString(R.string.continue_))

        viewModel.getListPageCategories().observe(requireActivity(), Observer { list ->
            // Create an ArrayAdapter using the string array and a default spinner layout
            val items = list.map { it.name }
            val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
            adapter.also {
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.categorySpinner.adapter = adapter
            }
        })

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewPageActivity).findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            if(check())
                findNavController().navigate(R.id.action_createPageFragment_to_createPageImagesFragment)
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    fun check():Boolean{

        binding.textView34.error = null
        val page = viewModel.getPages().value ?: WayaPages()

        if(TextUtils.isEmpty(binding.pageNameEditText.text.toString())){
            binding.pageNameEditText.error = ""
            binding.pageNameEditText.requestFocus()
            return false
        }else page.pageName = binding.pageNameEditText.text.toString()

        if(binding.categorySpinner.selectedItemPosition == 0){
            binding.textView34.error = ""
            binding.categorySpinner.requestFocus()
            return false
        }else {
            val category = viewModel.getCategory(binding.categorySpinner.selectedItemPosition)
            page.pageCategory = category.name
            page.categoryId = category.id
        }

        if(TextUtils.isEmpty(binding.descriptionEditText.text.toString())){
            binding.descriptionEditText.error = ""
            binding.descriptionEditText.requestFocus()
            return false
        }else page.description = binding.descriptionEditText.text.toString()

        page.websiteUrl = binding.websiteUrl.text.toString()
        page.isPublic = !binding.isPrivate.isChecked

        viewModel.setPages(page)

        return true
    }
}