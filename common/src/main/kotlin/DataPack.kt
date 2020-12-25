package io.github.mzdluo123.enableqqlog

enum class Direction(val value: Int) {
    IN(1),
    OUT(2)
}

fun Direction.prettyString(): String = when (this) {
    Direction.IN -> "[IN]  "
    Direction.OUT -> "[OUT] "
}

data class DataPack(val type: String, val direction: Direction, val content: Any? = null)