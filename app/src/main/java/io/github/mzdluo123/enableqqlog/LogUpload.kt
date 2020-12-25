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


        enum class PacketType(
            val url: String
        ) {
            MSG_MICRO(BuildConfig.URL_MSG_MICRO),
            UNI(BuildConfig.URL_UNI),
            SVC(BuildConfig.URL_SVC)
        }

        fun upload(dire: Direction, type: String, content: Any?, packetType: PacketType) {
            upload(
                gson.toJson(DataPack(type, dire, content)), packetType.url
            )
        }
    }
}