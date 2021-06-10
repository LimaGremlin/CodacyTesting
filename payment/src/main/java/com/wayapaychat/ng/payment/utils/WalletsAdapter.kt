package com.wayapaychat.ng.payment.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.textview.MaterialTextView
import com.wayapaychat.ng.payment.R
import com.wayapaychat.ng.payment.fragments.bottom_sheet.TopupDialogFragment
import com.wayapaychat.ng.payment.model.Wallet

class WalletsAdapter(var context: Context, var activity: Activity, var fragmentManager: FragmentManager)
    : PagerAdapter() {
    private var walletList: ArrayList<Wallet> = ArrayList()

    fun setDataList(list: ArrayList<Wallet>) {
        this.walletList = list
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, dObject: Any): Boolean {
        return view == dObject
    }

    override fun getCount(): Int {
        return walletList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val wallet = walletList[position]
        val view = LayoutInflater.from(this.context).inflate(R.layout.wallet_item, container, false)
        view.findViewById<MaterialTextView>(R.id.balance_text)
        val walletBalance = view.findViewById<MaterialTextView>(R.id.balance)
        val walletAccountNumber = view.findViewById<MaterialTextView>(R.id.account_number)
        val fundWallet = view.findViewById<MaterialTextView>(R.id.fund_wallet)
        val switch = view.findViewById<SwitchCompat>(R.id.default_wallet_switch)
        walletAccountNumber.text = wallet.accountNo
        walletBalance.text = wallet.balance.toString()
        if(wallet.accountName.equals("Default Wallet")){
            switch.setChecked(true);
            switch.setClickable(false);
        }else{
            switch.setChecked(false);
            switch.setClickable(true);
        }
        container.addView(view)
//        getChildFragmentManager()
        fundWallet.setOnClickListener {
            val topupDialogFragment = TopupDialogFragment(activity)
            topupDialogFragment.show(fragmentManager, "Dialog")
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, dObject: Any) {
        container.removeView(dObject as View?)
    }
}
