package com.wayapaychat.core.navigation

sealed class WayaAppIntentKey {

    class Authentication : WayaAppIntentKey()
    class Main : WayaAppIntentKey()
}
