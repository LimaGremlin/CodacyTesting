package com.wayapaychat.core.navigation

import javax.inject.Provider

typealias WayaAppIntentResolverMap = @JvmSuppressWildcards Map<Class<out WayaAppIntentKey>, Provider<WayaAppIntentResolver<*>>>
