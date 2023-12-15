package ui.screen.account.agreement

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.navigation.BaseComponent
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
    private val goBack: () -> Unit,
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
                goBack()
            }

            AgreementEvent.ClearError -> {
                updateState {
                    it.copy(
                        error = null
                    )
                }
            }

            is AgreementEvent.LoadAgreement -> {
                loadAgreement()
            }
        }
    }

    private fun loadAgreement() {
        if (type != 1 && type != 2) {
            updateState {
                it.copy(
                    error = "协议类型错误"
                )
            }
            return
        }
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            val res = repository.agreement(type)
            if (res.isSuccess()) {
                updateState {
                    it.copy(
                        content = res.data ?: "",
                        isLoading = false
                    )
                }
            } else {
                updateState {
                    it.copy(
                        error = res.msg,
                        isLoading = false
                    )
                }
            }
        }
    }

}