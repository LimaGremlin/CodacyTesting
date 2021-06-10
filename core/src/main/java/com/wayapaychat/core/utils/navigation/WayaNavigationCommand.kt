package com.wayapaychat.core.utils.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavDirections

sealed class WayaNavigationCommand {
    data class To(val direction: NavDirections): WayaNavigationCommand()
    object Back: WayaNavigationCommand()
    data class BackTo(@IdRes val destinationId: Int): WayaNavigationCommand()
    object BackToRoot: WayaNavigationCommand()
}
