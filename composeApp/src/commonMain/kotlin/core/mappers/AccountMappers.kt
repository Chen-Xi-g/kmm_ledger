package core.mappers

import core.data.dto.AgreementDto
import core.data.dto.CodeImageDto
import core.data.dto.TokenDto
import core.data.net.ResNet
import core.domain.entity.CodeImageEntity

/**
 * 验证码传输对象转换为View使用的成功响应实体类
 */
fun ResNet<CodeImageDto>.toCodeImageEntity() = mapData(
    CodeImageEntity(
        this.data?.img ?: "",
        this.data?.uuid ?: ""
    )
)

/**
 * Token传输对象转换为View使用的成功响应实体类
 */
fun ResNet<TokenDto>.toToken() = mapData(data?.token ?: "")

/**
 * 系统协议传输对象转换为View使用的成功响应实体类
 */
fun ResNet<AgreementDto>.toAgreement(): ResNet<String> {
    val html = """
    <html>
        <body>
            ${this.data?.agreementContent ?: "暂无数据"}
        </body>
    </html>
""".trimIndent()
    return mapData(html)
}