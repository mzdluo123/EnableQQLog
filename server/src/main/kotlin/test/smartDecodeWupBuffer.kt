@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package test

import io.github.mzdluo123.enableqqlog.Color
import io.github.mzdluo123.enableqqlog.DataPack
import kotlinx.io.core.readBytes
import net.mamoe.mirai.internal.network.protocol.data.jce.RequestPacket
import net.mamoe.mirai.internal.utils.io.serialization.readJceStruct
import net.mamoe.mirai.utils.toReadPacket
import net.mamoe.mirai.utils.toUHexString
import net.mamoe.mirai.utils.withUse

fun smartDecodeWupBuffer(wupBuffer: ByteArray, dataPack: DataPack): String? = buildString {
    wupBuffer.toReadPacket().withUse {
        var firstRun = true
        while (isNotEmpty) {
            if (firstRun) {
                firstRun = false
            } else {
                appendLine()
            }

            val bytes = readBytes(readInt() - 4)

            val result = kotlin.runCatching {
                bytes.decodeUni(dataPack)
            }.getOrElse {
                bytes.toUHexString()
            } ?: return null

            appendLine(result)
        }
    }
}


private fun ByteArray.decodeUni(dataPack: DataPack): String? {
    return toReadPacket().withUse {
        val requestPacket = readJceStruct(RequestPacket.serializer())
        requestPacket.smartToString(dataPack)
    }
}

private fun RequestPacket.smartToString(dataPack: DataPack): String? = buildString {
    if (isIgnored(servantName)) return null
    if (isIgnored(funcName)) return null

    appendLine(Color.LIGHT_GREEN + "UNI servant=${servantName}  func=${funcName}" + Color.RESET)
    appendLine(kotlin.runCatching { sBuffer.uniSmartPrint(version?.toInt() ?: 3, this@smartToString, dataPack) }.getOrElse {
        it.printStackTrace()
        sBuffer.toUHexString()
    })
}