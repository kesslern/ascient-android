package us.kesslern.ascient

import kotlin.reflect.KClass

/**
 * Global utility functions
 */
fun tag(clazz: KClass<*>) = clazz.java.simpleName
