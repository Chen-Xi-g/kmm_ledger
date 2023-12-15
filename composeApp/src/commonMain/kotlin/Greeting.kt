import platform.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    fun userAgent(): String {
        return platform.userAgent
    }
}