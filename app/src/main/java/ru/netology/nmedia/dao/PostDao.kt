package ru.netology.nmedia.dao

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.handler.AttachmentType
import ru.netology.nmedia.handler.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity WHERE show = 0 ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM PostEntity")
    suspend fun clear()

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: List<PostEntity>)

    @Query("UPDATE PostEntity SET show = 1")
    suspend fun show()

    @Query("UPDATE PostEntity SET likes = likes + 1, likedByMe = NOT likedByMe WHERE id = :id")
    suspend fun likeById(id: Int)

    @Query("UPDATE PostEntity SET likes = likes - 1, likedByMe = NOT likedByMe WHERE id = :id")
    suspend fun dislikeById(id: Int)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Int)
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}