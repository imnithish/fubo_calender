/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.ui.screens.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fubocalender.ui.components.EventLayout

@Composable
fun ListingScreen(
    navController: NavController,
    listingScreenVM: ListingScreenVM = hiltViewModel()
) {

    val state = listingScreenVM.state.value

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Calender")
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = { navController.navigate("add_screen") }) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                state.events.forEach {
                    item {
                        EventLayout(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            date = it.key,
                            events = it.value
                        )
                    }
                }
            }
        )

    }
}