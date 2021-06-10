package com.wayapaychat.ng.payment.managecard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand
import com.wayapaychat.ng.payment.PaymentViewModelFactory
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.databinding.ActivityManageCardBinding
import com.wayapaychat.ng.payment.databinding.FragmentLiatBankCardsBinding
import com.wayapaychat.ng.payment.fragments.PaymentLandingFragmentDirections
import com.wayapaychat.ng.payment.managewallet.WalletAdapter
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class ListBankCardsFragment : Fragment(){
private lateinit var binding: FragmentLiatBankCardsBinding
    private lateinit var viewModel: CardsViewModel
    private lateinit var navController: NavController
    private lateinit var add_card: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val viewModelFactory = PaymentViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CardsViewModel::class.java)
        val view:View = inflater.inflate(R.layout.fragment_liat_bank_cards, container, false)
        add_card = view.findViewById(R.id.add_card)

        controls()
        return view
    }

     fun controls() {
         add_card.setOnClickListener {
             //navigate to wallet list here, then navigate to add card
Log.d("ADD_CARD", "add card clicked!")
             findNavController().navigate(R.id.action_listAllCards_to_chooseWalletFragment)

         }

       viewModel.getListCards().observe(
           requireActivity(), Observer {
               Log.d("CARD_SIZE", it.size.toString())
            it.size
           }
       )
    }
}