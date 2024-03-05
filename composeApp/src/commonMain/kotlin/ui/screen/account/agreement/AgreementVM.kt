package ui.screen.account.agreement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.UiEffect
import kotlinx.coroutines.launch

/**
 * 系统协议
 *
 * @author 高国峰
 * @date 2023/12/10-14:46
 *
 * @param type 协议类型 1:用户协议 2:隐私政策
 */
class AgreementVM(
    component: ComponentContext,
    private val type: Int,
    private val rootComponent: IRootComponent,
    private val repository: AccountRepository = AccountRepositoryImpl(),
) :
    BaseComponent<AgreementState, AgreementEvent, UiEffect>(component) {

    init {
        lifecycle.doOnCreate {
            loadAgreement()
        }
    }

    override fun initialState(): AgreementState {
        return AgreementState(
            title = if (type == 1) "用户协议" else "隐私政策"
        )
    }

    override fun onEvent(event: AgreementEvent) {
        when (event) {
            AgreementEvent.GoBack -> {
                rootComponent.onBack()
            }

            is AgreementEvent.LoadAgreement -> {
                loadAgreement()
            }
        }
    }

    private fun loadAgreement() {
        if (type != 1 && type != 2) {
            rootComponent.toastError("协议类型错误")
            return
        }
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = repository.agreement(type)){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastError(resp.msg)
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            content = resp.data ?: "",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

}