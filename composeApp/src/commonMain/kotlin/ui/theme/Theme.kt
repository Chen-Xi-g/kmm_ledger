package ui.theme

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

val LightColors = lightColorScheme(
    primary = COLOR_THEME_PRIMARY,
    surface = COLOR_SURFACE,
    onSurface = COLOR_ON_SURFACE
)

val DarkColors = darkColorScheme(
    primary = COLOR_THEME_PRIMARY_DARK,
    surface = COLOR_SURFACE_DARK,
    onSurface = COLOR_ON_SURFACE_DARK
)

@Composable
fun LedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }
    ) {
        CompositionLocalProvider(
            // 字体不受系统大小印象
            LocalDensity provides Density(
                density = LocalDensity.current.density,
                fontScale = 1F
            ),
            // 自定义颜色
            LocalColor provides if (darkTheme) {
                LedgerDarkColorScheme
            } else {
                LedgerLightColorScheme
            },
            // 自定义资源
            LocalDrawable provides if (darkTheme) {
                LedgerDarkDrawable
            } else {
                LedgerLightDrawable
            },
            // 去除点击效果
            LocalRippleTheme provides NoRippleTheme,
            LocalIndication provides NoIndication
        ) {
            content()
        }
    }
}

object NoRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return Color.Unspecified
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
    }
}

object NoIndication : Indication {

    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }

}