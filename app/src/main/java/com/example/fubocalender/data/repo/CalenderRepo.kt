/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.data.repo

import com.example.fubocalender.data.models.CalenderEntry
import kotlinx.coroutines.flow.Flow

interface CalenderRepo {

    fun getEvents(): Flow<List<CalenderEntry>>

    suspend fun insertEvent(note: CalenderEntry)
}