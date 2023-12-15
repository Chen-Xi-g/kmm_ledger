package platform

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

/**
 * 创建Settings
 */
actual fun createSettings(): Settings {
    val preferences = Preferences.userRoot()
    return PreferencesSettings(preferences)
}