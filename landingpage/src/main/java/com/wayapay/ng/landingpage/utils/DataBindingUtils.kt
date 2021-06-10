package com.wayapay.ng.landingpage.utils

import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.button.MaterialButton
import com.wayapay.ng.landingpage.R
import com.wayapay.ng.landingpage.models.LandingOptions
import com.wayapay.ng.landingpage.models.WayaEvent
import com.wayapaychat.core.utils.fadeIn
import com.wayapaychat.core.utils.fadeOut


@BindingAdapter("setEventUrl")
fun TextView.setEventUrl(event: WayaEvent?){
    if(TextUtils.isEmpty(event?.urlLink)) visibility = View.GONE
    else {
        text = event?.urlLink
        visibility = View.VISIBLE
    }
}

@BindingAdapter("disableDrawer")
fun disableDrawer(view: DrawerLayout, boolean: Boolean){
    if(boolean)view.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    else view.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
}

@BindingAdapter("setDateAtTime")
fun TextView.setDateAtTime(ts: Long?){
    text = String.format(
        context.getString(R.string.get_date_at_time_format),
        getShortDate(ts), getTime(ts)
    )
}

@BindingAdapter("organizedBy")
fun TextView.organizedBy(string: String?){
    text = String.format(context.getString(R.string.organized_by_format), string)
}

@BindingAdapter("interested")
fun TextView.interested(value: Int = 0){
    text = String.format(context.getString(R.string.interested_format), value)
}

@BindingAdapter("going")
fun TextView.going(value: Int = 0){
    text = String.format(context.getString(R.string.going_format), value)
}


@BindingAdapter("registeredCount")
fun TextView.eventRegisterCount(registered: Int?){
    text = String.format(context.getString(R.string.register_count_format), registered)
}

@BindingAdapter(value = ["isFree", "fee"], requireAll = true)
fun TextView.isFreeToAttend(isFree: Boolean = false, fee: Double){
    text = if(isFree) context.getString(R.string.free_to_attend)
    else "${context.getString(R.string.fee_)} $fee"
}

@BindingAdapter("username")
fun TextView.username(name: String?){
    text = if (TextUtils.isEmpty(name)) ""
    else "@$name"
}

@BindingAdapter(value = ["voteCount", "totalCount"], requireAll = true)
fun TextView.setVotePercent(voteCount: Int = 0, totalCount: Int){
    text = if(voteCount == 0) "0%"
    else "${voteCount * 100 / totalCount}%"
}

@BindingAdapter("followButtonText")
fun TextView.followButtonText(isFollowing: Boolean){
    text = if(isFollowing) context.getString(R.string.un_follow) else context.getString(R.string.follow)
}

@BindingAdapter(value = ["backButtonToggle", "profileImgUrl"], requireAll = false)
fun backButtonToggle(view: ImageView, isBackButton: Boolean, profileImgUrl: String?){
    if(isBackButton){
        Glide.with(view.context.applicationContext).load(R.drawable.ic_arrow_back).into(view)
        //ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f).setDuration(500).start()
        fadeOut(view, 1000)
        fadeIn(view, 2000)
    }else{
        if(!TextUtils.isEmpty(profileImgUrl)){
            Glide.with(view.context.applicationContext)
                    .load(profileImgUrl)
                    .placeholder(R.drawable.ic_person)
                    .signature(ObjectKey(System.currentTimeMillis()))
                    .apply(
                        RequestOptions.circleCropTransform()
                            //.placeholder(R.drawable.circle_default_background)
                            .error(com.wayapaychat.core.R.drawable.ic_person)
                    )
                    .into(view)
        }else
            Glide.with(view.context.applicationContext).load(R.drawable.ic_account_circle).into(view)
    }
}

@BindingAdapter("setOptions")
fun TextView.setOptions(tag: String?){
    text = when(tag){
        LandingOptions.MAKE_RESERVATIONS -> context.getString(R.string.make_reservations)
        LandingOptions.EDIT_EVENT -> context.getString(R.string.edit_event_info)
        LandingOptions.SHARE_EVENT -> context.getString(R.string.share)
        LandingOptions.REPORT_EVENT -> context.getString(R.string.report_event)
        else -> ""
    }
}

@BindingAdapter("donationCount")
fun TextView.donationCount(long: Long = 0){
    text = String.format(context.getString(R.string.donation_count_format), long)
}

@BindingAdapter("canViewDonation")
fun TextView.canViewDonations(boolean: Boolean){
    if(boolean){
        text = context.getString(R.string.total_donation_is_public)
        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_open, 0, 0, 0)
    }else{
        text = context.getString(R.string.total_donation_is_private)
        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0)
    }
}

@BindingAdapter(value = ["isOwner", "isFollowing"], requireAll = false)
fun Button.setProfileButton(isOwner: Boolean = false, isFollowing: Boolean = false){
    text = if(isOwner){
        context.getText(R.string.edit)
    }else {
        if(isFollowing) context.getString(R.string.un_follow)
        else context.getString(R.string.follow)
    }
}