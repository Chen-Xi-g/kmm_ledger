package core.data.net

import Greeting
import core.data.dto.BaseDto
import core.utils.GlobalNavigator
import core.utils.KeyRepository
import core.utils.Res
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import platform.customHttpLogger
import platform.httpClient

val netClient = httpClient{
    expectSuccess = true
    install(UserAgent) {
        agent = Greeting().userAgent()
    }
    install(DefaultRequest) {
        url {
            protocol = Res.httpClient.server_protocol
            host = Res.httpClient.server_host
            port = Res.httpClient.server_port
        }
        header("Authorization", "Bearer ${KeyRepository.token}")
    }
    install(HttpTimeout){
        requestTimeoutMillis = 150000
        connectTimeoutMillis = 150000
        socketTimeoutMillis = 150000
    }
    install(Logging) {
        logger = customHttpLogger()
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        })
    }
}

/**
 * Post请求函数
 */
suspend inline fun <reified T> post(url: String, body: Any? = null): ResNet<T> {
    return try {
        val response = netClient.post {
            url(url)
            if (body != null) {
                setBody(body)
            }
            contentType(ContentType.Application.Json)
        }.body<BaseDto<T>>()
        if (response.isSuccess()) {
            ResNet.Success(response.data,response.msg)
        } else {
            if (response.code == 401){
                // 登录失效, 重新登录
                GlobalNavigator.login()
            }
            ResNet.Error(response.msg ?: "未知异常，请重试", response.code, response.data)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        e.toResNet()
    }
}

/**
 * Get请求函数
 */
suspend inline fun <reified T> get(url: String, map: MutableMap<String, Any?>.() -> Unit = {}): ResNet<T> {
    return try {
        val params = mutableMapOf<String, Any?>()
        params.apply(map)
        val response = netClient.get {
            url(url)
            params.forEach {
                if (it.value != null){
                    parameter(it.key, it.value)
                }
            }
        }.body<BaseDto<T>>()
        if (response.isSuccess()) {
            ResNet.Success(response.data, response.msg)
        } else {
            ResNet.Error(response.msg, response.code, response.data)
        }
    } catch (e: Exception) {
        e.toResNet()
    }
}

/**
 * Put请求函数
 */
suspend inline fun <reified T> put(url: String, body: Any? = null): ResNet<T> {
    return try {
        val response = netClient.put {
            url(url)
            if (body != null) {
                setBody(body)
            }
            contentType(ContentType.Application.Json)
        }.body<BaseDto<T>>()
        if (response.isSuccess()) {
            ResNet.Success(response.data, response.msg)
        } else {
            ResNet.Error(response.msg, response.code, response.data)
        }
    } catch (e: Exception) {
        e.toResNet()
    }
}

/**
 * Delete请求函数
 */
suspend inline fun <reified T> delete(url: String, map: MutableMap<String, Any?>.() -> Unit = {}): ResNet<T> {
    return try {
        val params = mutableMapOf<String, Any?>()
        params.apply(map)
        val response = netClient.delete {
            url(url)
            params.forEach {
                if (it.value != null) {
                    parameter(it.key, it.value)
                }
            }
        }.body<BaseDto<T>>()
        if (response.isSuccess()) {
            ResNet.Success(response.data, response.msg)
        } else {
            ResNet.Error(response.msg, response.code, response.data)
        }
    } catch (e: Exception) {
        e.toResNet()
    }
}

inline fun <T> Exception.toResNet(): ResNet<T> {
    // 根据不同的异常类型，返回不同的错误信息
    val message = when (this) {
        is RedirectResponseException -> "服务器异常,请稍后再试"
        is ClientRequestException -> "连接服务器失败,请稍后再试"
        is ServerResponseException -> "服务器响应失败,请稍后再试"
        else -> {
            "未知错误,请稍后再试"
        }
    }
    return ResNet.Error(message = message)
}