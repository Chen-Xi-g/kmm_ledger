package platform

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.alvin.ledger.BaseApp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackHandler

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val userAgent: String
        get() {
            return "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 "
        }
}

actual fun getPlatform(): Platform = AndroidPlatform()

@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> backAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T> =
    predictiveBackAnimation(
        backHandler = backHandler,
        animation = stackAnimation(slide()),
        onBack = onBack,
    )

actual fun ByteArray.base64ToBitmap(): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
    return bitmap.asImageBitmap()
}

actual fun String.log(){
    if (this.isEmpty()) return
    Log.d("KMM_Ledger_Android", this)
}

actual fun goHome(){
    // 友好的退出应用,实际上是调用Home
    // 创建一个隐式 Intent，启动桌面活动
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    // 启动 Intent 返回桌面
    BaseApp.instance.startActivity(intent)
}