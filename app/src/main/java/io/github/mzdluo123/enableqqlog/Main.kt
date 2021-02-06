package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Main : IXposedHookLoadPackage {
    companion object {
        lateinit var classLoader: ClassLoader
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.tencent.tim" && lpparam.packageName != "com.tencent.mobileqq") {
            return
        }
        classLoader = lpparam.classLoader
        // val QLog = lpparam.classLoader.loadClass("com.tencent.qphone.base.util.QLog")
        // QLog.getDeclaredField("UIN_REPORTLOG_LEVEL").also {
        //     it.isAccessible = true
        //     it.setInt(QLog.newInstance(), 4)
        // }
        // l("hook 打印log成功")

        // UniPackage().handleLoadPackage(lpparam)
        // ServiceHook().handleLoadPackage(lpparam)
        // MessageMicroHook().handleLoadPackage(lpparam)
        OicqRequestHook().handleLoadPackage(lpparam)

        NativeEncodeHook().handleLoadPackage(lpparam)
        HighwayHook().handleLoadPackage(lpparam)
    }
}