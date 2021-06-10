package com.wayapay.ng.landingpage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wayapay.ng.landingpage.databinding.ListImagePickerBinding

class ImagePickerAdapter(private val imagePickerClickListener: ImagePickerClickListener): RecyclerView.Adapter<ImagePickerAdapter.MyViewHolder>() {

    interface ImagePickerClickListener{
        fun removeImage(position: Int)
    }

    private var listImages = listOf<String>()

    fun getImage(position: Int) = listImages[position]

    fun setList(list: List<String>){
        listImages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListImagePickerBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listImages.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    inner class MyViewHolder(private val binding: ListImagePickerBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            val imageUri = getImage(position)

            binding.uri = imageUri

            binding.removeImageButton.setOnClickListener {
                imagePickerClickListener.removeImage(position)
            }
        }
    }
}