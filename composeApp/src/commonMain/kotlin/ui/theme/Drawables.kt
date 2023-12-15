package ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 自定义应用内明暗主题资源
 *
 * @param logo logo图片路径
 * @param splashBg 启动页图片
 * @param splashBgLarge 启动页大图
 * @param guide1 引导页1
 * @param guide2 引导页2
 * @param guide3 引导页3
 * @param backIcon 返回图标
 * @param uncheckIcon 未选中图标
 * @param checkIcon 选中图标
 * @param mapIcon 地图图标
 */
@Immutable
data class CustomDrawable(
    val logo: String = "",
    val splashBg: String = "",
    val splashBgLarge: String = "",
    val guide1: String = "",
    val guide2: String = "",
    val guide3: String = "",
    val backIcon: String = "",
    val uncheckIcon: String = "",
    val checkIcon: String = "",
    val mapIcon: String = "",
    val visibleIcon: String = "",
    val invisibleIcon: String = "",
)

val LocalDrawable = staticCompositionLocalOf { CustomDrawable() }

const val LOGO = "composeRes/images/ic_splash_logo.png"

const val IC_BACK_ICON = "composeRes/images/ic_back.xml"
const val IC_UNCHECK_ICON = "composeRes/images/ic_check_un.xml"
const val IC_CHECK_ICON = "composeRes/images/ic_check.xml"
const val ic_MAP = "composeRes/images/ic_map.xml"
const val ic_VISIBLE = "composeRes/images/ic_visible.xml"
const val ic_INVISIBLE = "composeRes/images/ic_invisible.xml"

/*              亮色            */
const val BG_SPLASH_LIGHT = "composeRes/images/bg_splash_light.png"
const val BG_SPLASH_LIGHT_LARGE = "composeRes/images/bg_splash_light_large.jpeg"
const val BG_SPLASH_LIGHT_GUIDE1 = "composeRes/images/ic_guide_1.png"
const val BG_SPLASH_LIGHT_GUIDE2 = "composeRes/images/ic_guide_2.png"
const val BG_SPLASH_LIGHT_GUIDE3 = "composeRes/images/ic_guide_3.png"

/*              暗色            */
const val BG_SPLASH_DARK = "composeRes/images/bg_splash_dark.png"
const val BG_SPLASH_DARK_LARGE = "composeRes/images/bg_splash_dark_large.jpeg"
const val BG_SPLASH_DARK_GUIDE1 = "composeRes/images/ic_guide_1_dark.png"
const val BG_SPLASH_DARK_GUIDE2 = "composeRes/images/ic_guide_2_dark.png"
const val BG_SPLASH_DARK_GUIDE3 = "composeRes/images/ic_guide_3_dark.png"

/**
 * 自定义亮色主题资源
 */
val LedgerLightDrawable = CustomDrawable(
    logo = LOGO,
    splashBg = BG_SPLASH_LIGHT,
    splashBgLarge = BG_SPLASH_LIGHT_LARGE,
    guide1 = BG_SPLASH_LIGHT_GUIDE1,
    guide2 = BG_SPLASH_LIGHT_GUIDE2,
    guide3 = BG_SPLASH_LIGHT_GUIDE3,
    backIcon = IC_BACK_ICON,
    uncheckIcon = IC_UNCHECK_ICON,
    checkIcon = IC_CHECK_ICON,
    mapIcon = ic_MAP,
    visibleIcon = ic_VISIBLE,
    invisibleIcon = ic_INVISIBLE
)

/**
 * 自定义暗色主题资源
 */
val LedgerDarkDrawable = CustomDrawable(
    logo = LOGO,
    splashBg = BG_SPLASH_DARK,
    splashBgLarge = BG_SPLASH_DARK_LARGE,
    guide1 = BG_SPLASH_DARK_GUIDE1,
    guide2 = BG_SPLASH_DARK_GUIDE2,
    guide3 = BG_SPLASH_DARK_GUIDE3,
    backIcon = IC_BACK_ICON,
    uncheckIcon = IC_UNCHECK_ICON,
    checkIcon = IC_CHECK_ICON,
    mapIcon = ic_MAP,
    visibleIcon = ic_VISIBLE,
    invisibleIcon = ic_INVISIBLE
)