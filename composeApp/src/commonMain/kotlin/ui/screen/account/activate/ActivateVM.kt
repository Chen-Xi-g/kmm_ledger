package ui.screen.account.activate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.domain.user_case.ValidationCode
import core.domain.user_case.ValidationUsername
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.UiEffect
import kotlinx.coroutines.launch

/**
 * 激活账号
 *
 * @author 高国峰
 * @date 2023/12/9-18:59
 */
class ActivateVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent,
    private val repository: AccountRepository = AccountRepositoryImpl(),
    private val validationUsername: ValidationUsername = ValidationUsername(),
    private val validationCode: ValidationCode = ValidationCode()
) : BaseComponent<ActivateState, ActivateEvent, UiEffect>(componentContext) {

    /**
     * 使用mutableStateOf作为用户名\验证码的数据源
     *
     * 使用StateFlow会出现输入内容错误的情况,可以查看这个文章[Effective state management for TextField in Compose](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
     */
    var username by mutableStateOf("")
        private set
    
    var code by mutableStateOf("")
        private set

    init {
        lifecycle.doOnCreate {
            getCodeImage()
        }
    }

    override fun initialState(): ActivateState {
        return ActivateState()
    }

    override fun onEvent(event: ActivateEvent) {
        when(event){
            ActivateEvent.GoBack -> {
                rootComponent.onBack()
            }
            ActivateEvent.Submit -> {
                submit()
            }
            ActivateEvent.RefreshCode -> {
                getCodeImage()
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
    private fun submit() {
        val usernameResult = validationUsername.execute(username)
        val codeResult = validationCode.execute(code)
        val hashError = listOf(
            usernameResult,
            codeResult
        ).any { !it.successful }
        if (hashError) {
            updateState {
                it.copy(
                    errorUsername = usernameResult.errorMessage,
                    errorCaptcha = codeResult.errorMessage
                )
            }
            return
        }
        updateState {
            it.copy(
                errorUsername = null,
                errorCaptcha = null,
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = repository.activateAccount(
                username = username,
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
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess(resp.msg)
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
                    rootComponent.toastError(resp.msg)
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            codeImg = resp.data?.img ?: "",
                            uuid = resp.data?.uuid ?: ""
                        )
                    }
                    rootComponent.toastSuccess(resp.msg)
                }
            }
        }
    }

}