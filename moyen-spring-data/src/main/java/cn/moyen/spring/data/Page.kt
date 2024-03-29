package cn.moyen.spring.data

import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.slf4j.LoggerFactory

// 分页工具
// Date: 2024-02-29 19:29

/**
 * 分页数据类
 */
interface Pagination
{
    /**
     * 当前所在页数
     */
    var pageNum: Int?

    /**
     * 每页元素数
     */
    var pageSize: Int?

    /**
     * 导航数字数量
     */
    var navNum: Int?

    /**
     * 排序字段名称
     */
    var orderBy: String?
}

/**
 * 分页数据抽象类
 */
abstract class BasePagination : Pagination
{
    @Schema(description = "当前所在页数")
    override var pageNum: Int? = null

    @Schema(description = "每页元素数")
    override var pageSize: Int? = null

    @Schema(description = "导航数字数量")
    override var navNum: Int? = null

    @Schema(description = "排序字段名称")
    override var orderBy: String? = null
}

private val log = LoggerFactory.getLogger("cn.schuke.website.util.PageUtil")

const val FIRST_PAGE = 1
const val ONE_PER_PAGE = 1

/**
 * 快速分页
 */
fun <E, P : Pagination> pagination(page: P, selector: (Pagination) -> Unit, pageSetter: (Page<E>) -> Unit = { _ -> }) =
    startPage(page.pageNum, page.pageSize, pageSetter).run {
        val ob = page.orderBy
        if (ob !== null && ob.isNotBlank()) this.setOrderBy<E>(ob)
        selector.invoke(page)
        page(this, page.navNum)
    }

/**
 * 快速分页
 */
fun <E> pagination(pageNum: Int?, pageSize: Int?, navNum: Int?, selector: () -> Unit, orderBy: String) =
    pagination(pageNum, pageSize, navNum, selector) { page: Page<E> -> page.setOrderBy<E>(orderBy) }

/**
 * 快速分页
 */
fun <E> pagination(
    pageNum: Int?, pageSize: Int?, navNum: Int?, selector: () -> Unit,
    pageSetter: (Page<E>) -> Unit = { _ -> }
) = selector.run { startPage(pageNum, pageSize, pageSetter).run { invoke(); page(this, navNum) } }

/**
 * 开始分页
 */
fun <E> startPage(pageNum: Int?, pageSize: Int?, orderBy: String): Page<E> = startPage(pageNum, pageSize)
{ page ->
    page.setOrderBy<E>(orderBy)
}

/**
 * 开始分页
 */
fun <E> startPage(pageNum: Int?, pageSize: Int?, pageSetter: (Page<E>) -> Unit = { _ -> }): Page<E> =
    log.run {
        val num = if (pageNum === null || pageNum < 1) FIRST_PAGE else pageNum
        val size = if (pageSize === null || pageSize < 1) ONE_PER_PAGE else pageSize

        PageHelper.clearPage()
        PageHelper.startPage<E>(num, size)
            .apply(pageSetter)
            .also { debug("Start pagination, page number [{}], maximum data per page [{}]", num, size) }
    }

/**
 * 获取分页数据
 */
fun <T> page(list: List<T>, navNum: Int? = null) =
    log.run {
        with(navNum ?: PageInfo.DEFAULT_NAVIGATE_PAGES)
        {
            debug("Get pagination data, the maximum number of navigation pages is [{}]", navNum)
            PageInfo<T>(list, this)
        }
    }
