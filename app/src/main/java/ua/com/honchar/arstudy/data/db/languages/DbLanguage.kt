package ua.com.honchar.arstudy.data.db.languages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language")
data class DbLanguage(
    @PrimaryKey
    val id: Int,
    val code: String
)
