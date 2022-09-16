/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fubocalender.data.models.CalenderEntry
import com.example.fubocalender.util.duration

@Composable
fun EventLayout(
    modifier: Modifier,
    date: String,
    events: List<CalenderEntry>
) {

    Card(modifier = modifier, border = BorderStroke(1.dp, MaterialTheme.colors.primary)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = date, color = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.height(12.dp))
            events.forEach {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${it.name}: ${it.startTime} - ${it.endTime} (${it.startTime duration it.endTime})"
                )
            }
        }
    }
}