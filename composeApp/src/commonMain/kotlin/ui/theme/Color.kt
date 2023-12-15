package ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 自定义应用内明暗主题颜色
 *
 * 个人Demo程序可以通过[Material Design Theme Builder](https://m3.material.io/theme-builder)来生成自己的主题颜色.
 * 国内建议自定义主题颜色
 *
 * @param surface Surface背景颜色
 * @param onSurface Surface的内容颜色
 * @param textTitle 文字标题颜色
 * @param textPrimary 文字主要颜色
 * @param textSecondary 文字次要颜色
 */
@Immutable
data class CustomColorPalette(
    val surface: Color = Color.Unspecified,
    val onSurface: Color = Color.Unspecified,
    val textTitle: Color = Color.Unspecified,
    val textPrimary: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified
)

val LocalColor = staticCompositionLocalOf { CustomColorPalette() }

/*              亮色            */
val COLOR_SURFACE = Color(0xFFFFFFFF)
val COLOR_ON_SURFACE = Color(0xFF333333)
val COLOR_TEXT_PRIMARY = Color(0xFF333333)
val COLOR_TEXT_SECONDARY = Color(0xFF666666)
val COLOR_THEME_PRIMARY = Color(0xFF3A94FF)


/*              暗色            */
val COLOR_SURFACE_DARK = Color(0xFF1A1A1A)
val COLOR_ON_SURFACE_DARK = Color(0xFFFFFFFF)
val COLOR_TEXT_PRIMARY_DARK = Color(0xFFFFFFFF)
val COLOR_TEXT_SECONDARY_DARK = Color(0xFFCCCCCC)
val COLOR_THEME_PRIMARY_DARK = Color(0xFF1E5AA8)

/**
 * 自定义亮色主题颜色
 */
val LedgerLightColorScheme = CustomColorPalette(
    surface = COLOR_SURFACE,
    onSurface = COLOR_ON_SURFACE,
    textTitle = Color.Black,
    textPrimary = COLOR_TEXT_PRIMARY,
    textSecondary = COLOR_TEXT_SECONDARY
)

/**
 * 自定义暗色主题颜色
 */
val LedgerDarkColorScheme = CustomColorPalette(
    surface = COLOR_SURFACE_DARK,
    onSurface = COLOR_ON_SURFACE_DARK,
    textTitle = Color.White,
    textPrimary = COLOR_TEXT_PRIMARY_DARK,
    textSecondary = COLOR_TEXT_SECONDARY_DARK
)