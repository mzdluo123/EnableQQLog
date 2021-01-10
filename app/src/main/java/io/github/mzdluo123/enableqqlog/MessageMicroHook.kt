package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MessageMicroHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val msgMicro = lpparam.classLoader.loadClass("com.tencent.mobileqq.pb.MessageMicro")

        val dump = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                LogUpload.upload(
                    Direction.IN,
                    "MsgMicro (${param.thisObject.javaClass.name})",
                    param.thisObject,
                    PacketType.MSG_MICRO
                )
            }

        }
        XposedHelpers.findAndHookMethod(
            msgMicro,
            "readFrom",
            "com.tencent.mobileqq.pb.CodedInputStreamMicro",
            dump
        )
        XposedHelpers.findAndHookMethod(
            msgMicro,
            "readFromDirectly",
            "com.tencent.mobileqq.pb.CodedInputStreamMicro",
            dump
        )
        XposedHelpers.findAndHookMethod(
            msgMicro,
            "toByteArray",
            ByteArray::class.java,
            Int::class.java,
            Int::class.java,
            dump
        )

        XposedHelpers.findAndHookMethod(msgMicro, "toByteArray", dump)

        XposedHelpers.findAndHookMethod(
            msgMicro,
            "writeTo",
            "com.tencent.mobileqq.pb.CodedOutputStreamMicro",
            Int::class.java,
            dump
        )
        XposedHelpers.findAndHookMethod(
            msgMicro,
            "writeToDirectly",
            "com.tencent.mobileqq.pb.CodedOutputStreamMicro",
            Int::class.java,
            Object::class.java,
            dump
        )

        l("MessageMicro hook成功")
    }


}