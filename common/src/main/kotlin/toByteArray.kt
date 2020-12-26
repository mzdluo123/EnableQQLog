/*
 * Copyright 2019-2020 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL internal LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_API_USAGE", "unused")

package io.github.mzdluo123.enableqqlog

/*
 * 类型转换 Utils.
 * 这些函数为内部函数, 可能会改变
 */

/**
 * 255 -> 00 FF
 */
internal fun Short.toByteArray(): ByteArray = with(toInt()) {
    byteArrayOf(
        (shr(8) and 0xFF).toByte(),
        (shr(0) and 0xFF).toByte()
    )
}

/**
 * 255 -> 00 00 00 FF
 */
internal fun Int.toByteArray(): ByteArray = byteArrayOf(
    ushr(24).toByte(),
    ushr(16).toByte(),
    ushr(8).toByte(),
    ushr(0).toByte()
)

/**
 * 255 -> 00 00 00 FF
 */
internal fun Long.toByteArray(): ByteArray = byteArrayOf(
    (ushr(56) and 0xFF).toByte(),
    (ushr(48) and 0xFF).toByte(),
    (ushr(40) and 0xFF).toByte(),
    (ushr(32) and 0xFF).toByte(),
    (ushr(24) and 0xFF).toByte(),
    (ushr(16) and 0xFF).toByte(),
    (ushr(8) and 0xFF).toByte(),
    (ushr(0) and 0xFF).toByte()
)

internal fun Int.toUHexString(separator: String = " "): String = this.toByteArray().toUHexString(separator)

/**
 * 255 -> 00 FF
 */
internal fun UShort.toByteArray(): ByteArray = with(toUInt()) {
    byteArrayOf(
        (shr(8) and 255u).toByte(),
        (shr(0) and 255u).toByte()
    )
}

internal fun Short.toUHexString(separator: String = " "): String = this.toUShort().toUHexString(separator)

internal fun UShort.toUHexString(separator: String = " "): String =
    this.toInt().shr(8).toUShort().toUByte().toUHexString() + separator + this.toUByte().toUHexString()

internal fun ULong.toUHexString(separator: String = " "): String =
    this.toLong().toUHexString(separator)

internal fun Long.toUHexString(separator: String = " "): String =
    this.ushr(32).toUInt().toUHexString(separator) + separator + this.toUInt().toUHexString(separator)

/**
 * 255 -> 00 FF
 */
internal fun UByte.toByteArray(): ByteArray = byteArrayOf((this and 255u).toByte())

internal fun UByte.toUHexString(): String = this.toByte().toUHexString()

/**
 * 255u -> 00 00 00 FF
 */
internal fun UInt.toByteArray(): ByteArray = byteArrayOf(
    (shr(24) and 255u).toByte(),
    (shr(16) and 255u).toByte(),
    (shr(8) and 255u).toByte(),
    (shr(0) and 255u).toByte()
)

/**
 * 转 [ByteArray] 后再转 hex
 */
internal fun UInt.toUHexString(separator: String = " "): String = this.toByteArray().toUHexString(separator)

/**
 * 转无符号十六进制表示, 并补充首位 `0`.
 * 转换结果示例: `FF`, `0E`
 */
internal fun Byte.toUHexString(): String = this.toUByte().fixToUHex()

/**
 * 转无符号十六进制表示, 并补充首位 `0`.
 */
internal fun Byte.fixToUHex(): String = this.toUByte().fixToUHex()

/**
 * 转无符号十六进制表示, 并补充首位 `0`.
 */
internal fun UByte.fixToUHex(): String =
    if (this.toInt() in 0..15) "0${this.toString(16).toUpperCase()}" else this.toString(16).toUpperCase()

internal fun String.hexToBytes(): ByteArray =
    this.split(" ")
        .asSequence()
        .filterNot { it.isEmpty() }
        .map { s -> s.toUByte(16).toByte() }
        .toList()
        .toByteArray()

/**
 * 每 2 char 为一组, 转换 Hex 为 [ByteArray]
 *
 * 这个方法很累, 不建议经常使用.
 */
internal fun String.chunkedHexToBytes(): ByteArray =
    this.asSequence().chunked(2).map { (it[0].toString() + it[1]).toUByte(16).toByte() }.toList().toByteArray()

/**
 * 删掉全部空格和换行后每 2 char 为一组, 转换 Hex 为 [ByteArray].
 */
internal fun String.autoHexToBytes(): ByteArray =
    this.replace("\n", "").replace(" ", "").asSequence().chunked(2).map {
        (it[0].toString() + it[1]).toUByte(16).toByte()
    }.toList().toByteArray()

/**
 * 将无符号 Hex 转为 [UByteArray], 有根据 hex 的 [hashCode] 建立的缓存.
 */
internal fun String.hexToUBytes(): UByteArray =
    this.split(" ")
        .asSequence()
        .filterNot { it.isEmpty() }
        .map { s -> s.toUByte(16) }
        .toList()
        .toUByteArray()

/**
 * 将 [this] 前 4 个 [Byte] 的 bits 合并为一个 [Int]
 *
 * 详细解释:
 * 一个 [Byte] 有 8 bits
 * 一个 [Int] 有 32 bits
 * 本函数将 4 个 [Byte] 的 bits 连接得到 [Int]
 */
internal fun ByteArray.toUInt(): UInt =
    (this[0].toUInt().and(255u) shl 24) + (this[1].toUInt().and(255u) shl 16) + (this[2].toUInt()
        .and(255u) shl 8) + (this[3].toUInt().and(
        255u
    ) shl 0)

internal fun ByteArray.toUShort(): UShort =
    ((this[0].toUInt().and(255u) shl 8) + (this[1].toUInt().and(255u) shl 0)).toUShort()

internal fun ByteArray.toInt(): Int =
    (this[0].toInt().and(255) shl 24) + (this[1].toInt().and(255) shl 16) + (this[2].toInt()
        .and(255) shl 8) + (this[3].toInt().and(
        255
    ) shl 0)


@JvmOverloads
internal fun generateImageId(md5: ByteArray, format: String = "mirai"): String {
    return """{${generateUUID(md5)}}.$format"""
}

internal fun generateUUID(md5: ByteArray): String {
    return "${md5[0, 3]}-${md5[4, 5]}-${md5[6, 7]}-${md5[8, 9]}-${md5[10, 15]}"
}

@JvmSynthetic
internal operator fun ByteArray.get(rangeStart: Int, rangeEnd: Int): String = buildString {
    for (it in rangeStart..rangeEnd) {
        append(this@get[it].fixToString())
    }
}

private fun Byte.fixToString(): String {
    return when (val b = this.toInt() and 0xff) {
        in 0..15 -> "0${this.toString(16).toUpperCase()}"
        else -> b.toString(16).toUpperCase()
    }
}

@JvmOverloads
@Suppress("DuplicatedCode") // false positive. foreach is not common to UByteArray and ByteArray
internal fun ByteArray.toUHexString(
    separator: String = " ",
    offset: Int = 0,
    length: Int = this.size - offset
): String {
    this.checkOffsetAndLength(offset, length)
    if (length == 0) {
        return ""
    }
    val lastIndex = offset + length
    return buildString(length * 2) {
        this@toUHexString.forEachIndexed { index, it ->
            if (index in offset until lastIndex) {
                var ret = it.toUByte().toString(16).toUpperCase()
                if (ret.length == 1) ret = "0$ret"
                append(ret)
                if (index < lastIndex - 1) append(separator)
            }
        }
    }
}

internal fun ByteArray.checkOffsetAndLength(offset: Int, length: Int) {
    require(offset >= 0) { "offset shouldn't be negative: $offset" }
    require(length >= 0) { "length shouldn't be negative: $length" }
    require(offset + length <= this.size) { "offset ($offset) + length ($length) > array.size (${this.size})" }
}