package com.wayapaychat.core.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator

fun slideUp(view:View){
    // Prepare the View for the animation
    view.visibility = View.VISIBLE
    view.alpha = 0.0f

// Start the animation
    view.animate()
        .translationY(0F)
        .alpha(1.0f)
        .setListener(null)
}

fun slideInLeft(view: View){
    view.visibility = View.VISIBLE
    view.alpha = 0.0f

    view.animate()
        .translationX(0F)
        .alpha(1.0f)
        .setListener(null)
}

/**
 * Slide a view down
 */
fun slideDown(view:View){
    view.animate()
        .translationY(view.height.toFloat())
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
}

fun slideDownSlideUp(view: View){
    view.animate()
        .translationY(view.height.toFloat())
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE

                // Prepare the View for slide up animation
                view.visibility = View.VISIBLE
                view.alpha = 0.0f

                // Start slide up animation
                view.animate()
                    .translationY(0F)
                    .alpha(1.0f)
                    .setListener(null)
            }
        })
}

fun slideOutRight(view:View){
    if(view.visibility == View.GONE) return
    view.animate()
        .translationX(view.width.toFloat())
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
}

fun fadeIn(view: View, duration: Long){
    view.visibility = View.VISIBLE

    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator() //add this
    fadeIn.duration = duration

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeIn)
    view.animation = animation
}

fun fadeOut(view: View, duration: Long){
    //hide view
    view.visibility = View.GONE

    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.startOffset = 1000
    fadeOut.duration = duration

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeOut)
    view.animation = animation
}

fun slideUpHide(view: View){
    // Prepare the View for the animation
    view.alpha = 0.0f

    // Start the animation
    view.animate()
        .translationY(0F)
        .alpha(1.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
}

fun slideDownShow(view:View){
    view.visibility = View.VISIBLE
    view.animate()
        .translationY(view.height.toFloat())
        .alpha(0.0f)
        .setListener(null)
}