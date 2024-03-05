package cn.moyen.spring.security.entity

import cn.jruyi.util.Assert
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

// Date: 2024-03-04 14:48

class AuthToken(val principle: Any, authorities: Collection<GrantedAuthority>) :
    AbstractAuthenticationToken(authorities)
{
    init
    {
        isAuthenticated = true
    }

    override fun getCredentials(): Any = throw UnsupportedOperationException()

    override fun getPrincipal(): Any = principle

    override fun setAuthenticated(authenticated: Boolean) =
        Assert
            .isFalse(
                isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
            )
            .run {
                super.setAuthenticated(false)
            }

    /**
     * 若已经验证过，返回父类的 toString()，否则只返回 token
     */
    override fun toString(): String = if (isAuthenticated) super.toString() else "Token: $principal"
}