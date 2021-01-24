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

        // pushLog(clazz.declaredMethods.joinToString("\n"))

        //// $FF: renamed from: b (byte[], int, int) byte[]
        //byte[] method_60801(byte[] var1, int var2, int var3) {


        // clazz.declaredMethods.filter { it.returnType == ByteArray::class.java }.map { it.name }.forEach { methodName ->
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                clazz,

                "b",
                ByteArray::class.java,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,

                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        pushLog("Oicq Send A: $param")
                        kotlin.runCatching {
                            val obj = param.thisObject

                            val data = (param.args[0] as ByteArray).dropLastWhile { it == 0.toByte() }.toByteArray()
                            LogUpload.upload(
                                Direction.OUT, "Oicq Send A", OicqHookOnMakePacket(
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
                    }
                }
            )
            //      }
        }.let {
            pushLog("Oicq Send A: $it")
        }

        // a (long, byte[], oicq.wlogin_sdk.request.oicq_request$EncryptionMethod) void
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                clazz,

                "a",
                Long::class.javaPrimitiveType,
                ByteArray::class.java,
                "oicq.wlogin_sdk.request.oicq_request\$EncryptionMethod",

                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        pushLog("Oicq Send B: $param")
                        kotlin.runCatching {
                            val obj = param.thisObject

                            val data = (param.args[1] as ByteArray).dropLastWhile { it == 0.toByte() }.toByteArray()
                            LogUpload.upload(
                                Direction.OUT, "Oicq Send B", OicqHookOnMakePacket(
                                    OicqRequest(
                                        cmd = getIntField(obj, "t"),
                                        subCmd = getIntField(obj, "u"),
                                        svcCmd = getObjectField(obj, "v") as String,
                                        sessionKey = getObjectField(obj, "c") as ByteArray
                                    ), data
                                ), PacketType.OICQ
                            )
                        }.onFailure {
                            pushLog(it.stackTraceToString())
                        }
                    }
                }
            )
            //      }
        }.let {
            pushLog("Oicq Send B: $it")
        }

        // a (byte[], byte[], byte[], byte[]) byte[]
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                clazz,

                "a",
                ByteArray::class.java,
                ByteArray::class.java,
                ByteArray::class.java,
                ByteArray::class.java,

                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        pushLog("Oicq Send C: $param")
                        kotlin.runCatching {
                            val obj = param.thisObject

                            val data = (param.args[0] as ByteArray).dropLastWhile { it == 0.toByte() }.toByteArray()
                            LogUpload.upload(
                                Direction.OUT, "Oicq Send C", OicqHookOnMakePacket(
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
                    }
                }
            )
            //      }
        }.let {
            pushLog("Oicq Send B: $it")
        }

        // a (byte[], byte[], byte[], byte[]) byte[]
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                clazz,

                "a",
                ByteArray::class.java,
                ByteArray::class.java,

                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        pushLog("Oicq Send D: $param")
                        kotlin.runCatching {
                            val obj = param.thisObject

                            val data = (param.args[0] as ByteArray).dropLastWhile { it == 0.toByte() }.toByteArray()
                            LogUpload.upload(
                                Direction.OUT, "Oicq Send D", OicqHookOnMakePacket(
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
                    }
                }
            )
            //      }
        }.let {
            pushLog("Oicq Send B: $it")
        }

        /*
public int oicq.wlogin_sdk.request.oicq_request.a(int)
public int oicq.wlogin_sdk.request.oicq_request.a(java.lang.String,boolean,oicq.wlogin_sdk.request.WUserSigInfo)
public int oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tlv_type.tlv_t161)
public int oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tlv_type.tlv_t173)
public int oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tlv_type.tlv_t17f)
public int oicq.wlogin_sdk.request.oicq_request.a(byte[],int,int,byte[])
public java.lang.String oicq.wlogin_sdk.request.oicq_request.a(int,boolean)
public java.lang.String oicq.wlogin_sdk.request.oicq_request.a(boolean)
public void oicq.wlogin_sdk.request.oicq_request.a()
public void oicq.wlogin_sdk.request.oicq_request.a(int,int,int,long,int,int,int,int,int)
public void oicq.wlogin_sdk.request.oicq_request.a(int,int,int,long,int,int,int,int,byte[])
public void oicq.wlogin_sdk.request.oicq_request.a(int,int,int,long,int,int,int,int,byte[],int)
void oicq.wlogin_sdk.request.oicq_request.a(int,byte[],int)
public void oicq.wlogin_sdk.request.oicq_request.a(long,byte[])
public void oicq.wlogin_sdk.request.oicq_request.a(long,byte[],oicq.wlogin_sdk.request.oicq_request$EncryptionMethod)
public void oicq.wlogin_sdk.request.oicq_request.a(java.net.Socket)
public void oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.sharemem.WloginSigInfo)
public void oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tlv_type.tlv_t149)
public void oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tools.ErrMsg)
public void oicq.wlogin_sdk.request.oicq_request.a(byte[],int)
public [B oicq.wlogin_sdk.request.oicq_request.a(oicq.wlogin_sdk.tlv_type.tlv_t169)
protected [B oicq.wlogin_sdk.request.oicq_request.a(byte[])
[B oicq.wlogin_sdk.request.oicq_request.a(byte[],int,int)
protected [B oicq.wlogin_sdk.request.oicq_request.a(byte[],oicq.wlogin_sdk.request.oicq_request$EncryptionMethod,byte[],byte[])
[B oicq.wlogin_sdk.request.oicq_request.a(byte[],byte[])
[B oicq.wlogin_sdk.request.oicq_request.a(byte[],byte[],byte[])
[B oicq.wlogin_sdk.request.oicq_request.a(byte[],byte[],byte[],byte[],int)
public int oicq.wlogin_sdk.request.oicq_request.b()
public int oicq.wlogin_sdk.request.oicq_request.b(java.lang.String,boolean,oicq.wlogin_sdk.request.WUserSigInfo)
public int oicq.wlogin_sdk.request.oicq_request.b(byte[])
public java.lang.String oicq.wlogin_sdk.request.oicq_request.b(boolean)
void oicq.wlogin_sdk.request.oicq_request.b(int,byte[],int)
public void oicq.wlogin_sdk.request.oicq_request.b(long,byte[])
public void oicq.wlogin_sdk.request.oicq_request.b(byte[],int)
[B oicq.wlogin_sdk.request.oicq_request.b(byte[],int,int)
protected [B oicq.wlogin_sdk.request.oicq_request.b(byte[],byte[],byte[])
public int oicq.wlogin_sdk.request.oicq_request.c(byte[],int)
public java.lang.String oicq.wlogin_sdk.request.oicq_request.c(boolean)
public void oicq.wlogin_sdk.request.oicq_request.c(byte[],int,int)
public [B oicq.wlogin_sdk.request.oicq_request.c()
public [B oicq.wlogin_sdk.request.oicq_request.c(byte[])
public [B oicq.wlogin_sdk.request.oicq_request.c(byte[],byte[],byte[])
public int oicq.wlogin_sdk.request.oicq_request.d(byte[],int,int)
public java.lang.String oicq.wlogin_sdk.request.oicq_request.d(boolean)
public java.net.Socket oicq.wlogin_sdk.request.oicq_request.d()
public int oicq.wlogin_sdk.request.oicq_request.e()
public int oicq.wlogin_sdk.request.oicq_request.e(boolean)
public int oicq.wlogin_sdk.request.oicq_request.f()
public void oicq.wlogin_sdk.request.oicq_request.g()
private void oicq.wlogin_sdk.request.oicq_request.a(int,int,long,int,int,int,int)
private void oicq.wlogin_sdk.request.oicq_request.a(int,int,long,int,int,int,int,int)
private void oicq.wlogin_sdk.request.oicq_request.a(int,int,long,int,int,int,byte[])
private void oicq.wlogin_sdk.request.oicq_request.a(int,long,int,int,boolean,boolean)
public static void oicq.wlogin_sdk.request.oicq_request.a(int,java.lang.String)
private void oicq.wlogin_sdk.request.oicq_request.b(int,int,long,int,int,int,int)
private void oicq.wlogin_sdk.request.oicq_request.b(int,int,long,int,int,int,byte[])
public static [B oicq.wlogin_sdk.request.oicq_request.b(byte[],byte[])
         */

        // clazz.declaredMethods.filter { it.returnType == Int::class.javaPrimitiveType }.map { it.name }.forEach { methodName ->
        kotlin.runCatching {
            XposedHelpers.findAndHookMethod(
                clazz,

                "d",
                ByteArray::class.java,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,

                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        pushLog("Oicq Recv A: $param")
                        kotlin.runCatching {
                            val obj = param.thisObject

                            val offset = param.args[1] as Int
                            val length = param.args[2] as Int

                            val data = (param.args[0] as ByteArray).sliceArray(offset until (offset + length))
                            LogUpload.upload(
                                Direction.IN, "Oicq Recv A", OicqHookOnMakePacket(
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
                    }
                }
            )
        }.let {
            pushLog("Oicq Recv A: $it")
        }
        // }


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