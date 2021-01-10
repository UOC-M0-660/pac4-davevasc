package edu.uoc.pac4.ui.login.oauth

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import edu.uoc.pac4.ui.LaunchActivity
import edu.uoc.pac4.R
import edu.uoc.pac4.data.network.Endpoints
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.data.oauth.OAuthConstants
import edu.uoc.pac4.databinding.ActivityOauthBinding
import edu.uoc.pac4.ui.streams.StreamsActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by david on 01/01/21.
 * OAuth Activity
 */

class OAuthActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "OAuthActivity"
    }

    // Binding variable for this activity
    private lateinit var binding: ActivityOauthBinding
    // ViewModel injection by Koin
    private val viewModel: OAuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set binding variable for current Activity
        binding = ActivityOauthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launchOAuthAuthorization()
    }

    private fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.authorizationUrl)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", OAuthConstants.scopes.joinToString(separator = " "))
            .appendQueryParameter("state", OAuthConstants.uniqueState)
            .build()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        // Set webView Redirect Listener
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == OAuthConstants.uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d("OAuth", "Here is the authorization code! $code")
                                onAuthorizationCodeRetrieved(code)
                                // Hide WebView
                                binding.webView.visibility = View.GONE
                            } ?: run {
                                // User cancelled the login flow
                                // Handle error
                                Toast.makeText(
                                    this@OAuthActivity,
                                    getString(R.string.error_oauth),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // Load OAuth Uri
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {
        // Show Loading Indicator
        binding.progressBar.visibility = View.VISIBLE

        try {
            // Do login in Twitch by ViewModel
            viewModel.doLogin(authorizationCode)
        } catch (t: UnauthorizedException) {
            // Log error
            Log.w(TAG, "Unauthorized Error getting tokens", t)
        }

        // Observer of login boolean
        viewModel.login.observe(this) { login ->

            if (login) {
                // Login OK :) -> Open Streams Activity
                startActivity(Intent(this@OAuthActivity, StreamsActivity::class.java))
            } else {
                // Login ERROR :( -> Show Error Message and LaunchActivity
                Toast.makeText(
                        this@OAuthActivity,
                        getString(R.string.error_oauth),
                        Toast.LENGTH_LONG
                ).show()
                // Clear tokens
                viewModel.logout()
                // Restart app to navigate and try again
                startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
            }
            // Finish current Activity
            finish()
            // Hide Loading Indicator
            binding.progressBar.visibility = View.GONE
        }
    }

}