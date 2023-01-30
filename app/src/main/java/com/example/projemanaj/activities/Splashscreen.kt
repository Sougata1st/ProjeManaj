package com.example.projemanaj.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projemanaj.R
import com.example.projemanaj.firebase.FirestoreClass

class splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        var currentuserid = FirestoreClass().getcurrentuserid()

        Log.d("currentuserid",currentuserid.toString())

        if (currentuserid.isNotEmpty()){
            intent =Intent(this, MainActivity::class.java)
            startActivity(intent)

        }else{
            intent =Intent(this, IntroActivity::class.java)
            startActivity(intent)

        }
       finish()
    }
}