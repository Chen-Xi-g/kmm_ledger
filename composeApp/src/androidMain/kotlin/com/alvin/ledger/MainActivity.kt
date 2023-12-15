package com.alvin.ledger

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import core.navigation.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 沉浸式状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val root = RootComponent(defaultComponentContext())
        window.statusBarColor = Color.Transparent.toArgb()
        // 透明导航栏
        window.navigationBarColor = Color.Transparent.toArgb()
        setContent {
            // 设置状态栏图标颜色
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !isSystemInDarkTheme()
            App(root)
        }
    }
}