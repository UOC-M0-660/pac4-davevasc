package edu.uoc.pac4.ui.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.pac4.R
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.databinding.ActivityStreamsBinding
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.ui.profile.ProfileActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by david on 01/01/21.
 * Streams Activity.
 */

class StreamsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "StreamsActivity"
    }

    // Binding variable for this activity
    private lateinit var binding: ActivityStreamsBinding
    // ViewModel injection by Koin
    private val viewModel: StreamsViewModel by viewModel()

    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)
    private var nextCursor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set binding variable for current Activity
        binding = ActivityStreamsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Init RecyclerView
        initRecyclerView()
        // Set Swipe to Refresh Listener
        setSwipeRefreshListener()
        // Get Streams
        getStreams()
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        binding.recyclerView.layoutManager = layoutManager
        // Set Adapter
        binding.recyclerView.adapter = adapter
        // Set Pagination Listener
        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                getStreams(nextCursor)
            }

            override fun isLastPage(): Boolean {
                return nextCursor == null
            }

            override fun isLoading(): Boolean {
                return binding.swipeRefreshLayout.isRefreshing
            }
        })
    }

    /** Set Swipe refresh automatic listener **/
    private fun setSwipeRefreshListener() {
        // Swipe to Refresh Listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            //getStreams()
            getStreams()
        }
    }

    /** Get Streams from Twitch **/
    private fun getStreams(cursor: String? = null) {
        Log.d(TAG, "Requesting streams with cursor $cursor")

        // Show Loading
        binding.swipeRefreshLayout.isRefreshing = true

        try {
        // Get streams from ViewModel
        viewModel.getStreams(cursor)
        } catch (t: UnauthorizedException) {
            Log.w(TAG, "Unauthorized Error getting streams", t)
            onUnauthorized()
        }

        // Observer of cursor
        viewModel.page.observe(this) { newCursor ->
            // Save cursor for next request
            nextCursor = newCursor
        }

        // Observer of streams
        viewModel.streams.observe(this) { streams ->

            if (streams != null) {
                // Success :)
                Log.d("StreamsActivity", "Got Streams: $streams")
                // Update UI with Streams
                if (cursor != null) {
                    // We are adding more items to the list
                    adapter.submitList(adapter.currentList.plus(streams))
                } else {
                    // It's the first n items, no pagination yet
                    adapter.submitList(streams)
                }
            } else {
                // Error :(
                // Show Error message to not leave the page empty
                if (adapter.currentList.isNullOrEmpty()) {
                    Toast.makeText(
                        this@StreamsActivity,
                        getString(R.string.error_streams), Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Hide Loading
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun onUnauthorized() {
        // Clear local access token
        viewModel.clearAccessToken()
        // User was logged out, close screen and open login
        finish()
        startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
    }

    // region Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate Menu
        menuInflater.inflate(R.menu.menu_streams, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_user -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion

}