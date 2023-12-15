package platform

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logger

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)
    engine {
        config {
            retryOnConnectionFailure(true)
        }
    }
}

actual fun customHttpLogger(): Logger {
    return object : Logger {
        override fun log(message: String) {
            Log.d("ML_Ledger_Logger", message)
        }
    }
}