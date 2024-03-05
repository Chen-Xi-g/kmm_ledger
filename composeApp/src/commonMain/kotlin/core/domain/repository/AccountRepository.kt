package core.domain.repository

import core.data.api.NetApi
import core.data.dto.UserAccountDto
import core.data.dto.UserInfoDto
import core.data.net.ResNet
import core.domain.entity.CodeImageEntity

/**
 * 账户相关存储库
 *
 * @author 高国峰
 * @date 2023/12/16-01:08
 */
interface AccountRepository {

    /**
     * 获取验证码
     *
     * @return 返回图片地址
     */
    suspend fun codeImage(req: NetApi.AccountApi.CodeImage = NetApi.AccountApi.CodeImage): ResNet<CodeImageEntity>

    /**
     * 登录
     *
     * @param code 输入的验证码
     * @param uuid 验证码唯一ID
     *
     * @return 返回Token信息
     */
    suspend fun login(username: String, password: String, code: String, uuid: String): ResNet<String>

    /**
     * 注册
     *
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @param code 验证码
     * @param uuid 验证码唯一ID
     *
     * @return 返回是否注册成功
     */
    suspend fun register(username: String, email: String, password: String, code: String, uuid: String): ResNet<String>

    /**
     * 忘记密码
     *
     * @param username 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @param code 验证码
     * @param uuid 验证码唯一ID
     *
     * @return 发送修改密码确认邮箱
     */
    suspend fun forgotPwd(username: String, password: String, confirmPassword: String, code: String, uuid: String): ResNet<String>

    /**
     * 发送激活账户的邮件
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 验证码唯一ID
     */
    suspend fun activateAccount(username: String, code: String, uuid: String): ResNet<String>

    /**
     * 获取系统协议
     *
     * @param type 协议类型 1:用户协议 2:隐私政策
     */
    suspend fun agreement(type: Int): ResNet<String>

    /**
     * 登出
     */
    suspend fun logout(): ResNet<String>

    /**
     * 获取用户信息
     */
    suspend fun userInfo(): ResNet<UserInfoDto>

    /**
     * 修改昵称
     *
     * @param nickName 昵称
     */
    suspend fun modifyUserInfo(nickName: String): ResNet<String>

    /**
     * 获取我的账户列表
     */
    suspend fun accountList(): ResNet<List<UserAccountDto>>
}