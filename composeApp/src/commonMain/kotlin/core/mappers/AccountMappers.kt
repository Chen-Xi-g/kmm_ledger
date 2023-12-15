package core.mappers

import core.data.dto.TokenDto
import core.data.net.ResNet
import core.domain.entity.LoginEntity

/**
 * Token传输对象转换为View使用的成功响应实体类
 */
fun ResNet<TokenDto>.toToken() = mapData(LoginEntity(token = data?.token ?: "", xxx = "自定义数据"))