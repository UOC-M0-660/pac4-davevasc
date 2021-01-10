package edu.uoc.pac4.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import edu.uoc.pac4.R
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.databinding.ActivityProfileBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.NumberFormat

/**
 * Created by david on 01/01/21.
 * Profile Activity
 */

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
    }

    // Binding variable for this activity
    private lateinit var binding: ActivityProfileBinding
    // ViewModel injection by Koin
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set binding variable for current Activity
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set update Description Button Listener
        setUpdateDescriptionListener()
        // Set logout Button Listener
        setLogoutListener()
        // Get User Profile
        getUserProfile()
    }

    /** Set update button listener **/
    private fun setUpdateDescriptionListener() {
        binding.updateDescriptionButton.setOnClickListener {
            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            // Update User Description
            updateUserDescription(binding.userDescriptionEditText.text?.toString() ?: "")
        }
    }

    /** Set logout button listener **/
    private fun setLogoutListener() {
        // Logout Button Listener
        binding.logoutButton.setOnClickListener {
            // Logout
            logout()
        }
    }

    /** Get user data from Twitch **/
    private fun getUserProfile() {
        // Show Loading
        binding.progressBar.visibility = VISIBLE
        try {
        // Get user from ViewModel
        viewModel.getUser()
        } catch (t: UnauthorizedException) {
            Log.w(TAG, "Unauthorized Error getting user", t)
            onUnauthorized()
        }
        // Observer of user
        viewModel.userGetted.observe(this) { user ->
            // Update UI with Streams
            if (user != null) {
                // Success :)
                // Update the UI with the user data
                setUserInfo(user)
            } else {
                // Error :(
                showError(getString(R.string.error_profile))
            }
            // Hide Loading
            binding.progressBar.visibility = GONE
        }
    }

    /** Update user description in Twitch by API **/
    private fun updateUserDescription(description: String) {
        // Show Loading
        binding.progressBar.visibility = VISIBLE
        try {
        // Update user by ViewModel
        viewModel.updateUser(description)
        } catch (t: UnauthorizedException) {
            Log.w(TAG, "Unauthorized Error updating description", t)
            onUnauthorized()
        }
        // Observer of user
        viewModel.userUpdate.observe(this) { user ->
            if (user != null) {
                // Success :)
                // Update the UI with the user data
                setUserInfo(user)
            } else {
                // Error :(
                showError(getString(R.string.error_profile))
            }
            // Hide Loading
            binding.progressBar.visibility = GONE
        }
    }

    private fun setUserInfo(user: User) {
        // Set Texts
        binding.userNameTextView.text = user.userName
        binding.userDescriptionEditText.setText(user.description ?: "")
        // Avatar Image
        user.profileImageUrl?.let {
            Glide.with(this)
                .load(user.getSizedImage(it, 128, 128))
                .centerCrop()
                .transform(CircleCrop())
                .into(binding.imageView)
        }
        // Set Profile Views
        val formattedViews = NumberFormat.getInstance().format(user.viewCount)
        binding.viewsText.text = resources.getQuantityString(R.plurals.views_text, user.viewCount, formattedViews)
    }

    /** Logout current session **/
    private fun logout() {
        // Clear local session data from data source
        viewModel.clearAccessToken()
        viewModel.clearRefreshToken()
        // Close this and all parent activities
        finishAffinity()
        // Open Login
        startActivity(Intent(this, LoginActivity::class.java))
    }

    /** onUnauthorized func, delete current access token to try renew it **/
    private fun onUnauthorized() {
        // Clear local access token from data source
        viewModel.clearAccessToken()
        // User was logged out, close screen and all parent screens and open login
        finishAffinity()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    // Override Action Bar Home button to just finish the Activity
    // not to re-launch the parent Activity (StreamsActivity)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }


}