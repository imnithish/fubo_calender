/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fubocalender.data.models.CalenderEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CalenderDao {

    @Query("SELECT * FROM CALENDERENTRY")
    fun getEvents(): Flow<List<CalenderEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(note: CalenderEntry)

}