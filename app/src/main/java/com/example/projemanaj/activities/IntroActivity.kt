package com.example.projemanaj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projemanaj.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
    }

    fun signupclicked(view: View) {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    fun loginclicked(view: View) {
        startActivity(Intent(this, SigninActivity::class.java))
    }
}