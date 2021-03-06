package com.wayapaychat.ng.di.keys

import com.wayapaychat.core.base.BaseViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GokadaSuperAppViewModelKey (val value: KClass<out BaseViewModel>)
