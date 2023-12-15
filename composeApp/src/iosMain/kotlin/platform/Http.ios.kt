package platform

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.Logger

/**
 * 获取Http客户端
 */
actual fun httpClient(config: HttpClientConfig<*>.() ->Unit) = HttpClient(Darwin){
    config(this)
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}

/**
 * 自定义日志打印
 */
actual fun customHttpLogger(): Logger {
    return object : Logger {
        override fun log(message: String) {
            println(message)
        }
    }
}