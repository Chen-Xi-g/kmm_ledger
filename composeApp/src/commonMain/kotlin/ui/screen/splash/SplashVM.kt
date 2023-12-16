package ui.screen.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.navigation.BaseComponent
import core.navigation.UiEffect
import core.navigation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ui.widget.ToastState
import kotlin.time.Duration.Companion.seconds

/**
 * 启动页
 *
 * @author 高国峰
 * @date 2023/12/6-16:30
 */
class SplashVM(
    componentContext: ComponentContext,
    private val onToast: (String?, ToastState.ToastStyle) -> Unit,
    private val onNavigationToScreenGuide: () -> Unit
) : BaseComponent<UiState, SplashEvent, UiEffect>(componentContext){

    init {
        lifecycle.doOnCreate {
            startCountDown()
        }
    }

    /**
     * 倒计时
     */
    private val _countDown = MutableStateFlow(5)
    val countDown: StateFlow<Int> = _countDown
    private var countDownJob: Job? = null

    override fun initialState(): UiState {
        return object : UiState{}
    }

    override fun onEvent(event: SplashEvent) {
        when(event){
            SplashEvent.Skip -> {
                countDownJob?.cancel()
                onToast.normal("跳过")
                onNavigationToScreenGuide()
            }
        }
    }

    private fun startCountDown(){
        countDownJob?.cancel()
        countDownJob = scope.launch {
            while (_countDown.value > 0){
                _countDown.value = _countDown.value - 1
                if (_countDown.value <= 0) {
                    scope.launch(Dispatchers.Main){
                        onNavigationToScreenGuide()
                    }
                }
                delay(1.seconds)
            }
        }
    }
}