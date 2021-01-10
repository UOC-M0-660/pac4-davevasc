package edu.uoc.pac4.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.uoc.pac4.databinding.ActivityLoginBinding
import edu.uoc.pac4.ui.login.oauth.OAuthActivity

class LoginActivity : AppCompatActivity() {

    // Binding variable for this activity
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set binding variable for current Activity
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login with Twitch
        binding.twitchLoginButton.setOnClickListener {
            startActivity(Intent(this, OAuthActivity::class.java))
        }


    }
}