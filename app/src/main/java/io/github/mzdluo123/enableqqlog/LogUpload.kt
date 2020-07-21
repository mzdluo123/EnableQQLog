package io.github.mzdluo123.enableqqlog

import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class LogUpload {
    companion object {
        enum class DIRECTION(val value: Int) {
            IN(1),
            OUT(2)
        }

        private val client = OkHttpClient()
        private fun upload(string: String) {
            if (!BuildConfig.ENABLE) return
            client.newCall(
                Request.Builder().url(BuildConfig.URL).method("POST", string.toRequestBody())
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

        public fun upload(dire: DIRECTION, type: String, content: Any?) {
            upload(gson.toJson(DataPack(type, dire.value, content)))
        }
    }
}

data class DataPack(val type: String, val direction: Int, val content: Any? = null)