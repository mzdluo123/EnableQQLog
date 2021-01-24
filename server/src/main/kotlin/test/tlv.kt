@file:Suppress("EXPERIMENTAL_API_USAGE")

package test

import kotlinx.io.core.*
import net.mamoe.mirai.utils.TlvMap


@Suppress("FunctionName")
inline fun Input._readTLVMap(tagSize: Int = 2, suppressDuplication: Boolean = true): TlvMap =
    _readTLVMap(true, tagSize, suppressDuplication)

@Suppress("DuplicatedCode", "FunctionName")
fun Input._readTLVMap(
    expectingEOF: Boolean = true,
    tagSize: Int,
    suppressDuplication: Boolean = true,
): TlvMap {
    val map = mutableMapOf<Int, ByteArray>()
    var key = 0

    while (kotlin.run {
            try {
                key = when (tagSize) {
                    1 -> readUByte().toInt()
                    2 -> readUShort().toInt()
                    4 -> readUInt().toInt()
                    else -> error("Unsupported tag size: $tagSize")
                }
            } catch (e: Exception) { // java.nio.BufferUnderflowException is not a EOFException...
                if (expectingEOF) {
                    return map
                }
                throw e
            }
            key
        }.toUByte() != UByte.MAX_VALUE) {

        if (map.containsKey(key)) {
            @Suppress("ControlFlowWithEmptyBody")
            if (!suppressDuplication) {
                /*
                @Suppress("DEPRECATION")
                MiraiLogger.error(
                    @Suppress("IMPLICIT_CAST_TO_ANY")
                    """
                Error readTLVMap:
                duplicated key ${when (tagSize) {
                        1 -> key.toByte()
                        2 -> key.toShort()
                        4 -> key
                        else -> error("unreachable")
                    }.contentToString()}
                map=${map.contentToString()}
                duplicating value=${this.readUShortLVByteArray().toUHexString()}
                """.trimIndent()
                )*/
            } else {
                try {
                    this.discardExact(this.readShort().toInt() and 0xffff)
                } catch (e: Exception) { // BufferUnderflowException, java.io.EOFException
                    if (expectingEOF) {
                        return map
                    }
                    throw e
                }
            }
        } else {
            try {
                map[key] = this.readBytes(readUShort().toInt())
            } catch (e: Exception) { // BufferUnderflowException, java.io.EOFException
                if (expectingEOF) {
                    return map
                }
                throw e
            }
        }
    }
    return map
}
