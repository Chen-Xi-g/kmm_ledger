package core.domain.user_case

/**
 * 校验密码
 *
 * @author 高国峰
 * @date 2023/10/10-11:46
 */
class ValidationPassword {

    /**
     * 校验密码格式
     *
     * @param password 密码
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(password: String): ValidationResult {
        if (password.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = "密码长度不能小于8位"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } && password.any { it.isLetter() }
        if (!containsLettersAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "密码必须包含字母和数字"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    /**
     * 校验密码是否一致
     *
     * @param password 密码
     * @param confirmPassword 确认密码
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(password: String, confirmPassword: String): ValidationResult {
        if (password.isBlank() || confirmPassword.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "确认密码不能为空"
            )
        }
        if (password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "两次密码输入不一致"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}