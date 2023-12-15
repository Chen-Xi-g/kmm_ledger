package platform

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.Logger

/**
 * 获取Http客户端
 */
expect fun httpClient(config: HttpClientConfig<*>.() ->Unit): HttpClient

/**
 * 自定义日志打印
 */
expect fun customHttpLogger(): Logger