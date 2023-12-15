package core.domain.user_case

/**
 * 校验返回结果
 *
 * @author 高国峰
 * @date 2023/10/10-11:47
 *
 * @param successful 是否成功
 * @param errorMessage 错误信息
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)