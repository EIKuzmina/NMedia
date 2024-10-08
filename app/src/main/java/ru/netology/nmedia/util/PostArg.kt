package ru.netology.nmedia.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PostArg : ReadWriteProperty<Bundle, Int> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): Int
    = thisRef.getInt(property.name)

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Int) {
        thisRef.putInt(property.name, value)
    }
}