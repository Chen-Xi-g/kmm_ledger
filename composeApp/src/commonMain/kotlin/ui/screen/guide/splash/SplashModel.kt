package ui.screen.guide.splash

import core.navigation.UiEvent

sealed interface SplashEvent : UiEvent {
    /**
     * 点击或自动跳过
     */
    data object Skip : SplashEvent
}