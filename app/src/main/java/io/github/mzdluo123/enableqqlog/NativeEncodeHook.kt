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

class NativeEncodeHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val codecWrapper = lpparam.classLoader.loadClass("com.tencent.qphone.base.util.CodecWarpper")

        LogUpload.log("codecWrapper=$codecWrapper")

        LogUpload.log(codecWrapper.methods.joinToString("\n"))

        // public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte[],byte[],byte[],boolean)
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapper,
                "nativeEncodeRequest",
                Int::class.java, String::class.java, String::class.java, String::class.java, String::class.java, String::class.java, ByteArray::class.java,
                Int::class.java, Int::class.java, String::class.java, Byte::class.java, Byte::class.java,
                ByteArray::class.java, ByteArray::class.java, ByteArray::class.java, Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "A: " + gson.toJson(param.args),
                            PacketType.LOG
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook A: $it")
        }

        // public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte[],boolean)
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapper,
                "nativeEncodeRequest",
                Int::class.java, String::class.java, String::class.java, String::class.java, String::class.java, String::class.java, ByteArray::class.java,
                Int::class.java, Int::class.java, String::class.java, Byte::class.java, Byte::class.java,
                ByteArray::class.java, Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "B: " + gson.toJson(param.args),
                            PacketType.LOG
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook B: $it")
        }

        // public static [B com.tencent.qphone.base.util.CodecWarpper.nativeEncodeRequest(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,byte[],int,int,java.lang.String,byte,byte,byte,byte[],byte[],byte[],boolean)
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapper,
                "nativeEncodeRequest",
                Int::class.java, String::class.java, String::class.java, String::class.java, String::class.java, String::class.java, ByteArray::class.java,
                Int::class.java, Int::class.java, String::class.java, Byte::class.java, Byte::class.java, Byte::class.java,
                ByteArray::class.java, ByteArray::class.java, ByteArray::class.java, Boolean::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "C: " + gson.toJson(param.args),
                            PacketType.LOG
                        )
                        LogUpload.upload(
                            Direction.OUT,
                            "Codec",
                            CodecNativeEncodePacket(
                                param.args[0].cast(),
                                param.args[1].cast(),
                                param.args[2].cast(),
                                param.args[3].cast(),
                                param.args[4].cast(),
                                param.args[5].cast(),
                                param.args[6].cast(),
                                param.args[7].cast(),
                                param.args[8].cast(),
                                param.args[9].cast(),
                                param.args[10].cast(),
                                param.args[11].cast(),
                                param.args[12].cast(),
                                param.args[13].cast(),
                                param.args[14].cast(),
                                param.args[15].cast(),
                                param.args[16].cast(),
                            ),
                            PacketType.CODEC_ENCODE
                        )
                    }

                    private inline fun <reified T> Any?.cast(): T {
                        return this as T
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook C: $it")
        }


        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapper,
                "nativeParseData",
                ByteArray::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val json = kotlin.runCatching {
                            gson.toJson(param.result)
                        }.getOrElse {
                            pushLog("Failed to decode Parse A: ${it.stackTraceToString()}")
                            return
                        }

                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "Parse A: $json",
                            PacketType.LOG
                        )

                        // Parse A: {"appId":-1,"appSeq":95706,"attributes":{},"errorMsg":"","extraData":{"mAllowFds":true,"mFdsKnown":true,"mHasFds":false,"mClassLoader":{"packages":{"com.android.org.conscrypt":{"implTitle":"Unknown","implVendor":"Unknown","implVersion":"0.0","name":"com.android.org.conscrypt","specTitle":"Unknown","specVendor":"Unknown","specVersion":"0.0"}},"proxyCache":{}},"mMap":{"version":1}},"flag":0,"fromVersion":1,"msfCommand":"unknown","msgCookie":[-1,78,-127,-28],"resultCode":1000,"serviceCmd":"VipCustom.GetCustomOnlineStatus","ssoSeq":-1,"uin":"xxxxxxxxxx","wupBuffer":[0,0,0,-125,0,0,0,127,16,3,44,60,66,84,77,62,-47,86,50,86,73,80,46,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,83,101,114,118,101,114,46,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,79,98,106,102,21,71,101,116,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,125,0,0,30,8,0,2,6,0,29,0,0,1,12,6,3,114,115,112,29,0,0,11,10,12,22,0,38,4,8,0,32,0,11,-116,-104,12,-88,12]}

                        LogUpload.upload(
                            Direction.IN,
                            "Codec",
                            json,
                            PacketType.CODEC_ENCODE
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook Parse A: $it")
        }

        // com.tencent.mobileqq.msf.core.MsfCore#addRespToQuque(java.lang.String, com.tencent.qphone.base.remote.ToServiceMsg, com.tencent.qphone.base.remote.FromServiceMsg)
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                lpparam.classLoader.loadClass("com.tencent.mobileqq.msf.core.MsfCore"),
                "addRespToQuque",
                String::class.java,
                "com.tencent.qphone.base.remote.ToServiceMsg",
                "com.tencent.qphone.base.remote.FromServiceMsg",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        // Parse A: {"appId":-1,"appSeq":95706,"attributes":{},"errorMsg":"","extraData":{"mAllowFds":true,"mFdsKnown":true,"mHasFds":false,"mClassLoader":{"packages":{"com.android.org.conscrypt":{"implTitle":"Unknown","implVendor":"Unknown","implVersion":"0.0","name":"com.android.org.conscrypt","specTitle":"Unknown","specVendor":"Unknown","specVersion":"0.0"}},"proxyCache":{}},"mMap":{"version":1}},"flag":0,"fromVersion":1,"msfCommand":"unknown","msgCookie":[-1,78,-127,-28],"resultCode":1000,"serviceCmd":"VipCustom.GetCustomOnlineStatus","ssoSeq":-1,"uin":"xxxxxxxxxx","wupBuffer":[0,0,0,-125,0,0,0,127,16,3,44,60,66,84,77,62,-47,86,50,86,73,80,46,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,83,101,114,118,101,114,46,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,79,98,106,102,21,71,101,116,67,117,115,116,111,109,79,110,108,105,110,101,83,116,97,116,117,115,125,0,0,30,8,0,2,6,0,29,0,0,1,12,6,3,114,115,112,29,0,0,11,10,12,22,0,38,4,8,0,32,0,11,-116,-104,12,-88,12]}

                        LogUpload.upload(
                            Direction.IN,
                            "Codec",
                            param.args[2],
                            PacketType.CODEC_ENCODE
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook Parse E: $it")
        }

        /*
        val fromServiceMsg = lpparam.classLoader.loadClass("com.tencent.qphone.base.remote.FromServiceMsg")
        val nativeParseData = codecWrapper.getMethod("nativeParseData", ByteArray::class.java)

        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapper,
                "nativeOnReceData",
                ByteArray::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "Parse B: " + gson.toJson(param.args),
                            PacketType.LOG
                        )

                        val data = param.args[0] as ByteArray
                        if (data[0] == 0.toByte() && data[1] == 0.toByte()) {
                            pushLog(kotlin.runCatching {
                                nativeParseData.invoke(param.thisObject, data)
                            }.toString())
                        }
                        LogUpload.upload(
                            Direction.IN,
                            "Codec",
                            data,
                            PacketType.CODEC_ENCODE
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook Parse B: $it")
        }
*/
        /*
        val codecWrapperImpl = kotlin.runCatching { lpparam.classLoader.loadClass("com.tencent.mobileqq.msf.core.ag\$a") }.getOrNull()

        pushLog("codecWrapperImpl=$codecWrapperImpl")
        if (codecWrapperImpl == null) return

        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapperImpl,
                "onResponse",
                Int::class.java,
                Any::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "Parse C: " + gson.toJson(param.args),
                            PacketType.LOG
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook Parse C: $it")
        }

        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                codecWrapperImpl,
                "onResponse",
                Int::class.java,
                Any::class.java,
                Int::class.java,
                ByteArray::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        LogUpload.upload(
                            Direction.OUT,
                            "LOG",
                            "Parse D: " + gson.toJson(param.args),
                            PacketType.LOG
                        )
                    }
                }
            )
        }.let {
            LogUpload.log("Codec Encode Hook Parse D: $it")
        }
*/
    }
}