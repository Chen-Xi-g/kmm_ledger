package ui.screen.setting.account

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.data.dto.UserAccountDto
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import kotlinx.coroutines.launch

/**
 * 账户管理 VM
 *
 * @author 高国峰
 * @date 2024/01/29-11:36
 */
class AccountVM(
    componentContext: ComponentContext,
    private val isSelect: Boolean = false,
    private val rootComponent: IRootComponent,
    private val accountRepository: AccountRepository = AccountRepositoryImpl()
) : BaseComponent<AccountState, AccountEvent, AccountEffect>(componentContext) {

    init {
        lifecycle.doOnCreate {
            accountList()
        }
    }

    override fun initialState(): AccountState {
        return AccountState()
    }

    override fun onEvent(event: AccountEvent) {
        when(event){
            is AccountEvent.Back -> {
                rootComponent.onBack()
            }

            is AccountEvent.SelectAccount -> {
                if(isSelect){
                    rootComponent.onSelectedAccount(event.account)
                }
            }
        }
    }

    private fun accountList(){
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = accountRepository.accountList()){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            error = resp.msg,
                            isLoading = false
                        )
                    }
                }
                is ResNet.Success -> {
                    val savingsList = mutableListOf<UserAccountDto>()
                    val electronicList = mutableListOf<UserAccountDto>()
                    resp.`data`?.forEach {
                        if(it.type == "01"){
                            savingsList.add(it)
                        }else{
                            electronicList.add(it)
                        }
                    }
                    updateState {
                        it.copy(
                            savingsList = savingsList,
                            electronicList = electronicList,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}