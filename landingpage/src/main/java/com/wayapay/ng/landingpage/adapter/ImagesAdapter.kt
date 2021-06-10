package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListPostImagesBinding

class ImagesAdapter(private  val onImageAdapterClickListener: OnImageAdapterClickListener):RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    private var listImages = listOf<String>()

    interface OnImageAdapterClickListener{
        fun onImageClicked(position: Int)
    }

    fun getList():List<String> = listImages

    fun setList(list: List<String>){
        listImages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListPostImagesBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(listImages.isEmpty()) 0
        else 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    inner class MyViewHolder(private val binding: ListPostImagesBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){

            val list = getList()

            when(list.size){
                1 -> {
                    binding.imagesOneLayout.url1 = list[0]
                    binding.imagesOneLayout.removeVisibility = false
                    binding.imagesOneLayout.imageCount1.visibility = View.VISIBLE
                    binding.imagesTwoLayout.imageCount2.visibility = View.GONE
                    binding.imagesThreeLayout.imageCount3.visibility = View.GONE
                    binding.imagesFourLayout.imageCount4.visibility = View.GONE
                }
                2 -> {
                    binding.imagesTwoLayout.list = list
                    binding.imagesTwoLayout.removeVisibility = false
                    binding.imagesOneLayout.imageCount1.visibility = View.GONE
                    binding.imagesTwoLayout.imageCount2.visibility = View.VISIBLE
                    binding.imagesThreeLayout.imageCount3.visibility = View.GONE
                    binding.imagesFourLayout.imageCount4.visibility = View.GONE
                }
                3 -> {
                    binding.imagesThreeLayout.list = list
                    binding.imagesThreeLayout.removeVisibility = false
                    binding.imagesOneLayout.imageCount1.visibility = View.GONE
                    binding.imagesTwoLayout.imageCount2.visibility = View.GONE
                    binding.imagesThreeLayout.imageCount3.visibility = View.VISIBLE
                    binding.imagesFourLayout.imageCount4.visibility = View.GONE
                }
                4 -> {
                    binding.imagesFourLayout.list = list
                    binding.imagesFourLayout.removeVisibility = false
                    binding.imagesOneLayout.imageCount1.visibility = View.GONE
                    binding.imagesTwoLayout.imageCount2.visibility = View.GONE
                    binding.imagesThreeLayout.imageCount3.visibility = View.GONE
                    binding.imagesFourLayout.imageCount4.visibility = View.VISIBLE
                }
            }

            binding.root.setOnClickListener {
                onImageAdapterClickListener.onImageClicked(position)
            }
        }
    }
}