package test

import com.google.gson.Gson
import io.github.mzdluo123.enableqqlog.DataPack
import io.github.mzdluo123.enableqqlog.prettyString
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object MsgMicroServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20810
            }
            module(Application::module)
        }).start(true)
    }
}

object SvcServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20811
            }
            module(Application::module)
        }).start(true)
    }
}

object UniServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20812
            }
            module(Application::module)
        }).start(true)
    }
}


private val lock = Mutex()
private fun Application.module() {
    routing {
        post("/") {
            lock.withLock {
                println(call.receivePack().prettyPrint())
            }
        }
    }
}

private fun DataPack.prettyPrint(): String = buildString {
    append(direction.prettyString())
    append("  ")
    append(type)
    appendLine()
    append(content)
    appendLine()
}

private suspend fun ApplicationCall.receivePack(): DataPack {
    return receive<String>().let { Gson().fromJson(it, DataPack::class.java) }
}
