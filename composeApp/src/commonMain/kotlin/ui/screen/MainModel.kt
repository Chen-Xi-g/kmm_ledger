package ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import core.navigation.UiEffect
import core.navigation.UiEvent
import core.navigation.UiState

/**
 * 主页事件
 *
 * @author 高国峰
 * @date 2023/12/6-16:38
 */
sealed interface MainEvent: UiEvent

/**
 * 主页状态
 */
data class MainState(
    val menuList: List<Menu> = listOf(
        Menu(selectIcon = Icons.Filled.Home,unSelectIcon = Icons.Outlined.Home, title = "首页"),
        Menu(selectIcon = Icons.Filled.Add,unSelectIcon = Icons.Outlined.Add, title = "新增账单"),
        Menu(selectIcon = Icons.Filled.Person,unSelectIcon = Icons.Outlined.Person, title = "我的")
    )
): UiState

/**
 * 主页菜单
 */
data class Menu(
    val selectIcon: ImageVector,
    val unSelectIcon: ImageVector,
    val title: String
)

sealed interface MainEffect: UiEffect