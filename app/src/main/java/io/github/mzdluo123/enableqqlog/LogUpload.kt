package io.github.mzdluo123.enableqqlog

import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class LogUpload {
    companion object {
        private val client = OkHttpClient()
        private fun upload(string: String, url: String) {
            if (!BuildConfig.ENABLE) return
            client.newCall(
                Request.Builder().url(url).method("POST", string.toRequestBody())
                    .build()
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    l("上传失败 $e")
                }

                override fun onResponse(call: Call, response: Response) {
                }

            })
        }

        private val PacketType.url: String
            get() = when (this) {
                PacketType.UNI -> BuildConfig.URL_UNI
                PacketType.MSG_MICRO -> BuildConfig.URL_MSG_MICRO
                PacketType.SVC -> BuildConfig.URL_SVC
                PacketType.OICQ -> BuildConfig.URL_OICQ
                PacketType.LOG -> BuildConfig.URL_LOG
            }


        fun upload(dire: Direction, type: String, content: Any?, packetType: PacketType) {
            upload(
                gson.toJson(DataPack(type, dire, gson.toJson(content), packetType)), packetType.url
            )
        }
    }
}


fun pushLog(content: String) {
    LogUpload.upload(Direction.OUT, "LOG", content, PacketType.LOG)
}