package com.wayapaychat.ng.di.keys

import com.wayapaychat.core.navigation.WayaAppIntentKey
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GokadaSuperAppIntentResolverKey (val value: KClass<out WayaAppIntentKey>)
