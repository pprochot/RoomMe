package uj.roomme.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import uj.roomme.R
import uj.roomme.RegisterFragmentDirections

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.title = "Sign up"
    }
}