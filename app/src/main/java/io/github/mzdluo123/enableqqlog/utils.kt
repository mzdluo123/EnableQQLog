package io.github.mzdluo123.enableqqlog

import com.google.gson.*
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Field
import java.lang.reflect.Type

fun l(msg: String) {
    XposedBridge.log("[EnableQQLog] $msg")
}

val hasFlag = Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBPrimitiveField")
    .getDeclaredField("hasFlag").also { it.isAccessible = true }

val messageMicro = Main.classLoader.loadClass("com.tencent.mobileqq.pb.MessageMicro")


val gson by lazy {
    val builder = GsonBuilder()
    builder.setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            if (clazz == Field::class.java) {
                return true
            }
            if (clazz == Class::class.java) {
                return true
            }
            return false
        }

        override fun shouldSkipField(f: FieldAttributes): Boolean {
            if (f.name == "_fields") {
                return true
            }
            return false
        }
    })


    val serial = object : JsonSerializer<Any> {
        override fun serialize(
            src: Any,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement? {
            val field = src::class.java.getDeclaredField("value")
            field.isAccessible = true
            if (!hasFlag.getBoolean(src)) {
                return null
            }
            when (field.type) {
                Number::class.java -> {
                    return JsonPrimitive(field.get(src) as Number)
                }
                Boolean::class.java -> {
                    return JsonPrimitive(field.getBoolean(src))
                }
                Byte::class.java -> {
                    return JsonPrimitive(field.getByte(src))
                }
                String::class.java -> {
                    return JsonPrimitive(field.get(src) as String)
                }
                ByteArray::class.java -> {
                    val bytes = field.get(src) as ByteArray
                    val array = JsonArray(bytes.size)
                    bytes.forEach { array.add(it) }
                    return array
                }
            }
            return null
        }

    }


    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBBytesField"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBBoolField"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBDoubleField"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBEnumField"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBFixed32Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBFixed64Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBInt32Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBInt64Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBSFixed32Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBSFixed64Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBSInt32Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBSInt64Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBStringField"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBUInt32Field"),
        serial
    )
    builder.registerTypeAdapter(
        Main.classLoader.loadClass("com.tencent.mobileqq.pb.PBUInt64Field"),
        serial
    )


    builder.create()
}