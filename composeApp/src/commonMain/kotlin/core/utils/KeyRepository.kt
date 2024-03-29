package core.utils

import com.russhwolf.settings.boolean
import com.russhwolf.settings.string
import core.data.dto.UserInfoDto
import platform.createSettings

/**
 * 持久化存储键对值
 *
 * @author 高国峰
 * @date 2023/12/8-11:50
 */
object KeyRepository {
    private val settings = createSettings()

    /**
     * 是否第一次启动
     */
    var isFirstLaunch: Boolean by settings.boolean(defaultValue = true)

    /**
     * Token
     */
    var token: String by settings.string(defaultValue = "")

    /**
     * 用户名
     */
    var username: String by settings.string(defaultValue = "")

    /**
     * 昵称
     */
    var nickName: String by settings.string(defaultValue = "")

    /**
     * 邮箱
     */
    var email: String by settings.string(defaultValue = "")

    fun clear() {
        token = ""
    }

    fun clearAll() {
        settings.clear()
    }
}