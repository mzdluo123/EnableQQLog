@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package test

import net.mamoe.mirai.internal.network.protocol.data.jce.RequestDataVersion2
import net.mamoe.mirai.internal.network.protocol.data.jce.RequestDataVersion3
import net.mamoe.mirai.internal.utils.io.serialization.loadAs
import net.mamoe.mirai.utils.toUHexString


internal fun ByteArray.uniSmartPrint(version: Int): String = buildString {
    when (version) {
        2 -> {
            val map = loadAs(RequestDataVersion2.serializer()).map
            map.entries.forEach { (key, value) ->
                value.entries.forEach { (key1, value1) ->
                    appendLine("v2  $key  $key1")
                    appendLine(value1.toUHexString())
                }
            }
        }
        3 -> {
            val value = loadAs(RequestDataVersion3.serializer()).map
            value.entries.forEach { (key1, value1) ->
                appendLine("v3  $key1")
                appendLine(value1.toUHexString())
            }
        }
        else -> error("unsupported version ${version}")
    }
}