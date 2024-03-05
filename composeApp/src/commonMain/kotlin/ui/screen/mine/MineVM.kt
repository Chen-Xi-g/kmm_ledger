package ui.screen.mine

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.utils.KeyRepository
import kotlinx.coroutines.launch

/**
 * 我的 VM
 *
 * @author 高国峰
 * @date 2023/12/16-12:38
 */
class MineVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent,
    private val accountRepository: AccountRepository = AccountRepositoryImpl()
) : BaseComponent<MineState, MineEvent, MineEffect>(componentContext) {

    init {
        lifecycle.doOnResume {
            getUserInfo()
        }
    }

    override fun initialState(): MineState {
        return MineState()
    }

    override fun onEvent(event: MineEvent) {
        when(event){
            MineEvent.ToPayType -> {
                // 跳转到收支类型
                rootComponent.onNavigationToScreenPayTypeManager()
            }

            MineEvent.ToAccount -> {
                // 跳转到账户管理
                rootComponent.onNavigationToScreenAccountManager()
            }

            MineEvent.ToSetting -> {
                // 跳转到设置
                rootComponent.onNavigationToScreenSetting()
            }

            MineEvent.ToUserInfo -> {
                rootComponent.onNavigationToScreenUserInfo()
            }
        }
    }

    private fun getUserInfo(){
        scope.launch {
            // 获取用户信息
            when(val resp = accountRepository.userInfo()){
                is ResNet.Error -> {
                    rootComponent.toastError(resp.msg)
                }
                is ResNet.Success -> {
                    val info = resp.data
                    KeyRepository.nickName = info?.nickName ?: ""
                    KeyRepository.email = info?.email ?: ""
                    updateState {
                        it.copy(nickName = info?.nickName ?: "")
                    }
                }
            }
        }
    }
}