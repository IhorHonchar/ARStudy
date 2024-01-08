package ua.com.honchar.arstudy.data.db

import ua.com.honchar.arstudy.data.db.languages.DbLanguage
import androidx.room.Database
import androidx.room.RoomDatabase
import ua.com.honchar.arstudy.data.db.user.DbUser

@Database(entities = [DbLanguage::class, DbUser::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun arDao(): ArDao
}
