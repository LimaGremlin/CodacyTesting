package com.wayapay.ng.landingpage.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.NewPostActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.ImagePickerAdapter
import com.wayapay.ng.landingpage.databinding.FragmentPostHomeBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.PostType
import com.wayapay.ng.landingpage.models.WayaPost

/**
 * A simple [Fragment] subclass.
 * Use the [PostHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostHomeFragment : Fragment(), ImagePickerAdapter.ImagePickerClickListener {

    private lateinit var binding: FragmentPostHomeBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var listImagePickerAdapter: ImagePickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_post_home, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PostViewModel::class.java)

        viewModel.setHeaderText(getString(R.string.post))

        listImagePickerAdapter = ImagePickerAdapter(this)
        val manager = LinearLayoutManager(requireContext().applicationContext, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = listImagePickerAdapter
        }
        viewModel.getListImageUrl().observe(requireActivity(), Observer {
            listImagePickerAdapter.setList(it)
        })

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewPostActivity).findViewById<Button>(R.id.post_next_button)
        nextButton.setOnClickListener {
            if(check()){
                val post = viewModel.getPost().value ?: WayaPost()
                val returnIntent = Intent()
                returnIntent.putExtra(IntentBundles.POST_KEY, post)
                requireActivity().setResult(Activity.RESULT_OK, returnIntent)

                requireActivity().finish()
            }
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun check():Boolean{
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val email = sharedPref.getString(getString(R.string.email_key), "") ?: ""
        val displayName = sharedPref.getString(getString(R.string.first_last_name_key), "") ?: ""
        val phoneNumber = sharedPref.getString(getString(R.string.phone_number_key), "") ?: ""
        val userId = sharedPref.getInt(getString(R.string.user_id_key), -1)

        val post = viewModel.getPost().value ?: WayaPost()
        val listImages = viewModel.getListImageUrl().value ?: mutableListOf()
        val listAbsolutePath = viewModel.getListAbsolutePath().value ?: mutableListOf()
        post.listAbsolutePath = listAbsolutePath
        post.listImageUri = listImages
        post.type = PostType.NORMAL
        post.userDisplayName = displayName
        post.userEmail = email
        post.userPhoneNumber = phoneNumber

        if(TextUtils.isEmpty(binding.postText.text.toString())){
            binding.postText.error = ""
            binding.postText.requestFocus()
            return false
        }else
            post.text = binding.postText.text.toString()

        //set post in viewModel
        viewModel.setPost(post)

        return true
    }

    override fun removeImage(position: Int) {
        val listImages = viewModel.getListImageUrl().value ?: mutableListOf()
        listImages.removeAt(position)
        viewModel.setListImageUrl(listImages)
    }
}