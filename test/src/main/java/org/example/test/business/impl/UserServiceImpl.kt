package org.example.test.business.impl

import org.example.test.business.User
import org.example.test.business.UserService
import org.springframework.stereotype.Service

// Date: 2024-03-05 10:35

@Service
class UserServiceImpl: UserService
{
    override fun thisUser(): User
    {
        return User()
    }
}