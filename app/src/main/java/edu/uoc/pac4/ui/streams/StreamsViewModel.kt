package edu.uoc.pac4.ui.streams

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 11/21/20.
 * Updated by david on 01/01/21.
 * Streams Activity View Model
 */

class StreamsViewModel(
        private val repository: StreamsRepository
) : ViewModel() {

    // Live Data
    val page = MutableLiveData<String>()
    val streams = MutableLiveData<List<Stream>>()

    // Public function that can be called from the view (Activity)
    fun getStreams(cursor: String?) {
        // Get Availability from Repository and post result
        viewModelScope.launch {
            page.postValue(repository.getStreams(cursor).first)
            streams.postValue(repository.getStreams(cursor).second)

        }
    }

    // Call to clear access token
    fun clearAccessToken() {
        repository.clearAccessToken()
    }

}