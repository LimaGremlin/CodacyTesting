package com.wayapaychat.ng.onboarding.utils

import android.content.Context
import com.wayapaychat.core.models.Page
import com.wayapaychat.ng.onboarding.R

fun getListPages(context: Context): List<Page>{
    return listOf(
        Page(R.drawable.ic_connect, context.getString(R.string.connect),context.getString(R.string.connect_description)),
        Page(R.drawable.ic_discover, context.getString(R.string.discover), context.getString(R.string.discover_description)),
        Page(R.drawable.ic_transact,  context.getString(R.string.transact), context.getString(R.string.manage_description)),
        Page(R.drawable.ic_transact,  context.getString(R.string.transact), context.getString(R.string.manage_description))
    )
}
