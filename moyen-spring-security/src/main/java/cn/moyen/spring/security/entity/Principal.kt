package cn.moyen.spring.security.entity

// Date: 2024-03-04 14:39

/**
 * 当事人实体接口
 */
interface Principal
{
    val id: Long

    val username: String

    val ip: String

    val expireTime: Long
}