package core.data.api

import core.utils.Res

/**
 * 网络请求接口
 *
 * @author 高国峰
 * @date 2023/12/8-17:26
 */
sealed class NetApi(val url: String) {

    /**
     * 账户相关接口
     */
    sealed class AccountApi(url: String) : NetApi(url) {

        /**
         * TODO 登录接口示例, 可以按照这个格式写, 也可以自己定义
         *
         * @property username 用户名
         * @property password 密码
         * @property code 验证码
         * @property uuid 验证码唯一ID
         */
        data class Login(
            val username: String,
            val password: String,
            val code: String,
            val uuid: String
        ) : AccountApi(Res.httpClient.base_path + "login") {
            fun toMap(): Map<String, String> {
                return mapOf(
                    "username" to username,
                    "password" to password,
                    "code" to code,
                    "uuid" to uuid,
                )
            }
        }

    }

}