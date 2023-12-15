package core.domain.user_case

/**
 * 校验用户名
 *
 * @author 高国峰
 * @date 2023/10/10-11:46
 */
class ValidationUsername {

    /**
     * 校验用户名格式
     *
     * @param username 用户名
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(username: String): ValidationResult {
        if (username.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "用户名不能为空"
            )
        }
        val firstLetter = username.first().isLetter()
        if (!firstLetter){
            return ValidationResult(
                successful = false,
                errorMessage = "用户名必须以字母开头"
            )
        }
        val regex = Regex("^[A-Za-z0-9]+$")
        if (!regex.matches(username)){
            return ValidationResult(
                successful = false,
                errorMessage = "用户名只能包含字母和数字"
            )
        }
        val containsLettersAndDigits = username.any { it.isDigit() } && username.any { it.isLetter() }
        if (!containsLettersAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "用户名必须包含字母和数字"
            )
        }
        if (username.length !in 6..20){
            return ValidationResult(
                successful = false,
                errorMessage = "用户名长度必须在6-20位之间"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}