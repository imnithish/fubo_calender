/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalenderEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String,
    val startTime: String,
    val endTime: String
)