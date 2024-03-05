package cn.moyen.spring.log

import cn.jruyi.util.ArrayUtil
import cn.moyen.spring.core.REQUEST_TIME
import cn.moyen.spring.core.exception.ServiceException
import cn.moyen.spring.core.service.IUserService
import cn.moyen.spring.core.util.*
import cn.moyen.spring.core.web.ERR_OTHER
import cn.moyen.spring.log.enums.Status
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StopWatch
import java.util.*

// Date: 2024-03-04 10:33

@Aspect
class LogAspect
{
    private val log = logger<LogAspect>()

    @set:Autowired
    lateinit var users: IUserService<*>
    @set:Autowired
    lateinit var logs: ILogService

    init
    {
        log.debug("Initializing logging aop")
    }

    @Around("@annotation(anno)")
    fun log(point: ProceedingJoinPoint, anno: Log) =
        SysLog()
            .apply {
                title = anno.title
                description = anno.description
                userId = users.thisUser().id
                userType = anno.userType
                method = getRequestMethod()
                uri = getRequestURI()
                ip = getRequestIp()
                serviceType = anno.serviceType
                params = if (anno.recordParams) getParams(point) else null
                createTime = getRequestAttribute(REQUEST_TIME) as Long?
            }
            .run {
                val watcher = StopWatch()
                val rt: Any?
                try
                {
                    // 放行
                    watcher.start()
                    rt = point.proceed()
                    watcher.stop()

                    result = if (anno.recordResult && rt !== null) toJson(rt, LOG_JSON) else null
                    status = Status.SUCCESS
                }
                catch (e: Throwable)
                {
                    errorCode = if (e is ServiceException) e.code.hex else ERR_OTHER.hex
                    status = Status.FAILED

                    throw e
                }
                finally
                {
                    if (watcher.isRunning) watcher.stop()
                    // 记录执行时间
                    castTime = "${watcher.totalTimeMillis}ms"
                    log.debug("logging: {}", this)

                    logs.record(this)
                }
                rt
            }

    private fun getParams(point: ProceedingJoinPoint) =
        point.args.run {
            if (!ArrayUtil.check(this) { arg -> arg !== null }) null
            else
            {
                val parameters = (point.signature as MethodSignature).method.parameters
                val joiner = StringJoiner(", ")
                for (i in this.indices)
                    if (parameters[i].getAnnotation(LogIgnore::class.java) === null)
                        joiner.add("${parameters[i].name}: ${toJson(this[i], LOG_JSON)}")
                joiner.toString()
            }
        }
}