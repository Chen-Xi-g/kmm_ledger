package core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import core.utils.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.widget.ToastState

/**
 * 统一封装导航组件
 *
 * @author 高国峰
 * @date 2023/12/8-14:20
 */
abstract class BaseComponent<S : UiState, EV : UiEvent, EF : UiEffect>(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    val scope = coroutineScope(Dispatchers.Default)

    private val _state: MutableStateFlow<S> by lazy { MutableStateFlow(initialState()) }
    val state by lazy { _state.asStateFlow() }

    private val _effect: MutableSharedFlow<EF> = MutableSharedFlow()
    val effect by lazy { _effect.asSharedFlow() }

    protected abstract fun initialState(): S

    /**
     * 事件接收
     */
    abstract fun onEvent(event: EV)

    /**
     * 更新UI状态
     */
    protected fun updateState(function: (S) -> S) {
        _state.update(function)
    }

    /**
     * 发送单次事件
     */
    fun onEffect(effect: EF) {
        scope.launch {
            _effect.emit(effect)
        }
    }

    /**
     * toast默认状态
     */
    fun ((String?, ToastState.ToastStyle) -> Unit).toastDefault(msg: String?) {
        this(msg, ToastState.ToastStyle.Default)
    }

    /**
     * toast错误状态
     */
    fun ((String?, ToastState.ToastStyle) -> Unit).toastError(msg: String?) {
        this(msg, ToastState.ToastStyle.Error)
    }

    /**
     * toast成功状态
     */
    fun ((String?, ToastState.ToastStyle) -> Unit).toastSuccess(msg: String?) {
        this(msg, ToastState.ToastStyle.Success)
    }
}

interface UiState

interface UiEvent

interface UiEffect

@Composable
fun <S : UiState, EV : UiEvent, EF : UiEffect> BaseComponent<S, EV, EF>.collectEffect(
    onEffect: (suspend (effect: EF) -> Unit)
) {
    val effectFlow = this.effect
    LaunchedEffect(effectFlow) {
        effectFlow.collect {
            onEffect(it)
        }
    }
}