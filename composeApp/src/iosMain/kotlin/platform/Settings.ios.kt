package platform

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

/**
 * 创建Settings
 */
actual fun createSettings(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}