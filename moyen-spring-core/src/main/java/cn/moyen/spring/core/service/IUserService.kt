package cn.moyen.spring.core.service

import cn.moyen.spring.core.pojo.IUser

// Date: 2024-03-04 10:42

/**
 * 用户服务接口
 */
interface IUserService<U: IUser>
{
    /**
     * 当前登录的用户
     */
    fun thisUser(): U
}