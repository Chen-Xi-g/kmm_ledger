package core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 系统协议
 *
 * @author 高国峰
 * @date 2023/12/10-15:02
 *
 * @property agreementContent 协议内容
 * @property agreementId 协议ID
 * @property agreementType 协议类型(1:注册协议;2:隐私政策;)
 */
@Serializable
data class AgreementDto(
    @SerialName("agreementContent")
    val agreementContent: String = "",
    @SerialName("agreementId")
    val agreementId: Long = 0,
    @SerialName("agreementType")
    val agreementType: String = ""
)

/**
 * 验证码传输对象
 *
 * @author 高国峰
 * @date 2023/12/8-17:33
 */
@Serializable
data class CodeImageDto(
    val img: String = "",
    val uuid: String = ""
)

/**
 * Token传输对象
 *
 * @author 高国峰
 * @date 2023/12/8-17:33
 */
@Serializable
data class TokenDto(
    val token: String = ""
)

/**
 * 用户信息传输对象
 *
 * @property accountNum 账户数量
 * @property billNum 账单数量
 * @property email 邮箱
 * @property nickName 昵称
 * @property sex 性别（sys_user_sex 0=男,1=女,2=未知）
 * @property status 状态（sys_user_status 0=正常 1=停用 2=未激活）
 * @property userId 用户ID
 * @property userName 用户名
 */
@Serializable
data class UserInfoDto(
    val accountNum: Int = 0,
    val billNum: Int = 0,
    val email: String = "",
    val nickName: String = "",
    val sex: String = "",
    val status: String = "",
    val userId: Long = 0,
    val userName: String = "",
    val createTime: String = ""
)