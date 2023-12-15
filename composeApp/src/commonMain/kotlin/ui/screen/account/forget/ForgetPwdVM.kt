package ui.screen.account.forget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.domain.user_case.ValidationCode
import core.domain.user_case.ValidationPassword
import core.domain.user_case.ValidationUsername
import core.navigation.BaseComponent
import core.navigation.UiEffect
import kotlinx.coroutines.launch

/**
 * 忘记密码
 *
 * @author 高国峰
 * @date 2023/12/9-18:00
 */
class ForgetPwdVM(
    componentContext: ComponentContext,
    private val accountUsername: String = "",
    private val goBack: () -> Unit,
    private val repository: AccountRepository = AccountRepositoryImpl(),
    private val validationUsername: ValidationUsername = ValidationUsername(),
    private val validationPassword: ValidationPassword = ValidationPassword(),
    private val validationCode: ValidationCode = ValidationCode()
) : BaseComponent<ForgetPwdState, ForgetPwdEvent, UiEffect>(componentContext){

    /**
     * 使用mutableStateOf作为用户名\密码\确认密码\验证码的数据源
     *
     * 使用StateFlow会出现输入内容错误的情况,可以查看这个文章[Effective state management for TextField in Compose](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
     */
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var code by mutableStateOf("")
        private set

    init {
        lifecycle.doOnCreate {
            getCodeImage()
        }
    }

    override fun initialState(): ForgetPwdState {
        username = accountUsername
        return ForgetPwdState()
    }

    override fun onEvent(event: ForgetPwdEvent) {
        when(event){
            ForgetPwdEvent.ClearError -> {
                updateState {
                    it.copy(
                        error = null
                    )
                }
                getCodeImage()
            }
            ForgetPwdEvent.GoBack -> {
                goBack()
            }
            ForgetPwdEvent.RefreshCode -> {
                getCodeImage()
            }
            ForgetPwdEvent.Submit -> {
                submit()
            }
        }
    }

    /**
     * 更新用户名
     */
    fun updateUsername(input: String) {
        username = input
        val usernameResult = validationUsername.execute(input)
        updateState {
            it.copy(
                errorUsername = usernameResult.errorMessage
            )
        }
    }

    /**
     * 更新密码
     */
    fun updatePassword(input: String) {
        password = input
        val passwordResult = validationPassword.execute(input)
        updateState {
            it.copy(
                errorPassword = passwordResult.errorMessage
            )
        }
    }

    /**
     * 更新确认密码
     */
    fun updateConfirmPassword(input: String) {
        confirmPassword = input
        val configPasswordResult = validationPassword.execute(password, input)
        updateState {
            it.copy(
                errorConfirmPassword = configPasswordResult.errorMessage
            )
        }
    }

    /**
     * 更新验证码
     */
    fun updateCode(input: String) {
        code = input
        val codeResult = validationCode.execute(input)
        updateState {
            it.copy(
                errorCaptcha = codeResult.errorMessage
            )
        }
    }

    /**
     * 提交
     */
    private fun submit(){
        val usernameResult = validationUsername.execute(username)
        val passwordResult = validationPassword.execute(password)
        val configPasswordResult = validationPassword.execute(password, confirmPassword)
        val codeResult = validationCode.execute(code)
        val hashError = listOf(
            usernameResult,
            passwordResult,
            configPasswordResult,
            codeResult
        ).any { !it.successful }
        if (hashError) {
            updateState {
                it.copy(
                    errorUsername = usernameResult.errorMessage,
                    errorPassword = passwordResult.errorMessage,
                    errorConfirmPassword = configPasswordResult.errorMessage,
                    errorCaptcha = codeResult.errorMessage
                )
            }
            return
        }
        updateState {
            it.copy(
                errorUsername = null,
                errorPassword = null,
                errorConfirmPassword = null,
                errorCaptcha = null,
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = repository.forgotPwd(
                username = username,
                password = password,
                confirmPassword = confirmPassword,
                code = code,
                uuid = state.value.uuid
            )){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            error = resp.msg,
                            isLoading = false
                        )
                    }
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            error = resp.msg,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    /**
     * 获取验证码
     */
    private fun getCodeImage(){
        scope.launch {
            when(val resp = repository.codeImage()){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            error = resp.msg
                        )
                    }
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            codeImg = resp.data?.img ?: "",
                            uuid = resp.data?.uuid ?: "",
                            error = null
                        )
                    }
                }
            }
        }
    }
}