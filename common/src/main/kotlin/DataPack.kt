package io.github.mzdluo123.enableqqlog

enum class Direction(val value: Int) {
    IN(1),
    OUT(2)
}

fun Direction.prettyString(): String = when (this) {
    Direction.IN -> Color.RED + "[IN]  " + Color.RESET
    Direction.OUT -> Color.LIGHT_YELLOW + "[OUT] " + Color.RESET
}

data class DataPack(val type: String, val direction: Direction, val contentJson: String, val packetType: PacketType)


enum class PacketType {
    MSG_MICRO,
    UNI,
    SVC,
    OICQ,

    CODEC_ENCODE,

    LOG,
}

data class OicqRequest(
    val cmd: Int,
    val subCmd: Int,
    val svcCmd: String, // serviceCommandName
) {
    override fun toString(): String {
        return "OicqRequest(cmd=0x${cmd.toShort().toUHexString("")}, subCmd=$subCmd, svcCmd='$svcCmd')"
    }
}

class OicqHookOnMakePacket(
    val request: OicqRequest, // json
    val data: ByteArray,
)

/*
 Int::class.java, String::class.java, String::class.java, String::class.java, String::class.java, String::class.java, ByteArray::class.java,
               Int::class.java, Int::class.java, String::class.java, Byte::class.java, Byte::class.java, Byte::class.java,
               ByteArray::class.java, ByteArray::class.java, ByteArray::class.java, Boolean::class.java,
 */

class CodecNativeEncodePacket(
    val sequenceId: Int,
    val imei: String,
    val unknown1: String, //460009373934487
    val appVersion: String, // "8.5.0.4003a808"
    val unknown2: String, //empty
    val commandId: String, // "Heartbeat.Alive"
    val unknown3: ByteArray, //63,85,21,-127
    val appId: Int,
    val appId2: Int,
    val uin: String,
    val unknown4: Byte,
    val unknown5: Byte,
    val unknown6: Byte,
    val unknown7: ByteArray?,
    val unknown8: ByteArray,
    val wupBuffer: ByteArray,
    val unknown9: Boolean,
)