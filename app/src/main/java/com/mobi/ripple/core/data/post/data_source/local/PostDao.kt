package com.mobi.ripple.core.data.post.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostEntity

@Dao
interface PostDao {
    @Upsert
    suspend fun upsertAll(simplePosts: List<PostEntity>)

    @Query(
        "SELECT * FROM postentity " +
                "ORDER BY creationDate DESC " +
                "LIMIT :pageSize " +
                "OFFSET :pageIndex * :pageSize"
    )
    fun getPage(pageIndex: Int, pageSize: Int): MutableList<PostEntity>

    @Query("SELECT * FROM postentity " +
            "ORDER BY creationDate DESC")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM postentity " +
            "ORDER BY creationDate DESC " +
            "LIMIT 1")
    fun getFirst(): PostEntity

    @Query("SELECT * FROM postentity " +
            "ORDER BY creationDate ASC " +
            "LIMIT 1")
    fun getLast(): PostEntity


    @Query(
        "SELECT * FROM postentity " +
                "ORDER BY creationDate DESC " +
                "LIMIT 1 " +
                "OFFSET :index"
    )
    fun getPost(index: Int): PostEntity

    @Update
    fun updatePost(post: PostEntity)

    @Query("Delete FROM postentity")
    suspend fun clearAll()
}