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
 * @param back 返回图标
 * @param uncheck 未选中图标
 * @param check 选中图标
 * @param map 地图图标
 * @param visible 可见图标
 * @param invisible 不可见图标
 * @param dateFilter 日期筛选图标
 * @param scrollTop 滚动到顶部图标
 */
@Immutable
data class CustomDrawable(
    val logo: String = "",
    val splashBg: String = "",
    val splashBgLarge: String = "",
    val guide1: String = "",
    val guide2: String = "",
    val guide3: String = "",
    val back: String = "",
    val uncheck: String = "",
    val check: String = "",
    val map: String = "",
    val visible: String = "",
    val invisible: String = "",
    val dateFilter: String = "",
    val scrollTop: String = ""
)

val LocalDrawable = staticCompositionLocalOf { CustomDrawable() }

const val LOGO = "composeRes/images/ic_splash_logo.png"

const val IC_BACK_ICON = "composeRes/images/ic_back.xml"
const val IC_UNCHECK_ICON = "composeRes/images/ic_check_un.xml"
const val IC_CHECK_ICON = "composeRes/images/ic_check.xml"
const val IC_MAP = "composeRes/images/ic_map.xml"
const val IC_VISIBLE = "composeRes/images/ic_visible.xml"
const val IC_INVISIBLE = "composeRes/images/ic_invisible.xml"
const val IC_DATE_FILTER = "composeRes/images/ic_date_filter.xml"

/*              亮色            */
const val BG_SPLASH_LIGHT = "composeRes/images/bg_splash_light.png"
const val BG_SPLASH_LIGHT_LARGE = "composeRes/images/bg_splash_light_large.jpeg"
const val BG_SPLASH_LIGHT_GUIDE1 = "composeRes/images/ic_guide_1.png"
const val BG_SPLASH_LIGHT_GUIDE2 = "composeRes/images/ic_guide_2.png"
const val BG_SPLASH_LIGHT_GUIDE3 = "composeRes/images/ic_guide_3.png"
const val IC_SCROLL_TOP = "composeRes/images/ic_scroll_top.png"

/*              暗色            */
const val BG_SPLASH_DARK = "composeRes/images/bg_splash_dark.png"
const val BG_SPLASH_DARK_LARGE = "composeRes/images/bg_splash_dark_large.jpeg"
const val BG_SPLASH_DARK_GUIDE1 = "composeRes/images/ic_guide_1_dark.png"
const val BG_SPLASH_DARK_GUIDE2 = "composeRes/images/ic_guide_2_dark.png"
const val BG_SPLASH_DARK_GUIDE3 = "composeRes/images/ic_guide_3_dark.png"
const val IC_SCROLL_TOP_DARK = "composeRes/images/ic_scroll_top_dark.png"

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
    back = IC_BACK_ICON,
    uncheck = IC_UNCHECK_ICON,
    check = IC_CHECK_ICON,
    map = IC_MAP,
    visible = IC_VISIBLE,
    invisible = IC_INVISIBLE,
    dateFilter = IC_DATE_FILTER,
    scrollTop = IC_SCROLL_TOP
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
    back = IC_BACK_ICON,
    uncheck = IC_UNCHECK_ICON,
    check = IC_CHECK_ICON,
    map = IC_MAP,
    visible = IC_VISIBLE,
    invisible = IC_INVISIBLE,
    dateFilter = IC_DATE_FILTER,
    scrollTop = IC_SCROLL_TOP_DARK
)