package org.example.test.business

import cn.moyen.spring.core.pojo.BaseUser
import cn.moyen.spring.core.service.IUserService
import cn.moyen.spring.data.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

// Date: 2024-03-05 10:27

interface UserService: IUserService<User>

@Mapper
interface UserMapper: BaseMapper<User>

class User: BaseUser()