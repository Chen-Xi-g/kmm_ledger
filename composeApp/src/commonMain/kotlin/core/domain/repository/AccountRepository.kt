package core.domain.repository

import core.data.net.ResNet
import core.domain.entity.LoginEntity

/**
 * 账户相关接口
 *
 * @author 高国峰
 * @date 2023/12/16-01:08
 */
interface AccountRepository {

    /**
     * TODO 登录示例接口
     *
     * @param code 输入的验证码
     * @param uuid 验证码唯一ID
     *
     * @return 返回Token信息
     */
    suspend fun login(username: String, password: String, code: String, uuid: String): ResNet<LoginEntity>

}