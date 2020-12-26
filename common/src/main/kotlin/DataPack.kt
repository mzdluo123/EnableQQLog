package io.github.mzdluo123.enableqqlog

enum class Direction(val value: Int) {
    IN(1),
    OUT(2)
}

fun Direction.prettyString(): String = when (this) {
    Direction.IN -> "[IN]  "
    Direction.OUT -> "[OUT] "
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
)

class OicqHookOnMakePacket(
    val request: OicqRequest, // json
    val data: ByteArray
)