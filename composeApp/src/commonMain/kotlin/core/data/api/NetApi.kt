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
         * 获取验证码
         */
        data object CodeImage : AccountApi(Res.httpClient.base_path + "captchaImage")

        /**
         * 登录
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

        /**
         * 注册
         *
         * @property username 用户名
         * @property email 邮箱
         * @property password 密码
         * @property code 验证码
         * @property uuid 验证码唯一ID
         */
        data class Register(
            val username: String,
            val email: String,
            val password: String,
            val code: String,
            val uuid: String
        ) : AccountApi(Res.httpClient.base_path + "register") {
            fun toMap(): Map<String, String> {
                return mapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password,
                    "code" to code,
                    "uuid" to uuid,
                )
            }
        }

        /**
         * 忘记密码
         *
         * @property username 用户名
         * @property password 密码
         * @property confirmPassword 确认密码
         * @property code 验证码
         * @property uuid 验证码唯一ID
         */
        data class ForgotPwd(
            val username: String,
            val password: String,
            val confirmPassword: String,
            val code: String,
            val uuid: String
        ) : AccountApi(Res.httpClient.base_path + "forgotPwd") {
            fun toMap(): Map<String, String> {
                return mapOf(
                    "username" to username,
                    "password" to password,
                    "confirmPassword" to confirmPassword,
                    "code" to code,
                    "uuid" to uuid,
                )
            }
        }

        /**
         * 激活账号
         *
         * @property username 用户名
         * @property code 验证码
         * @property uuid 验证码唯一ID
         */
        data class ActivateAccount(
            val username: String,
            val code: String,
            val uuid: String
        ) : AccountApi(Res.httpClient.base_path + "sendEmailActivate") {
            fun toMap(): Map<String, String> {
                return mapOf(
                    "username" to username,
                    "code" to code,
                    "uuid" to uuid,
                )
            }
        }

        /**
         * 系统协议
         *
         * @property type 协议类型
         */
        data class Agreement(val type: Int) : AccountApi(Res.httpClient.base_path + "agreement")
    }

}