package core.utils

import io.ktor.http.URLProtocol

/**
 * 全局常量, 如网络请求地址\常用字符串等
 *
 * @author 高国峰
 * @date 2023/12/7-15:08
 */
object Res {

    object httpClient{
        val server_protocol = URLProtocol.HTTP
        const val server_host = "192.168.1.3"
        const val server_port = 8080
        const val base_path = "dev-api/app/"
    }

    object strings {
        const val str_logo = "Logo"
    }

}