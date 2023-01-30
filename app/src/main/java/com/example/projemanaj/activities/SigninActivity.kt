package com.example.projemanaj.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projemanaj.R
import com.example.projemanaj.firebase.FirestoreClass
import com.example.projemanaj.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        supportActionBar?.hide()

        auth = Firebase.auth

    }

    private fun signinuser(){
        val email: String=et_mail_signin.text.toString().trim(){ it <=' '}
        val password: String=et_pass_signin.text.toString().trim(){ it <=' '}

        if (validateform(email,password)){
            showProgressDialog(getString(R.string.please_wait_string))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign in", "signInWithEmail:success")
                       FirestoreClass().loaduserdata(this@SigninActivity)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("sign in", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }

        }

    }

    private fun validateform( email:String , password : String ):Boolean{
        return when{

            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a Email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a Password")
                false
            }
            else -> {
                true
            }
        }
    }

    fun signinclicked(view: View) {
        signinuser()
    }

    fun signInSuccess(loggedinuser: user?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}