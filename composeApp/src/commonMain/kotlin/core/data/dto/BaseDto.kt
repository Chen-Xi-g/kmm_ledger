package core.data.dto

import kotlinx.serialization.Serializable

/**
 * 实体类基类
 *
 * @author 高国峰
 * @date 2023/12/8-17:32
 */
@Serializable
open class BaseDto<T>(
    val code: Int = 0,
    val data: T? = null,
    val msg: String? = null
){
    fun isSuccess() = code == 200
}

