package com.wayapay.ng.landingpage.post

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.NewPostActivity
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.adapter.ChoiceAdapter
import com.wayapay.ng.landingpage.databinding.FragmentVotePostBinding
import com.wayapay.ng.landingpage.models.*
import com.wayapay.ng.landingpage.utils.addDays
import com.wayapaychat.core.wayagram.Vote

/**
 * A simple [Fragment] subclass.
 * Use the [VotePostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VotePostFragment : Fragment(){

    private lateinit var binding: FragmentVotePostBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var choiceAdapter: ChoiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_vote_post, container, false)

        val viewModelFactory = LandingViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PostViewModel::class.java)

        viewModel.setHeaderText(getString(R.string.poll_post))
        binding.textView26.movementMethod = LinkMovementMethod.getInstance()

        choiceAdapter = ChoiceAdapter()
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.choiceRecyclerView.apply {
            layoutManager = manager
            adapter = choiceAdapter
        }
        choiceAdapter.setList(listOf())

        //reference next button from parent activity
        val nextButton = (requireActivity() as NewPostActivity).findViewById<Button>(R.id.post_next_button)
        nextButton.setOnClickListener {
            if(check()){
                val post = viewModel.getPost().value ?: WayaPost()
                buildAlertDialog(post)
            }
        }

        binding.addChoiceButton.setOnClickListener {
            if(choiceAdapter.itemCount < 5)
                choiceAdapter.addChoice(Vote(binding.choiceEditText.text.toString(), 0))
            binding.removeChoiceButton.visibility = View.VISIBLE
            binding.choiceEditText.setText("")
        }

        binding.removeChoiceButton.setOnClickListener {
            //Remove the last choice
            if(choiceAdapter.itemCount > 0) choiceAdapter.removeChoice()
            //hide button if choice count is less that 3
            if(choiceAdapter.itemCount == 0) binding.removeChoiceButton.visibility = View.GONE
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        //val items = listOf("Select Bank", "Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, (0..15).toList())
        adapter.also {
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.daysSpinner.adapter = adapter
        }

        ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, (0..23).toList())
            .also {
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.hoursSpinner.adapter = it
        }

        ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, (0..59).toList())
            .also {
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.minutesSpinner.adapter = it
            }

        binding.checkBox.isChecked = viewModel.getIsVotePaid().value ?: false
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setIsVotePaid(isChecked)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    private fun buildAlertDialog(post: WayaPost){
        //Build alert dialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.payment_info))
        builder.setMessage(getString(R.string.payment_info_description))
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

    fun check():Boolean{

        val post = viewModel.getPost().value ?: WayaPost()
        //set vote type
        post.type = PostType.VOTE

        if(TextUtils.isEmpty(binding.postText.text.toString())){
            binding.postText.error = ""
            binding.postText.requestFocus()
            return false
        }else
            post.text = binding.postText.text.toString()

        val listChoices = choiceAdapter.getListChoices()
        if(listChoices.size < 2){
            binding.choiceEditText.requestFocus()
            Toast.makeText(requireContext().applicationContext, getString(R.string.set_two_options), Toast.LENGTH_SHORT).show()
            return false
        }else {
            post.listVotes = listChoices.toMutableList()
            post.listOptions = listChoices.map { it.option }
        }

        if(binding.daysSpinner.selectedItemPosition == 0 && binding.hoursSpinner.selectedItemPosition == 0
            && binding.minutesSpinner.selectedItemPosition == 0){
            binding.textView24.error = ""
            binding.minutesSpinner.requestFocus()
            return false
        }else{
            val d = binding.daysSpinner.selectedItemPosition
            val h = binding.hoursSpinner.selectedItemPosition
            val m = binding.minutesSpinner.selectedItemPosition
            post.poolEndTime = addDays(System.currentTimeMillis(), 1)
        }

        val isPaid = viewModel.getIsVotePaid().value ?: false
        post.isPoolPaid = isPaid

        if(TextUtils.isEmpty(binding.votesPerUser.text.toString())){
            binding.votesPerUser.error = ""
            binding.votesPerUser.requestFocus()
            return false
        }else post.voteLimit = binding.votesPerUser.text.toString().toIntOrNull() ?: 1

        if(isPaid && TextUtils.isEmpty(binding.priceToVote.text.toString())){
            binding.priceToVote.error = ""
            binding.priceToVote.requestFocus()
            return false
        }else{
            val cost = binding.priceToVote.text.toString().toDoubleOrNull() ?: 0.0
            post.price = cost
        }

        viewModel.setPost(post)

        return true
    }
}