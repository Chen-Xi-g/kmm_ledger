package platform

import android.content.Context.MODE_PRIVATE
import com.alvin.ledger.BaseApp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

/**
 * 创建Settings
 */
actual fun createSettings(): Settings{
    // 使用BaseApp.instance创建SharedPreferences
    val sharedPreferences = BaseApp.instance.getSharedPreferences("ledger", MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}