package ui.screen.setting.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import core.data.net.ResNet
import core.data.repository.AccountRepositoryImpl
import core.domain.repository.AccountRepository
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.RootComponent
import core.utils.KeyRepository
import kotlinx.coroutines.launch

/**
 * 用户信息 VM
 *
 * @author 高国峰
 * @date 2024/02/05-15:27
 */
class UserInfoVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent,
    private val accountRepository: AccountRepository = AccountRepositoryImpl()
) : BaseComponent<UserInfoState, UserInfoEvent, UserInfoEffect>(componentContext) {

    /**
     * 使用mutableStateOf作为昵称|邮箱
     *
     * 使用StateFlow会出现输入内容错误的情况,可以查看这个文章[Effective state management for TextField in Compose](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
     */
    var nickName by mutableStateOf(KeyRepository.nickName)
        private set

    override fun initialState(): UserInfoState {
        return UserInfoState()
    }

    override fun onEvent(event: UserInfoEvent) {
        when(event){
            UserInfoEvent.Back -> {
                rootComponent.onBack()
            }
            UserInfoEvent.ForgetPassword -> {
                rootComponent.onNavigationToScreenForgetPwd("")
            }
            UserInfoEvent.OnClickLogout -> {
                logout()
            }
            UserInfoEvent.ModifyUserInfo -> {
                modifyUserInfo()
            }
        }
    }

    /**
     * 退出登录
     */
    private fun logout(){
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when(val resp = accountRepository.logout()){
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastDefault(resp.msg)
                }
                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess("退出登录成功")
                    // 清空缓存数据
                    KeyRepository.clear()
                    // 跳转到登录页
                    rootComponent.onNavigationToScreenLogin(true)
                }
            }
        }
    }

    /**
     * 修改昵称和邮箱
     */
    private fun modifyUserInfo() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
        scope.launch {
            when (val resp = accountRepository.modifyUserInfo(nickName)) {
                is ResNet.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastDefault(resp.msg)
                }

                is ResNet.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false
                        )
                    }
                    rootComponent.toastSuccess("修改成功")
                }
            }
        }
    }

    /**
     * 更新昵称
     *
     * @param nickName 昵称
     */
    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }

}