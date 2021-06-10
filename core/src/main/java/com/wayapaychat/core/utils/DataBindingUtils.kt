package com.wayapaychat.core.utils

import android.text.TextUtils
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.wayapaychat.core.R

@BindingAdapter(value = ["imgUrl", "imgUri"], requireAll = false)
fun setEventHeaderImage(view: ImageView, imgUrl:String?, imgUri:String?){
    if (!TextUtils.isEmpty(imgUri))
        Glide.with(view.context.applicationContext)
            .load(imgUri?.toUri())
            .transform(CenterCrop(), RoundedCorners(24))
            .placeholder(R.drawable.shape_image_place_holder)
            .signature(ObjectKey(System.currentTimeMillis()))
            .error(R.drawable.shape_image_place_holder)
            .into(view)
    else if(!TextUtils.isEmpty(imgUrl)){
        Glide.with(view.context.applicationContext)
                .load(imgUrl)
            .transform(CenterCrop(), RoundedCorners(24))
                .placeholder(R.drawable.shape_image_place_holder)
                .signature(ObjectKey(System.currentTimeMillis()))
                .error(R.drawable.shape_image_place_holder)
                .into(view)
    }
}

@BindingAdapter(value = ["profileImgUrl", "profileImgUri"], requireAll = false)
fun setProfileImage(view: ImageView, profileImgUrl:String?, profileImgUri:String?){
    if(!TextUtils.isEmpty(profileImgUrl)){
        Glide.with(view.context.applicationContext)
                .load(profileImgUrl)
                .placeholder(R.drawable.ic_person)
                .signature(ObjectKey(System.currentTimeMillis()))
                .apply(
                        RequestOptions.circleCropTransform()
                                //.placeholder(R.drawable.circle_default_background)
                                .error(R.drawable.ic_person)
                )
                .into(view)
    }
    else if (!TextUtils.isEmpty(profileImgUri))
        Glide.with(view.context.applicationContext)
                .load(profileImgUri?.toUri())
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(view)
}

@BindingAdapter("avatar")
fun avatar(view: ImageView, avatar: String?){
    Glide.with(view.context.applicationContext)
        .load(avatar)
        .placeholder(R.drawable.ic_person)
        .signature(ObjectKey(System.currentTimeMillis()))
        .apply(
            RequestOptions.circleCropTransform()
                //.placeholder(R.drawable.circle_default_background)
                .error(R.drawable.ic_person)
        )
        .into(view)
}