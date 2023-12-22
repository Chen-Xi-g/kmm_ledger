package core.data.api

import core.utils.Res
import core.utils.Res.httpClient.base_path

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
        data object CodeImage : AccountApi("captchaImage".base_path)

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
        ) : AccountApi("login".base_path) {
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
        ) : AccountApi("register".base_path) {
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
        ) : AccountApi( "forgotPwd".base_path) {
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
        ) : AccountApi("sendEmailActivate".base_path) {
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
        data class Agreement(val type: Int) : AccountApi("agreement".base_path)
    }

    /**
     * 账单相关接口
     */
    sealed class BillApi(url: String): NetApi(url){
        /**
         * 获取账单列表
         *
         * @param beginTime 开始时间 yyyy-MM-dd
         * @param endTime 结束时间 yyyy-MM-dd
         * @param billName 账单名称
         * @param typeId 消费类型Id
         * @param typeTag 消费类型标签
         */
        data class GetBill(
            val beginTime: String? = null,
            val endTime: String? = null,
            val billName: String? = null,
            val typeId: String? = null,
            val typeTag: String? = null
        ) : BillApi("getBill".base_path){
            fun toMap(): Map<String, String?> {
                return mapOf(
                    "beginTime" to beginTime,
                    "endTime" to endTime,
                    "billName" to billName,
                    "typeId" to typeId,
                    "typeTag" to typeTag,
                )
            }
        }

        /**
         * 获取消费类型
         *
         * @param typeTag 类型标签（0代表支出 1代表收入）
         * @param typeName 类型名称
         */
        data class GetPayType(
            val typeTag: String = "0",
            val typeName: String? = null
        ) : BillApi("getPayType".base_path){
            fun toMap(): Map<String, String?> {
                return mapOf(
                    "typeTag" to typeTag,
                    "typeName" to typeName,
                )
            }
        }

        /**
         * 获取消费类型子列表
         *
         * @param typeId 消费类型Id
         */
        data class GetPayTypeChild(val typeId: String) : BillApi("getPayTypeChild".base_path + "/$typeId")

        /**
         * 新增消费类型
         *
         * @param typeName 类型名称
         * @param typeTag 类型标签（0代表支出 1代表收入）
         * @param parentId 父级id
         * @param typeId 消费类型Id
         */
        data class AddOrEditPayType(
            val typeName: String,
            val typeTag: String,
            val parentId: Long? = null,
            val typeId: Long? = null
        ): BillApi("addOrUpdatePayType".base_path){
            fun toMap(): Map<String, String?> {
                return mapOf(
                    "typeName" to typeName,
                    "typeTag" to typeTag,
                    "parentId" to parentId?.toString(),
                    "typeId" to typeId?.toString(),
                )
            }
        }
    }
}