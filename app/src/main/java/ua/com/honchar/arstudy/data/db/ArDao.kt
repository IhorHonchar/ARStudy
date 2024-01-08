package ua.com.honchar.arstudy.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ua.com.honchar.arstudy.data.db.languages.DbLanguage
import ua.com.honchar.arstudy.data.db.user.DbUser

@Dao
interface ArDao {

    @Insert
    suspend fun insertAll(vararg users: DbLanguage)

    @Query("select * from language")
    suspend fun getLanguages(): List<DbLanguage>

    @Insert
    suspend fun insertUser(user: DbUser)

    @Query("select * from user where id = 0")
    suspend fun getUserData(): DbUser?

    @Query("delete from user where id = 0")
    suspend fun clearUser()
}