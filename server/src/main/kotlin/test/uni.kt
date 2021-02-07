@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package test

import io.github.mzdluo123.enableqqlog.DataPack
import io.github.mzdluo123.enableqqlog.Direction
import net.mamoe.mirai.internal.network.protocol.data.jce.*
import net.mamoe.mirai.internal.utils._miraiContentToString
import net.mamoe.mirai.internal.utils.io.serialization.loadAs
import net.mamoe.mirai.utils.toUHexString

internal fun ByteArray.uniSmartPrint(version: Int, request: RequestPacket, dataPack: DataPack): String = buildString {
    when (version) {
        2 -> {
            val map = loadAs(RequestDataVersion2.serializer()).map
            map.entries.forEach { (key, value) ->
                value.entries
                    .map { refineDataIfNeeded(it, dataPack) }
                    .forEach { (key1, value1) ->
                        appendLine("v2  $key  $key1")
                        appendLine(value1.toUHexString())
                        val decode = dataPack.uniSmartDecode(request.servantName, request.funcName, key1, value1)
                        if (decode != null) appendLine(decode)
                    }
            }
        }
        3 -> {
            val value = loadAs(RequestDataVersion3.serializer()).map
            value.entries
                .map { refineDataIfNeeded(it, dataPack) }
                .forEach { (key1, value1) ->
                    appendLine("v3  $key1")
                    appendLine(value1.toUHexString())
                    val decode = dataPack.uniSmartDecode(request.servantName, request.funcName, key1, value1)
                    if (decode != null) appendLine(decode)
                }
        }
        else -> error("unsupported version $version")
    }
}

private fun refineDataIfNeeded(
    it: Map.Entry<String, ByteArray>,
    dataPack: DataPack,
) = it.key to it.value.drop(1).dropLast(1).toByteArray()

@Suppress("UNUSED_PARAMETER")
internal fun DataPack.uniSmartDecode(servant: String, func: String, name: String, data: ByteArray): String? {
    return when ("$servant.$func") {
        "PushService.SvcReqRegister" -> {
            data.loadAs(
                if (direction == Direction.IN) SvcRespRegister.serializer()
                else SvcReqRegister.serializer()
            )._miraiContentToString()
        }
        else -> null
    }
}