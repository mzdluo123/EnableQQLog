package io.github.mzdluo123.enableqqlog

import com.google.gson.Gson
import de.robv.android.xposed.XposedBridge

inline fun l(msg: String) {
    XposedBridge.log("[EnableQQLog] $msg")
}

val gson = Gson()