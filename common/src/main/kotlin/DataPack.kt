package io.github.mzdluo123.enableqqlog

import net.mamoe.mirai.utils.toUHexString

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

    LOG,
}

data class OicqRequest(
    val cmd: Int,
    val subCmd: Int,
    val svcCmd: String // serviceCommandName
) {
    override fun toString(): String {
        return "OicqRequest(cmd=0x${cmd.toShort().toUHexString("")}, subCmd=$subCmd, svcCmd='$svcCmd')"
    }
}

class OicqHookOnMakePacket(
    val request: OicqRequest, // json
    val data: ByteArray
)