package core.data.dto

import kotlinx.serialization.Serializable

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