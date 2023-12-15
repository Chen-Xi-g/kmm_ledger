package core.data.repository

import core.data.api.NetApi
import core.data.dto.TokenDto
import core.data.net.ResNet
import core.data.net.post
import core.domain.entity.LoginEntity
import core.domain.repository.AccountRepository
import core.mappers.toToken

/**
 * 账户相关接口实现
 *
 * @author 高国峰
 * @date 2023/12/16-01:11
 */
class AccountRepositoryImpl: AccountRepository{

    override suspend fun login(
        username: String,
        password: String,
        code: String,
        uuid: String
    ): ResNet<LoginEntity> {
        val req = NetApi.AccountApi.Login(username, password, code, uuid)
        return post<TokenDto>(req.url, req.toMap()).toToken()
    }

}