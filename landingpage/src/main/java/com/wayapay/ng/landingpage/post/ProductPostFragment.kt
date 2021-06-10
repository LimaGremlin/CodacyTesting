package com.wayapay.ng.landingpage.post

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wayapay.ng.landingpage.NewPostActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.ImagePickerAdapter
import com.wayapay.ng.landingpage.databinding.FragmentProductPostBinding
import com.wayapay.ng.landingpage.models.IntentBundles
import com.wayapay.ng.landingpage.models.LandingViewModelFactory
import com.wayapay.ng.landingpage.models.PostType
import com.wayapay.ng.landingpage.models.WayaPost
import com.wayapay.ng.landingpage.utils.toDouble

/**
 * A simple [Fragment] subclass.
 * Use the [ProductPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductPostFragment : Fragment(), ImagePickerAdapter.ImagePickerClickListener {

    private lateinit var binding: FragmentProductPostBinding
    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_product_post, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PostViewModel::class.java)

        viewModel.setHeaderText(getString(R.string.product_post))
        binding.textView46.movementMethod = LinkMovementMethod.getInstance()

        setListImages(mutableListOf())
        viewModel.getListImageUrl().observe(requireActivity(), Observer {
            //listImagePickerAdapter.setList(it)
            setListImages(it)
        })

        binding.pickImageButton.setOnClickListener {
            (requireActivity() as NewPostActivity).performFileSearch()
        }

        binding.productImages.imagesOneLayout.removeImageButton1.setOnClickListener {
            removeListAt(0)
        }
        binding.productImages.imagesTwoLayout.removeImageButton1.setOnClickListener {
            removeListAt(0)
        }
        binding.productImages.imagesThreeLayout.removeImageButton1.setOnClickListener {
            removeListAt(0)
        }
        binding.productImages.imagesFourLayout.removeImageButton1.setOnClickListener {
            removeListAt(0)
        }
        binding.productImages.imagesTwoLayout.removeImageButton2.setOnClickListener {
            removeListAt(1)
        }
        binding.productImages.imagesThreeLayout.removeImageButton2.setOnClickListener {
            removeListAt(1)
        }
        binding.productImages.imagesFourLayout.removeImageButton2.setOnClickListener {
            removeListAt(1)
        }
        binding.productImages.imagesThreeLayout.removeImageButton3.setOnClickListener {
            removeListAt(2)
        }
        binding.productImages.imagesFourLayout.removeImageButton3.setOnClickListener {
            removeListAt(2)
        }
        binding.productImages.imagesFourLayout.removeImageButton4.setOnClickListener {
            removeListAt(3)
        }

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewPostActivity).findViewById<Button>(R.id.post_next_button)
        nextButton.setOnClickListener {
            if(check()){
                val post = viewModel.getPost().value ?: WayaPost()
                buildAlertDialog(post)
            }
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun removeListAt(position: Int){
        val listImages = viewModel.getListImageUrl().value ?: mutableListOf()
        listImages.removeAt(position)
        viewModel.setListImageUrl(listImages)
    }

    private fun setListImages(list: MutableList<String>){
        when(list.size){
            0 -> {
                binding.imagePlaceHolder.visibility = View.VISIBLE
                binding.productImages.imagesOneLayout.imageCount1.visibility = View.GONE
                binding.productImages.imagesTwoLayout.imageCount2.visibility = View.GONE
                binding.productImages.imagesThreeLayout.imageCount3.visibility = View.GONE
                binding.productImages.imagesFourLayout.imageCount4.visibility = View.GONE
            }
            1 -> {
                binding.productImages.imagesOneLayout.url1 = list[0]
                binding.productImages.imagesOneLayout.removeVisibility = true
                binding.imagePlaceHolder.visibility = View.GONE
                binding.productImages.imagesOneLayout.imageCount1.visibility = View.VISIBLE
                binding.productImages.imagesTwoLayout.imageCount2.visibility = View.GONE
                binding.productImages.imagesThreeLayout.imageCount3.visibility = View.GONE
                binding.productImages.imagesFourLayout.imageCount4.visibility = View.GONE
            }
            2 -> {
                binding.productImages.imagesTwoLayout.list = list
                binding.productImages.imagesTwoLayout.removeVisibility = true
                binding.imagePlaceHolder.visibility = View.GONE
                binding.productImages.imagesOneLayout.imageCount1.visibility = View.GONE
                binding.productImages.imagesTwoLayout.imageCount2.visibility = View.VISIBLE
                binding.productImages.imagesThreeLayout.imageCount3.visibility = View.GONE
                binding.productImages.imagesFourLayout.imageCount4.visibility = View.GONE
            }
            3 -> {
                binding.productImages.imagesThreeLayout.list = list
                binding.productImages.imagesThreeLayout.removeVisibility = true
                binding.imagePlaceHolder.visibility = View.GONE
                binding.productImages.imagesOneLayout.imageCount1.visibility = View.GONE
                binding.productImages.imagesTwoLayout.imageCount2.visibility = View.GONE
                binding.productImages.imagesThreeLayout.imageCount3.visibility = View.VISIBLE
                binding.productImages.imagesFourLayout.imageCount4.visibility = View.GONE
            }
            4 -> {
                binding.productImages.imagesFourLayout.list = list
                binding.productImages.imagesFourLayout.removeVisibility = true
                binding.imagePlaceHolder.visibility = View.GONE
                binding.productImages.imagesOneLayout.imageCount1.visibility = View.GONE
                binding.productImages.imagesTwoLayout.imageCount2.visibility = View.GONE
                binding.productImages.imagesThreeLayout.imageCount3.visibility = View.GONE
                binding.productImages.imagesFourLayout.imageCount4.visibility = View.VISIBLE
            }
        }
    }

    private fun buildAlertDialog(post: WayaPost){
        //Build alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.payment_info))
        builder.setMessage(getString(R.string.product_payment_info))
        builder.setPositiveButton(getString(R.string.okay)){ _, _ ->
            val returnIntent = Intent()
            returnIntent.putExtra(IntentBundles.POST_KEY, post)
            requireActivity().setResult(Activity.RESULT_OK, returnIntent)

            requireActivity().finish()
        }
        builder.setNegativeButton(getString(R.string.close)) { dialog, _ ->
            //Dismiss dialog on cancel
            dialog.dismiss()
        }.show()
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
        post.type = PostType.PRODUCT
        post.userDisplayName = displayName
        post.userEmail = email
        post.userPhoneNumber = phoneNumber

        if(TextUtils.isEmpty(binding.postText.text.toString())){
            binding.postText.error = ""
            binding.postText.requestFocus()
            return false
        }else
            post.text = binding.postText.text.toString()

        if(listImages.size < 1){
            Toast.makeText(requireContext().applicationContext, getString(R.string.add_product_image), Toast.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(binding.productPrice.text.toString())){
            binding.productPrice.error = ""
            binding.productPrice.requestFocus()
            return  false
        }else
            post.price = binding.productPrice.toDouble()

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