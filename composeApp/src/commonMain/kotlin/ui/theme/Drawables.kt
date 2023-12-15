package ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 自定义应用内明暗主题资源
 *
 * 可以通过[Google Fonts Material Icons](https://fonts.google.com/icons?icon.set=Material+Icons)寻找自己想要使用的图标.
 * 配合tint修改图标颜色
 *
 * 个人资源图片放置在[resources]目录下,可自定义路径存放目录
 *
 * @param logo logo图片路径
 * @param splashBg 启动页图片
 * @param splashBgLarge 启动页大图
 */
@Immutable
data class CustomDrawable(
    val logo: String = "",
    val splashBg: String = "",
    val splashBgLarge: String = ""
)

val LocalDrawable = staticCompositionLocalOf { CustomDrawable() }

const val LOGO = "composeRes/images/ic_splash_logo.png"

/*              亮色            */
const val BG_SPLASH_LIGHT = "composeRes/images/bg_splash_light.png"
const val BG_SPLASH_LIGHT_LARGE = "composeRes/images/bg_splash_light_large.jpeg"

/*              暗色            */
const val BG_SPLASH_DARK = "composeRes/images/bg_splash_dark.png"
const val BG_SPLASH_DARK_LARGE = "composeRes/images/bg_splash_dark_large.jpeg"

/**
 * 自定义亮色主题资源
 */
val LedgerLightDrawable = CustomDrawable(
    logo = LOGO,
    splashBg = BG_SPLASH_LIGHT,
    splashBgLarge = BG_SPLASH_LIGHT_LARGE
)

/**
 * 自定义暗色主题资源
 */
val LedgerDarkDrawable = CustomDrawable(
    logo = LOGO,
    splashBg = BG_SPLASH_DARK,
    splashBgLarge = BG_SPLASH_DARK_LARGE
)