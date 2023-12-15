package core.data.dto

import kotlinx.serialization.Serializable

/**
 * TODO Token传输对象示例
 *
 * @author 高国峰
 * @date 2023/12/8-17:33
 */
@Serializable
data class TokenDto(
    val token: String = ""
)