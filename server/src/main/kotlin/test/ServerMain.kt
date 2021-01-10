package test

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.concurrent.thread

object UniOicqSvcCodecServer {
    @JvmStatic
    fun main(args: Array<String>) {
        thread { UniServer.main(args) }
        thread { OicqServer.main(args) }
        thread { SvcServer.main(args) }
        thread { CodecEncodeServer.main(args) }
    }
}

object MsgMicroServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20810)
    }
}

object SvcServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20811)
    }
}

object UniServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20812)
    }
}

object OicqServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20813)
    }
}

object LogServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20814)
    }
}

object CodecEncodeServer {
    @JvmStatic
    fun main(args: Array<String>) {
        startServer(20815)
    }
}

private fun startServer(port: Int) {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        this.connector {
            host = "0.0.0.0"
            this.port = port
        }
        module(Application::module)
    }).start(true)
}
