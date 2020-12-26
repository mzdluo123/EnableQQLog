package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getIntField
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

//oicq.wlogin_sdk.request.oicq_request
// $FF: renamed from: a (int, int, int, long, int, int, int, int, byte[]) void

class OicqRequestHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz =
            lpparam.classLoader.loadClass("oicq.wlogin_sdk.request.oicq_request")

        pushLog("11 oicq.wlogin_sdk.request.oicq_request")

        pushLog(clazz.methods.joinToString("\n"))

        val peekData = clazz.getMethod("c").apply { isAccessible = true }
        //     // $FF: renamed from: a (java.lang.String, boolean, oicq.wlogin_sdk.request.WUserSigInfo) int
        XposedHelpers.findAndHookMethod(
            clazz,
            "a",
            String::class.java,
            Boolean::class.javaPrimitiveType,
            "oicq.wlogin_sdk.request.WUserSigInfo",

            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    pushLog("hook ok")
                    kotlin.runCatching {

                        val obj = param.thisObject

                        val data = peekData.invoke(obj) as ByteArray
                        LogUpload.upload(
                            Direction.OUT, "Oicq", OicqHookOnMakePacket(
                                OicqRequest(
                                    cmd = getIntField(obj, "t"),
                                    subCmd = getIntField(obj, "u"),
                                    svcCmd = getObjectField(obj, "v") as String,
                                ), data
                            ), PacketType.OICQ
                        )
                    }.onFailure {
                        pushLog(it.stackTraceToString())
                    }
                    pushLog("upload ok")
                }
            }
        )

        /*
        pushLog(
            kotlin.runCatching {
                XposedHelpers.findAndHookMethod(
                    clazz,
                    "a",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Long::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    ByteArray::class.java,

                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            pushLog("hook ok")
                            val obj = gson.toJson(param.thisObject)
                            val data = param.args[8] as ByteArray
                            LogUpload.upload(Direction.OUT, "Oicq", OicqHookOnMakePacket(obj, data), PacketType.OICQ)
                            pushLog("upload ok")
                        }
                    }
                )
            }.toString()
        )*/
    }

}