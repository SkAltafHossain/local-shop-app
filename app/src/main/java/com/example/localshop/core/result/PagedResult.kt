package com.example.localshop.core.result

data class PagedResult<T>(
    val items: List<T>,
    val currentPage: Int,
    val lastPage: Int,
    val total: Int,
    val perPage: Int,
    val from: Int? = null,
    val to: Int? = null
) {
    val hasNextPage: Boolean
        get() = currentPage < lastPage
    
    val hasPreviousPage: Boolean
        get() = currentPage > 1
    
    val isFirstPage: Boolean
        get() = currentPage == 1
    
    val isLastPage: Boolean
        get() = currentPage == lastPage
    
    companion object {
        fun <T> empty(pageSize: Int = 12): PagedResult<T> {
            return PagedResult(
                items = emptyList(),
                currentPage = 1,
                lastPage = 1,
                total = 0,
                perPage = pageSize,
                from = null,
                to = null
            )
        }
    }
}