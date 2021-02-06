package io.github.mzdluo123.enableqqlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


/*
com.tencent.qphone.base.util.CodecWarpper

 private static synchronized native byte[] encodeRequest(
 int var0,  String var1,  String var2,   String var3,   String var4,   String var5,
  byte[] var6, int var7, int var8, String var9, byte var10, byte var11, byte var12, byte[] var13, byte[] var14, boolean var15);

 private static synchronized native byte[] encodeRequest(
 int var0, String var1, String var2, String var3, String var4, String var5, byte[] var6, int var7,
 int var8, String var9, byte var10, byte var11, byte[] wupbuffer, boolean var13);

 private static synchronized native byte[] encodeRequest(
 int var0, String var1, String var2, String var3, String var4, String var5, byte[] var6, int var7, int var8,
 String var9, byte var10, byte var11, byte[] var12, byte[] var13, boolean var14);

 */

/*
public static void com.tencent.qphone.base.util.CodecWarpper.checkSOVersion()
public boolean java.lang.Object.equals(java.lang.Object)
public static native int com.tencent.qphone.base.util.CodecWarpper.getAppid()
public final java.lang.Class java.lang.Object.getClass()
public static native [B com.tencent.qphone.base.util.CodecWarpper.getFileStoreKey()
public static native int com.tencent.qphone.base.util.CodecWarpper.getMaxPackageSize()
public static native long com.tencent.qphone.base.util.CodecWarpper.getPacketLossLength(int)
public static native int com.tencent.qphone.base.util.CodecWarpper.getSOVersion()
public static int com.tencent.qphone.base.util.CodecWarpper.getSharedObjectVersion()
public static native int com.tencent.qphone.base.util.CodecWarpper.getVersionCode()
public int java.lang.Object.hashCode()
public native void com.tencent.qphone.base.util.CodecWarpper.init(android.content.Context,boolean)
public void com.tencent.qphone.base.util.CodecWarpper.nativeClearReceData()
public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte[],byte[],byte[],boolean)
public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte[],boolean)
public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte,byte[],byte[],byte[],boolean)
public static void com.tencent.qphone.base.util.CodecWarpper.nativeOnConnClose()
public void com.tencent.qphone.base.util.CodecWarpper.nativeOnReceData(byte[],int)
public com.tencent.qphone.base.remote.FromServiceMsg com.tencent.qphone.base.util.CodecWarpper.nativeParseData(byte[])
public static void com.tencent.qphone.base.util.CodecWarpper.nativeRemoveAccountKey(java.lang.String)
public static void com.tencent.qphone.base.util.CodecWarpper.nativeSetAccountKey(java.lang.String,byte[],byte[],byte[],byte[],byte[],byte[],byte[],byte[],java.lang.String)
public static void com.tencent.qphone.base.util.CodecWarpper.nativeSetKsid(byte[])
public static void com.tencent.qphone.base.util.CodecWarpper.nativeSetUseSimpleHead(java.lang.String,boolean)
public final native void java.lang.Object.notify()
public final native void java.lang.Object.notifyAll()
public abstract void com.tencent.qphone.base.util.CodecWarpper.onInvalidData(int,int)
public void com.tencent.qphone.base.util.CodecWarpper.onInvalidDataNative(int)
public abstract void com.tencent.qphone.base.util.CodecWarpper.onInvalidSign()
public abstract void com.tencent.qphone.base.util.CodecWarpper.onResponse(int,java.lang.Object,int)
public abstract void com.tencent.qphone.base.util.CodecWarpper.onResponse(int,java.lang.Object,int,byte[])
public abstract int com.tencent.qphone.base.util.CodecWarpper.onSSOPingResponse(byte[],int)
public static void com.tencent.qphone.base.util.CodecWarpper.printBytes(java.lang.String,byte[],java.lang.StringBuilder)
public static native void com.tencent.qphone.base.util.CodecWarpper.setMaxPackageSize(int)
public java.lang.String java.lang.Object.toString()
public final native void java.lang.Object.wait() throws java.lang.InterruptedException
public final void java.lang.Object.wait(long) throws java.lang.InterruptedException
public final native void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
 */

class HighwayHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        return
        // com.tencent.mobileqq.highway.codec.TcpProtocolDataCodec#encodeC2SData
        val codec = lpparam.classLoader.loadClass("com.tencent.mobileqq.highway.codec.TcpProtocolDataCodec")

        // public byte[] encodeC2SData(EndPoint var1, HwRequest var2, byte[] var3) {
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codec,
                "encodeC2SData",
                "com.tencent.mobileqq.highway.utils.EndPoint",
                "com.tencent.mobileqq.highway.segment.HwRequest",
                ByteArray::class,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        pushLog("Highway Send A: " + gson.toJson(param.args))
                    }
                }
            )
        }.let {
            LogUpload.log("Highway Send A: $it")
        }
    }
}