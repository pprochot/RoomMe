package uj.roomme.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.R

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(R.layout.login_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Sign up"
    }
}