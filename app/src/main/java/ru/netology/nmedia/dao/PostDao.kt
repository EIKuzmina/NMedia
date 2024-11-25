package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: List<PostEntity>)

    @Query("UPDATE PostEntity SET likes = likes + 1, likedByMe = NOT likedByMe WHERE id = :id")
    suspend fun likeById(id: Int)

    @Query("UPDATE PostEntity SET likes = likes - 1, likedByMe = NOT likedByMe WHERE id = :id")
    suspend fun dislikeById(id: Int)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Int)
}