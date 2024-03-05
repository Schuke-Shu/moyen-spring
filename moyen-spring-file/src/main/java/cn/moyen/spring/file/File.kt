package cn.moyen.spring.file

import cn.moyen.spring.core.exception.SystemException
import cn.moyen.spring.core.util.logger
import io.minio.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URI

// 文件模块
// Date: 2024-03-05 11:02

@Configuration
class FIleConfig
{
    private val log = logger<FIleConfig>()

    init
    {
        log.info("Configuring moyen-file")
    }
}

@ConfigurationProperties("moyen.file.minio")
class MinioProperties
{
    /**
     * 是否启用 MinIO
     */
    var enable: Boolean? = true

    /**
     * MinIO 地址
     */
    var address: String? = null

    /**
     * 用户名
     */
    var username: String? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 桶名称
     */
    var defaultBucket: String? = null

    /**
     * 最小切片尺寸（单位：字节）
     */
    var minMultipartSize: Long? = 5242880
}

/**
 * 文件传输接口
 */
interface FileService
{
    /**
     * 创建容器
     */
    fun make(name: String)

    /**
     * 上传文件
     */
    fun upload(filename: String, input: InputStream, target: String = defaultTarget())

    /**
     * 下载文件
     */
    fun download(filename: String, target: String = defaultTarget()): InputStream

    /**
     * 删除文件
     */
    fun delete(filename: String, target: String = defaultTarget())

    /**
     * 默认目标容器名称（例如：文件夹或 MinIO 桶的名称）
     */
    fun defaultTarget(): String
}

@Service
@Import(MinioProperties::class)
@ConditionalOnProperty(prefix = "moyen.file.minio", name = ["enable"], havingValue = "true", matchIfMissing = true)
class MinioFileServiceImpl : FileService
{
    private val log = logger<MinioFileServiceImpl>()

    init
    {
        log.info("Initializing default file service implementation (use MinIO)")
    }

    @set:Autowired
    lateinit var props: MinioProperties

    override fun make(name: String) = client().io {
        if (this.bucketExists(BucketExistsArgs.builder().bucket(name).build()))
            this.makeBucket(MakeBucketArgs.builder().bucket(name).build())
    }

    override fun upload(filename: String, input: InputStream, target: String): Unit = client().io {
        this.putObject(
            PutObjectArgs
                .builder()
                .bucket(target)
                .`object`(filename)
                .stream(input, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE.toLong())
                .contentType("application/octet-stream")
                .build()
        )
    }

    override fun download(filename: String, target: String): InputStream = client().io {
        this.getObject(
            GetObjectArgs
                .builder()
                .bucket(target)
                .`object`(filename)
                .build()
        )
    }

    override fun delete(filename: String, target: String) = client().io {
        this.removeObject(RemoveObjectArgs.builder().bucket(target).`object`(filename).build())
    }

    private fun client(): MinioClient =
        log.run {
            if (isDebugEnabled) debug("Connecting to MinIO: ${props.address}, username: ${props.username}")
            try
            {
                MinioClient
                    .builder()
                    .endpoint(URI.create(props.address!!).toURL())
                    .credentials(props.username, props.password)
                    .build()
            }
            catch (e: Exception)
            {
                throw MinIOException(e)
            }
        }

    override fun defaultTarget(): String = props.defaultBucket!!
}

inline fun <R> MinioClient.io(block: MinioClient.() -> R): R =
    try
    {
        block()
    }
    catch (e: Exception)
    {
        throw MinIOException(e)
    }

class MinIOException(cause: Throwable) : SystemException(cause = cause)