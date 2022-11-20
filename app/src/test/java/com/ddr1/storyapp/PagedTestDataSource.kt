package com.ddr1.storyapp

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ddr1.storyapp.data.local.entity.StoriesEntity

class PagedTestDataSource :
    PagingSource<Int, LiveData<List<StoriesEntity>>>() {

    companion object {
        fun snapshot(items: List<StoriesEntity>): PagingData<StoriesEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}