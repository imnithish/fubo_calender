/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.ui.screens.listing

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fubocalender.data.models.CalenderEntry
import com.example.fubocalender.data.repo.CalenderRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class ListingState(
    val events: Map<String, List<CalenderEntry>> = emptyMap()
)

@HiltViewModel
class ListingScreenVM @Inject constructor(
    private val repo: CalenderRepo
) : ViewModel() {

    private val _state = mutableStateOf(ListingState())
    val state: State<ListingState> = _state

    private var job: Job? = null

    init {
        job?.cancel()
        job = repo.getEvents()
            .onEach { events ->
                _state.value = state.value.copy(
                    events = events.groupBy {
                        it.date
                    },
                )
            }
            .launchIn(viewModelScope)
    }

}