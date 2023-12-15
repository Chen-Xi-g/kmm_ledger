package ui.screen.guide

import core.navigation.UiEvent

/**
 * 引导页事件
 *
 * @author 高国峰
 * @date 2023/12/6-16:38
 */
sealed interface GuideEvent : UiEvent{
    /**
     * 点击按钮
     */
    data object ClickButton : GuideEvent
}