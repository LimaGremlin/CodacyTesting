package com.wayapaychat.ng.payment.managecard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wayapaychat.domain.models.payment.UserBankCard
import com.wayapaychat.domain.models.payment.UserBankCardDetails
import com.wayapaychat.ng.payment.R

//
class CardsAdapter(val localData: List<UserBankCardDetails> = arrayListOf()) :
    RecyclerView.Adapter<CardsAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: UserBankCardDetails) {

            itemView.findViewById<TextView>(R.id.expiry_date).text = data.cardNumber
            itemView.findViewById<TextView>(R.id.last_four).text = data.accountName
            var cardTypeImage = data.type
            when (cardTypeImage) {
                "visa" ->
                    itemView.findViewById<ImageView>(R.id.card_image).setImageResource(R.drawable.visa)
                "verve" ->
                    itemView.findViewById<ImageView>(R.id.card_image).setImageResource(R.drawable.ic_verve)
                "master" ->
                    itemView.findViewById<ImageView>(R.id.card_image).setImageResource(R.drawable.mastercard)
            }
            itemView.setOnClickListener {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_card, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int = localData.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) =
        //holder.bind(localData.filter{localData[position].thread_id}
        holder.bind(localData[position])


}