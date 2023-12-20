package ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 自定义应用内明暗主题颜色
 *
 * @param surface Surface背景颜色
 * @param onSurface Surface的内容颜色
 * @param textTitle 文字标题颜色
 * @param textPrimary 文字主要颜色
 * @param textSecondary 文字次要颜色
 * @param textHint 文字提示颜色
 * @param textError 文字错误颜色
 * @param themePrimary 主题主要色
 * @param themeSecondary 主题次要色
 * @param backIcon 返回按钮颜色
 * @param border 边框颜色
 * @param card 卡片颜色
 * @param toastBg Toast背景颜色，默认状态下的Toast背景颜色
 * @param toastSuccess Toast背景颜色，成功状态下的Toast背景颜色
 * @param toastError Toast背景颜色，错误状态下的Toast背景颜色
 * @param homeBg 首页背景颜色
 */
@Immutable
data class CustomColorPalette(
    val surface: Color = Color.Unspecified,
    val onSurface: Color = Color.Unspecified,
    val textTitle: Color = Color.Unspecified,
    val textPrimary: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val textHint: Color = Color.Unspecified,
    val textError: Color = Color.Unspecified,
    val themePrimary: Color = Color.Unspecified,
    val themeSecondary: Color = Color.Unspecified,
    val backIcon: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
    val card: Color = Color.Unspecified,
    val toastBg: Color = Color.Unspecified,
    val toastSuccess: Color = Color.Unspecified,
    val toastError: Color = Color.Unspecified,
    val homeBg: Color = Color.Unspecified,
)

val LocalColor = staticCompositionLocalOf { CustomColorPalette() }

val COLOR_DDD = Color(0xFFDDDDDD)
val COLOR_F7F7F7 = Color(0xFFF7F7F7)

/*              亮色            */
val COLOR_SURFACE = Color(0xFFFFFFFF)
val COLOR_ON_SURFACE = Color(0xFF333333)
val COLOR_TEXT_PRIMARY = Color(0xFF333333)
val COLOR_TEXT_SECONDARY = Color(0xFF666666)
val COLOR_TEXT_HINT = Color(0xFF999999)
val COLOR_THEME_PRIMARY = Color(0xFF3A94FF)
val COLOR_THEME_SECONDARY = Color(0xFF82BFFF)
val COLOR_BORDER = Color(0xFFD9D9D9)
val COLOR_CARD = Color(0xFFEBF2FD)
val COLOR_TOAST_BG = Color(0xFF999999)
val COLOR_TOAST_SUCCESS = Color(0xFF7FBF97)
val COLOR_TOAST_ERROR = Color(0xFFFF4545)
val COLOR_TEXT_ERROR = Color(0xFFDC3545)


/*              暗色            */
val COLOR_SURFACE_DARK = Color(0xFF1A1A1A)
val COLOR_ON_SURFACE_DARK = Color(0xFFFFFFFF)
val COLOR_TEXT_PRIMARY_DARK = Color(0xFFFFFFFF)
val COLOR_TEXT_SECONDARY_DARK = Color(0xFFCCCCCC)
val COLOR_TEXT_HINT_DARK = Color(0xFF808080)
val COLOR_THEME_PRIMARY_DARK = Color(0xFF1E5AA8)
val COLOR_THEME_SECONDARY_DARK = Color(0xFF447ACC)
val COLOR_BORDER_DARK = Color(0xFF444444)
val COLOR_CARD_DARK = Color(0xFF333333)
val COLOR_TOAST_BG_DARK = Color(0xFF444444)
val COLOR_TOAST_SUCCESS_DARK = Color(0xFF4CAF50)
val COLOR_TOAST_ERROR_DARK = Color(0xFFFF7F7F)
val COLOR_TEXT_ERROR_DARK = Color(0xFFFF453A)

/**
 * 自定义亮色主题颜色
 */
val LedgerLightColorScheme = CustomColorPalette(
    surface = COLOR_SURFACE,
    onSurface = COLOR_ON_SURFACE,
    textTitle = Color.Black,
    textPrimary = COLOR_TEXT_PRIMARY,
    textSecondary = COLOR_TEXT_SECONDARY,
    textHint = COLOR_TEXT_HINT,
    textError = COLOR_TEXT_ERROR,
    themePrimary = COLOR_THEME_PRIMARY,
    themeSecondary = COLOR_THEME_SECONDARY,
    backIcon = Color.Black,
    border = COLOR_BORDER,
    card = COLOR_CARD,
    toastBg = COLOR_TOAST_BG,
    toastSuccess = COLOR_TOAST_SUCCESS,
    toastError = COLOR_TOAST_ERROR,
    homeBg = COLOR_F7F7F7
)

/**
 * 自定义暗色主题颜色
 */
val LedgerDarkColorScheme = CustomColorPalette(
    surface = COLOR_SURFACE_DARK,
    onSurface = COLOR_ON_SURFACE_DARK,
    textTitle = Color.White,
    textPrimary = COLOR_TEXT_PRIMARY_DARK,
    textSecondary = COLOR_TEXT_SECONDARY_DARK,
    textHint = COLOR_TEXT_HINT_DARK,
    textError = COLOR_TEXT_ERROR_DARK,
    themePrimary = COLOR_THEME_PRIMARY_DARK,
    themeSecondary = COLOR_THEME_SECONDARY_DARK,
    backIcon = Color.White,
    border = COLOR_BORDER_DARK,
    card = COLOR_CARD_DARK,
    toastBg = COLOR_TOAST_BG_DARK,
    toastSuccess = COLOR_TOAST_SUCCESS_DARK,
    toastError = COLOR_TOAST_ERROR_DARK,
    homeBg = COLOR_SURFACE_DARK
)