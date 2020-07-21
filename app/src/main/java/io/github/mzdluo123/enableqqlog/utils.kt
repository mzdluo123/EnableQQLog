package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.XposedBridge

fun l(msg: String) {
    XposedBridge.log("[EnableQQLog] $msg")
}