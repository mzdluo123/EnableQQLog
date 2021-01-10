package io.github.mzdluo123.enableqqlog

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class LogUpload {
    companion object {
        private val queue = Channel<Call>(Channel.BUFFERED)
        private val client = OkHttpClient().also {
            GlobalScope.launch {
                queue.receiveAsFlow().collect { call ->
                    kotlin.runCatching {
                        withContext(Dispatchers.IO) {
                            call.execute()
                        }
                    }
                }
            }
        }

        fun log(log: String) {
            upload(Direction.OUT, "LOG", log, PacketType.LOG)
        }

        private fun upload(string: String, url: String) {
            if (!BuildConfig.ENABLE) return
            client.newCall(
                Request.Builder().url(url).method("POST", string.toRequestBody())
                    .build()
            ).let { queue.offer(it) }
        }

        private val PacketType.url: String
            get() = when (this) {
                PacketType.UNI -> BuildConfig.URL_UNI
                PacketType.MSG_MICRO -> BuildConfig.URL_MSG_MICRO
                PacketType.SVC -> BuildConfig.URL_SVC
                PacketType.OICQ -> BuildConfig.URL_OICQ
                PacketType.LOG -> BuildConfig.URL_LOG
                PacketType.CODEC_ENCODE -> BuildConfig.URL_CODEC_ENCODE
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