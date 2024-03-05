package org.example.test.business

import cn.moyen.spring.core.web.Web
import org.apache.ibatis.annotations.Mapper

// Date: 2024-03-05 10:17

@Web(path = "core", title = "核心模块", description = "核心模块测试控制器")
class CoreController

interface CoreService

@Mapper
interface CoreMapper