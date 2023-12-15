package core.data.net

import core.utils.GlobalNavigator

/**
 * @author 高国峰
 * @date 2023/8/28-21:08
 * @description 网络响应封装
 */
sealed class ResNet<T>(
    val data: T? = null,
    val msg: String? = null,
    val code: Int = 0
) {
    /**
     * 请求成功
     *
     * @param T 响应的数据类型
     * @param data 响应的数据
     *
     * @return 响应体
     */
    class Success<T>(data: T?, msg: String? = null) : ResNet<T>(data = data, msg = msg, code = 200)

    /**
     * 请求失败
     *
     * @param T 响应的数据我俩
     * @param message 描述信息
     * @param data 错误的数据
     *
     * @return 响应体
     */
    class Error<T>(message: String? = null, code: Int = 500, data: T? = null) :
        ResNet<T>(data, message, code)

    fun isSuccess() = code == 200

    inline fun <reified T> mapData(data: T?): ResNet<T>{
        return if (isSuccess()){
            Success(data, msg)
        }else{
            if (code == 401){
                // 登录失效, 重新登录
                GlobalNavigator.login()
            }
            Error(msg, code, data)
        }
    }
}
