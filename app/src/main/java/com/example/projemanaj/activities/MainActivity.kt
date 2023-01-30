package com.example.projemanaj.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.projemanaj.R
import com.example.projemanaj.firebase.FirestoreClass
import com.example.projemanaj.models.user
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity() ,NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupactionbar()
        nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loaduserdata(this)
    }

    companion object{
        const val my_profile_req_code = 11
    }

    private fun setupactionbar(){
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_nav_menu)
        toolbar_main_activity.setNavigationOnClickListener{
            //Toggle drawer
            toggledrawer()
        }
    }

    private fun toggledrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode== my_profile_req_code){
            FirestoreClass().loaduserdata(this)
        }else{
            Toast.makeText(this,"Not Updated Anything",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.nav_my_profile->{
                startActivityForResult(Intent(this, MyProfileActivity::class.java),
                    my_profile_req_code)
            }


            R.id.nav_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNzvigationUserDeyails(loggedinuser: user?) {
        if (loggedinuser != null) {
            Glide
                .with(this)
                .load(loggedinuser.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(nav_user_image)

            tv_username.text=loggedinuser.name
        }
    }

}