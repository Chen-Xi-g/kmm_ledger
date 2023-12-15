package core.domain.user_case

/**
 * 校验验证码
 *
 * @author 高国峰
 * @date 2023/10/10-11:46
 */
class ValidationCode {

    /**
     * 校验邮箱格式
     *
     * @param code 验证码
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(code: String): ValidationResult {
        if (code.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "验证码不能为空"
            )
        }
        if (!code.matches(Regex("[0-9]+"))){
            return ValidationResult(
                successful = false,
                errorMessage = "验证码只能是数字"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}