package core.domain.user_case

/**
 * 校验邮箱
 *
 * @author 高国峰
 * @date 2023/10/10-11:46
 */
class ValidationEmail {

    companion object{
        private const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    }

    /**
     * 校验邮箱格式
     *
     * @param email 邮箱
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(email: String): ValidationResult {
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "邮箱不能为空"
            )
        }

        if (!Regex(EMAIL_PATTERN).matches(email)){
            return ValidationResult(
                successful = false,
                errorMessage = "邮箱格式不正确"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}