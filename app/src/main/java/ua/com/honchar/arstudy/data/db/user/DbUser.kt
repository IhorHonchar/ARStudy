package ua.com.honchar.arstudy.data.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class DbUser(
    @PrimaryKey
    val id: Int,
    val login: String,
    val name: String
)
