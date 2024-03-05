package core.data.repository

import core.data.api.NetApi
import core.data.dto.AgreementDto
import core.data.dto.CodeImageDto
import core.data.dto.TokenDto
import core.data.dto.UserAccountDto
import core.data.dto.UserInfoDto
import core.data.net.ResNet
import core.data.net.get
import core.data.net.post
import core.domain.entity.CodeImageEntity
import core.domain.repository.AccountRepository
import core.mappers.toAgreement
import core.mappers.toCodeImageEntity
import core.mappers.toToken

/**
 * 账户存储库实现
 *
 * @author 高国峰
 * @date 2023/12/16-01:11
 */
class AccountRepositoryImpl: AccountRepository{
    override suspend fun codeImage(req: NetApi.AccountApi.CodeImage): ResNet<CodeImageEntity> {
        return get<CodeImageDto>(req.url).toCodeImageEntity()
    }

    override suspend fun login(
        username: String,
        password: String,
        code: String,
        uuid: String
    ): ResNet<String> {
        val req = NetApi.AccountApi.Login(username, password, code, uuid)
        return post<TokenDto>(req.url, req.toMap()).toToken()
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        code: String,
        uuid: String
    ): ResNet<String> {
        val req = NetApi.AccountApi.Register(username, email, password, code, uuid)
        return post(req.url, req.toMap())
    }

    override suspend fun forgotPwd(
        username: String,
        password: String,
        confirmPassword: String,
        code: String,
        uuid: String
    ): ResNet<String> {
        val req = NetApi.AccountApi.ForgotPwd(username, password, confirmPassword, code, uuid)
        return post(req.url, req.toMap())
    }

    override suspend fun activateAccount(
        username: String,
        code: String,
        uuid: String
    ): ResNet<String> {
        val req = NetApi.AccountApi.ActivateAccount(username, code, uuid)
        return post(req.url, req.toMap())
    }

    override suspend fun agreement(type: Int): ResNet<String> {
        val req = NetApi.AccountApi.Agreement(type)
        return get<AgreementDto>(req.url){
            put("type", type.toString())
        }.toAgreement()
    }

    override suspend fun logout(): ResNet<String> {
        val req = NetApi.AccountApi.Logout
        return post(req.url)
    }

    override suspend fun userInfo(): ResNet<UserInfoDto> {
        val req = NetApi.AccountApi.UserInfo
        return get<UserInfoDto>(req.url)
    }

    override suspend fun modifyUserInfo(nickName: String): ResNet<String> {
        val req = NetApi.AccountApi.ChangeNickName(nickName)
        return post(req.url)
    }

    override suspend fun accountList(): ResNet<List<UserAccountDto>> {
        val req = NetApi.AccountApi.AccountList
        return get(req.url)
    }

}