package core.utils

import io.ktor.http.URLProtocol

/**
 * 全局常量
 *
 * @author 高国峰
 * @date 2023/12/7-15:08
 */
object Res {

    object httpClient{
        val server_protocol = URLProtocol.HTTP
        const val server_host = "192.168.1.3"
        const val server_port = 8080
        const val base_path = "dev-api/app/"
    }

    object strings {

        const val str_app_name = "忞鹿记账"
        const val str_logo = "Logo"

        const val str_skip = "跳过"

        const val str_money = "￥"

        const val str_guide1 = "【高效快捷】"
        const val str_guide1_content = "记录生活，掌握财务"
        const val str_guide2 = "【安全可靠】"
        const val str_guide2_content = "隐私至上，金额隐藏"
        const val str_guide3 = "【忞鹿记账】"
        const val str_guide3_content = "财务一目了然，尽在此刻"

        const val str_enter = "进入忞鹿记账"

        const val str_back = "返回"

        const val str_code= "验证码"
        const val str_login_account = "登录账户"
        const val str_hint_login_username = "请输入用户名"
        const val str_hint_login_email = "请输入邮箱"
        const val str_hint_login_password = "请输入密码"
        const val str_hint_login_confirm_password = "请再次输入密码"
        const val str_hint_login_code = "请输入验证码"
        const val str_forget_password = "忘记密码"
        const val str_activate_account = "激活账户"
        const val str_register_account = "注册账户"
        const val str_login = "登录"
        const val str_register = "注册"
        const val str_submit = "提交"

        const val str_read_agree = "我已阅读并同意"
        const val str_terms = "《用户协议》"
        const val str_privacy = "《隐私政策》"
        const val str_and = "和"

        const val str_bill_detail = "账单详情"
        const val str_bill_map = "账单地图"

    }

}