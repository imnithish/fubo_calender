/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fubocalender.data.models.CalenderEntry

@Database(
    entities = [CalenderEntry::class],
    version = 1
)
abstract class CalenderDatabase: RoomDatabase() {

    abstract val calenderDao: CalenderDao

    companion object {
        const val DATABASE_NAME = "calender_db"
    }
}