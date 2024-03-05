package ui.screen.guide.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import core.navigation.BaseComponent
import core.navigation.IRootComponent
import core.navigation.UiEffect
import core.navigation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * 启动页
 *
 * @author 高国峰
 * @date 2023/12/6-16:30
 */
class SplashVM(
    componentContext: ComponentContext,
    private val rootComponent: IRootComponent
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
                rootComponent.onNavigationToScreenGuide()
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
                        rootComponent.onNavigationToScreenGuide()
                    }
                }
                delay(1.seconds)
            }
        }
    }
}