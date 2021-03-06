package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class UniPackage : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val uniClass = lpparam.classLoader.loadClass("com.qq.jce.wup.UniPacket")

        val pack = uniClass.getDeclaredField("_package")
        pack.isAccessible = true

        XposedHelpers.findAndHookMethod(uniClass, "encode", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val data = pack.get(param.thisObject)
                LogUpload.upload(Direction.OUT, "Uni", data, PacketType.UNI)

            }
        })

        val decodeHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val data = pack.get(param.thisObject)
                LogUpload.upload(Direction.IN, "Uni", data, PacketType.UNI)
            }
        }
        XposedHelpers.findAndHookMethod(uniClass, "decode", ByteArray::class.java, decodeHook)
        XposedHelpers.findAndHookMethod(
            uniClass,
            "decodeVersion3",
            ByteArray::class.java,
            decodeHook
        )
        XposedHelpers.findAndHookMethod(
            uniClass,
            "decodeVersion2",
            ByteArray::class.java,
            decodeHook
        )

        l("hook unipackage成功")
    }

}