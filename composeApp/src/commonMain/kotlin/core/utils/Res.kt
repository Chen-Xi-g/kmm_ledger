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
        const val server_host = "192.168.0.105"
        const val server_port = 8080
        val String.base_path
            get() = "dev-api/app/$this"
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
        const val str_confirm = "确定"
        const val str_cancel = "取消"
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
        const val str_bill_date_filter = "账单日期筛选"
        const val str_pack_up = "收起"
        const val str_expand = "展开"
        const val str_no_bill = "当前还没有添加账单，快去添加吧"
        const val str_add_bill = "添加账单"
        const val str_scroll_to_top = "回到顶部"
        const val str_last_year = "上一年"
        const val str_this_year = "下一年"
        const val str_income_expenditure_type = "收支类型"
        const val str_bill_date = "账单日期"
        const val str_account_type = "账户类型"
        const val str_query = "查询"
        const val str_no_more = "没有更多了"
        const val str_income = "收入"
        const val str_expenditure = "支出"
        const val str_next_step = "下一步"


        val String.str_format_income
            get() = "收入：${this}￥"

        val String.str_format_expenditure
            get() = "支出：${this}￥"

    }

}