package cn.moyen.spring.log.enums

// Date: 2024-03-04 10:22

/**
 * 业务类型
 */
enum class ServiceType
{
    /**
     * 新增
     */
    SAVE,

    /**
     * 删除
     */
    REMOVE,

    /**
     * 编辑
     */
    EDIT,

    /**
     * 授权
     */
    GRANT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 生成代码
     */
    GENCODE,

    /**
     * 清除数据
     */
    CLEAN,

    /**
     * 其他
     */
    OTHER,
}