package test

import com.google.gson.Gson
import io.github.mzdluo123.enableqqlog.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.mamoe.mirai.utils.*


private val processQueue = Channel<DataPack>(Channel.BUFFERED)
private val init by lazy {
    GlobalScope.launch {
        processQueue.receiveAsFlow().collect { dataPack ->
            dataPack.prettyPrint()?.let(::println)
        }
    }
}

fun Application.module() {
    init
    routing {
        post("/") {
            processQueue.send(call.receivePack())
        }
    }
}

private fun DataPack.prettyPrint(): String? = buildString {
    append(direction.prettyString())
    append("  ")
    append(type)
    appendLine()
    val content = contentPrint() ?: return null
    append(content.trim())
    appendLine()
}


class UniPacket(
    val cPacketType: Int,
    val iMessageType: Int,
    val iRequestId: Int,
    val iTimeout: Int,
    val iVersion: Int,
    val sBuffer: ByteArray,
    val sFuncName: String,
    val sServantName: String,
)

class SvcPacket(
    val serviceCmd: String,
    val wupBuffer: ByteArray,
)


@Suppress("SpellCheckingInspection")
internal val IgnoredPackets = arrayOf(
    "",
    "socketnetflow",
    "CliLogSvc.UploadReq",
    "QQService.CliLogSvc.MainServantObj",
    "QQService.ConfigPushSvc.MainServant",
    "KQQ.ConfigService.ConfigServantObj",
    "App_reportRDM",
    "RedTouchSvc.ClientReport",
    "MobileReport.UserActionReport",
    "JsApiSvr.webview.whitelist",
    "ConfigServantObj",
    "cmd_getServerConfig",
    "cmd_RegisterMsfService",
    "CameraModuleSvc",
    "Pay",
    "Game",
    "TianShu.GetAds"
)

internal val IgnoredPacketFilters: Array<(String) -> Boolean> = arrayOf(
    { it.contains("qzone", ignoreCase = true) }
)

fun isIgnored(s: String?): Boolean = s == null || IgnoredPackets.contains(s) || IgnoredPacketFilters.any { it.invoke(s) }

private fun DataPack.contentPrint(): String? = buildString {
    when (packetType) {
        PacketType.UNI -> {
            /*
            {cPacketType=0.0, iMessageType=0.0, iRequestId=0.0, iTimeout=0.0, iVersion=3.0,
             sBuffer=[8.0, 0.0, 1.0, 6.0, 8.0, 80.0, 117.0, 115.0, 104.0, 82.0, 101.0, 115.0, 112.0, 29.0, 0.0, 0.0, 9.0, 10.0, 16.0, 2.0, 34.0, 108.0, -116.0, -122.0, -63.0, 11.0],
             sFuncName=PushResp, sServantName=QQService.ConfigPushSvc.MainServant}
             */
            @Suppress("UNCHECKED_CAST")
            val content = Gson().fromJson(contentJson, UniPacket::class.java)

            if (isIgnored(content.sServantName)) return null
            if (isIgnored(content.sFuncName)) return null

            appendLine(Color.LIGHT_GREEN + "servant=${content.sServantName}  func=${content.sFuncName}" + Color.RESET)
            appendLine(kotlin.runCatching { content.sBuffer.uniSmartPrint(content.iVersion) }.getOrElse { content.sBuffer.toUHexString() })
        }

        PacketType.OICQ -> {
            val data = Gson().fromJson(contentJson, OicqHookOnMakePacket::class.java)

            if (isIgnored(data.request.svcCmd)) return null

            appendLine(Color.LIGHT_GREEN + data.request.toString() + Color.RESET)
            appendLine(data.data.toUHexString())
            appendLine()
            appendLine(data.data.toReadPacket().withUse { _readTLVMap() }.smartToString())
        }

        PacketType.SVC -> {
            val svcPacket = Gson().fromJson(contentJson, SvcPacket::class.java)

            // {"appId":537066758,
            //"appSeq":1,"attributes":{"fastresend":false,"to_SendTime":1608978220122,"to_SenderProcessName":"com.tencent.mobileqq"},
            //"extraData":{"mAllowFds":true,"mFdsKnown":true,"mHasFds":false,
            //  "mClassLoader":
            //  {"packages":
            //      {"com.android.org.conscrypt":
            //          {"implTitle":"Unknown","implVendor":"Unknown","implVersion":"0.0","name":"com.android.org.conscrypt","specTitle":"Unknown","specVendor":"Unknown","specVersion":"0.0"}
            //      },"proxyCache":{}
            //  },
            //  "mMap":{"version":1,"sendTime":1608978214231}
            //},"mIsSupportRetry":false,"mSkipBinderWhenMarshall":false,"msfCommand":"unknown","needResp":false,"quickSendEnable":false,"quickSendStrategy":0,"sendTimeout":-1,
            //"serviceCmd":"CliLogSvc.UploadReq",
            //"serviceName":"com.tencent.mobileqq.msf.service.MsfService",
            //"ssoSeq":-1,"timeout":30000,"toVersion":1,
            //"uin":"1994701021","uinType":0,
            //"wupBuffer":[0,0,1,30,16,3,44,60,76,86,34,81,81,83,101,114,118,105,99,101,46,67,108,105,76,111,103,83,118,99,46,77,97,105,110,83,101,114,118,97,110,116,79,98,106,102,9,85,112,108,111,97,100,82,101,113,125,0,1,0,-36,8,0,1,6,4,68,97,116,97,29,0,1,0,-50,10,8,0,1,6,7,100,99,48,52,50,55,50,25,0,1,13,0,1,0,-75,49,54,48,56,57,55,56,50,49,51,124,53,51,55,48,54,54,55,53,56,124,97,110,100,114,111,105,100,124,49,57,57,52,55,48,49,48,50,49,124,110,101,119,95,114,101,103,95,56,48,53,124,108,111,103,95,112,97,103,101,124,112,97,103,101,95,101,120,112,124,124,49,124,124,56,54,53,49,54,54,48,50,50,56,56,48,48,56,53,124,124,79,80,80,79,124,80,67,82,84,48,48,124,78,79,78,69,124,53,46,49,46,49,124,63,59,76,77,89,52,57,73,59,97,110,100,114,111,105,100,95,120,56,54,45,117,115,101,114,32,53,46,49,46,49,32,76,77,89,52,57,73,32,56,46,51,46,49,57,32,114,101,108,101,97,115,101,45,107,101,121,115,124,124,124,124,124,124,124,124,124,22,0,44,11,-116,-104,12,-88,12]}

            if (isIgnored(svcPacket.serviceCmd)) return null

            appendLine(Color.LIGHT_GREEN + "cmd=${svcPacket.serviceCmd}" + Color.RESET)
            appendLine(svcPacket.wupBuffer.toUHexString())
        }

        PacketType.LOG -> {
            val content = Gson().fromJson(contentJson, String::class.java)
            appendLine(content)
        }

        PacketType.CODEC_ENCODE -> {
            when (direction) {
                Direction.IN -> {
                    val packet = Gson().fromJson(contentJson, FromServiceMsg::class.java)
                    if (isIgnored(packet.serviceCmd)) return null
                    appendLine(Color.LIGHT_GREEN + "cmd=${packet.serviceCmd}" + Color.RESET)
                    appendLine(smartDecodeWupBuffer(packet.wupBuffer) ?: return null)
                }
                Direction.OUT -> {
                    val packet = Gson().fromJson(contentJson, CodecNativeEncodePacket::class.java)
                    if (isIgnored(packet.commandId)) return null
                    appendLine(Color.LIGHT_GREEN + "cmd=${packet.commandId}" + Color.RESET)
                    appendLine(smartDecodeWupBuffer(packet.wupBuffer) ?: return null)
                }
            }
        }
        else -> append(contentJson)
    }
}

private suspend fun ApplicationCall.receivePack(): DataPack {
    return receive<String>().let { Gson().fromJson(it, DataPack::class.java) }
}

private val Char.isHumanReadable get() = this in '0'..'9' || this in 'a'..'z' || this in 'A'..'Z' || this in """ <>?,.";':/\][{}~!@#$%^&*()_+-=`""" || this in "\n\r"

private fun TlvMap.smartToString(): String {
    fun ByteArray.valueToString(): String {
        val str = this.encodeToString()
        return if (str.all { it.isHumanReadable }) str
        else this.toUHexString()
    }

    return entries.joinToString("\n") { (key, value) ->
        "0x" + key.toShort().toUHexString("") + " = " + value.valueToString()
    }
}