/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.data.repo

import com.example.fubocalender.data.CalenderDao
import com.example.fubocalender.data.models.CalenderEntry
import kotlinx.coroutines.flow.Flow

class CalenderRepoImpl(
    private val dao: CalenderDao
) : CalenderRepo {

    override fun getEvents(): Flow<List<CalenderEntry>> {
        return dao.getEvents()
    }

    override suspend fun insertEvent(note: CalenderEntry) {
        dao.insertEvent(note)
    }


}