package io.github.mzdluo123.enableqqlog

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Field

inline fun l(msg: String) {
    XposedBridge.log("[EnableQQLog] $msg")
}

val gson by lazy {
    val builder = GsonBuilder()
    builder.setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            if (clazz == Field::class.java) {
                return true
            }
            if (clazz == Class::class.java) {
                return true
            }
            return false
        }

        override fun shouldSkipField(f: FieldAttributes): Boolean {
            if (f.name == "_fields") {
                return true
            }
            return false
        }

    })

    builder.create()
}