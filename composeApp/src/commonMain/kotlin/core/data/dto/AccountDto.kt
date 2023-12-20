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