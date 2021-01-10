package edu.uoc.pac4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.databinding.ActivityLaunchBinding
import edu.uoc.pac4.ui.streams.StreamsActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by david on 01/01/21.
 * Launch Activity
 */

class LaunchActivity : AppCompatActivity() {

    // Binding variable for this activity
    private lateinit var binding: ActivityLaunchBinding
    // ViewModel injection by Koin
    private val viewModel: LaunchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set binding variable for current Activity
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUserSession()
    }

    private fun checkUserSession() {

        // Get user availability from ViewModel
        viewModel.getUserAvailability()

        // Observer of user availability
        viewModel.isUserAvailable.observe(this) { user ->
            if (user) {
                // User is available, open directly Streams Activity
                startActivity(Intent(this, StreamsActivity::class.java))
            } else {
                // User not available, request Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }

}