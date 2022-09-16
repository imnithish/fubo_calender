/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.ui.screens.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fubocalender.data.models.CalenderEntry
import com.example.fubocalender.data.repo.CalenderRepo
import com.example.fubocalender.util.mergeDateAndTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddScreenVM @Inject constructor(
    private val repo: CalenderRepo
) : ViewModel() {

    private val list = mutableListOf<CalenderEntry>()

    init {
        viewModelScope.launch {
            repo.getEvents().collectLatest {
                list.addAll(it)
            }
        }
    }

    fun saveEvent(
        name: String,
        date: String,
        start: String,
        end: String,
        status: (error: String?) -> Unit
    ) = viewModelScope.launch {
        if (isInThePast(date, start))
            status("Events cannot be scheduled in the past")
        else if (isOverlapping(date, start, end))
            status("Events cannot be scheduled over existing events")
        else {
            repo.insertEvent(
                CalenderEntry(
                    name = name,
                    date = date,
                    startTime = start,
                    endTime = end
                )
            )
            status(null)
        }
    }

    /*
    Events cannot be scheduled in the past (assume current time is Sep 1 6AM for reference)
    Aug 31 10AM-11AM should not be allowed
     */
    private fun isInThePast(
        date: String,
        start: String,
    ): Boolean {
        val mergedDateTime = mergeDateAndTime(date, start)
        val now: LocalDateTime = LocalDateTime.now()
        return mergedDateTime.isBefore(now)
    }

    /*
    Events cannot be scheduled over existing events
     */
    private fun isOverlapping(
        date: String,
        start: String,
        end: String,
    ): Boolean {
        val start1 = mergeDateAndTime(date, start)
        val end1 = mergeDateAndTime(date, end)
        var flag = false
        list.forEach { entry ->
            val start2 = mergeDateAndTime(entry.date, entry.startTime)
            val end2 = mergeDateAndTime(entry.date, entry.endTime)
            flag =
                (start1.isBefore(end2) || start1 == end2) && (start2.isBefore(end1) || start2 == end1)
        }
        return flag
    }


    fun isStartTimeAfterEndTime(start: String, end: String, eventDate: String): Boolean {
        val startingTime = mergeDateAndTime(eventDate, start)
        val endingTime = mergeDateAndTime(eventDate, end)



        Log.d("dasdas", "start ${startingTime.format(DateTimeFormatter.ISO_DATE_TIME)}")
        Log.d("dasdas", "end ${endingTime.format(DateTimeFormatter.ISO_DATE_TIME)}")
        return startingTime.isAfter(endingTime)
    }

    fun isEndTimeBeforeStartTime(start: String, end: String, eventDate: String): Boolean {
        val startingTime = mergeDateAndTime(eventDate, start)
        val endingTime = mergeDateAndTime(eventDate, end)

        Log.d("dasdas", "start ${startingTime.format(DateTimeFormatter.ISO_DATE_TIME)}")
        Log.d("dasdas", "end ${endingTime.format(DateTimeFormatter.ISO_DATE_TIME)}")
        return startingTime.isBefore(endingTime)
    }

}