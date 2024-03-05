package ui.screen.account.login

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
import core.domain.user_case.ValidationTerms
import core.domain.user_case.ValidationUsername
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.UiEffect
import core.utils.KeyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 登录组件
 *
 * @author 高国峰
 * @date 2023/12/8-12:35
 */
class LoginVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent,
    private val repository: AccountRepository = AccountRepositoryImpl(),
    private val validationUsername: ValidationUsername = ValidationUsername(),
    private val validationPassword: ValidationPassword = ValidationPassword(),
    private val validationCode: ValidationCode = ValidationCode(),
    private val validationTerms: ValidationTerms = ValidationTerms()
) : BaseComponent<LoginState, LoginEvent, UiEffect>(componentContext) {

    /**
     * 使用mutableStateOf作为用户名\密码\验证码的数据源
     *
     * 使用StateFlow会出现输入内容错误的情况,可以查看这个文章[Effective state management for TextField in Compose](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
     */
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var code by mutableStateOf("")
        private set

    init {
        lifecycle.doOnCreate {
            getCodeImage()
        }
    }

    override fun initialState(): LoginState {
        username = KeyRepository.username
        return LoginState()
    }

    override fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.GoBack -> rootComponent.onBack()

            is LoginEvent.UpdateTerms -> {
                updateState {
                    it.copy(
                        isAcceptedTerms = event.isAcceptedTerms
                    )
                }
            }

            LoginEvent.RefreshCode -> {
                getCodeImage()
            }

            LoginEvent.Submit -> {
                login()
            }

            is LoginEvent.ActivateAccount -> rootComponent.onNavigationToScreenActivateAccount(event.username)
            is LoginEvent.ForgetPassword -> rootComponent.onNavigationToScreenForgetPwd(username)
            LoginEvent.PrivacyPolicy -> rootComponent.onNavigationToScreenAgreement(IRootComponent.TYPE_AGREEMENT_PRIVACY)
            is LoginEvent.Register -> rootComponent.onNavigationToScreenRegister(event.username)
            LoginEvent.UserAgreement -> rootComponent.onNavigationToScreenAgreement(IRootComponent.TYPE_AGREEMENT_USER)
        }
    }

    /**
     * 更新用户名
     */
    fun updateUsername(input: String){
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
     * 登录
     */
    private fun login() {
        val usernameResult = validationUsername.execute(username)
        val passwordResult = validationPassword.execute(password)
        val codeResult = validationCode.execute(code)
        val termsResult = validationTerms.execute(state.value.isAcceptedTerms)
        val hashError = listOf(
            usernameResult,
            passwordResult,
            codeResult,
            termsResult
        ).any { !it.successful }
        if (hashError) {
            updateState {
                it.copy(
                    errorUsername = usernameResult.errorMessage,
                    errorPassword = passwordResult.errorMessage,
                    errorCaptcha = codeResult.errorMessage
                )
            }
            rootComponent.toastError(termsResult.errorMessage ?: "")
            return
        }
        updateState {
            it.copy(
                errorUsername = null,
                errorPassword = null,
                errorCaptcha = null,
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = repository.login(
                username = username,
                password = password,
                code = code,
                uuid = state.value.uuid
            )) {
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                    getCodeImage()
                }

                is ResNet.Success -> {
                    KeyRepository.username = username
                    KeyRepository.token = resp.data ?: ""
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess("登录成功")
                    launch(Dispatchers.Main){
                        rootComponent.onNavigationToScreenMain()
                    }
                }
            }
        }
    }

    /**
     * 获取验证码
     */
    private fun getCodeImage() {
        scope.launch {
            when (val resp = repository.codeImage()) {
                is ResNet.Error -> {
                    rootComponent.toastError(resp.msg)
                }

                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            codeImg = resp.data?.img ?: "",
                            uuid = resp.data?.uuid ?: ""
                        )
                    }
                }
            }
        }
    }

}