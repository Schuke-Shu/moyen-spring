package cn.moyen.spring.core

import java.time.format.DateTimeFormatter

// Date: 2024-02-27 14:28

const val MOYEN = "moyen"

const val MOYEN_VERSION = "1.0"

val DEFAULT_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

const val SERVICE_INFO_HEAD = "service.info"

const val REQUEST_TIME = "$SERVICE_INFO_HEAD.request-time"

const val APP_PROP_HEAD = "application.info"