package core.domain.user_case

/**
 * 校验条款
 *
 * @author 高国峰
 * @date 2023/10/10-11:46
 */
class ValidationTerms {

    /**
     * 校验条款是否同意
     *
     * @param acceptedTerms 条款
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(acceptedTerms: Boolean): ValidationResult {
        if (!acceptedTerms){
            return ValidationResult(
                successful = false,
                errorMessage = "请阅读并同意《用户协议》与《隐私政策》"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}