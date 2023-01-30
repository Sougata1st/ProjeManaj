package com.example.projemanaj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.projemanaj.R
import com.example.projemanaj.firebase.FirestoreClass
import com.example.projemanaj.models.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
    }

    private fun registeruser(){
        val name: String=et_name.text.toString().trim(){ it <=' '}
        val email: String=et_mail.text.toString().trim(){ it <=' '}
        val password: String=et_pass.text.toString().trim(){ it <=' '}
        val confirmpass: String=et_confpass.text.toString().trim(){ it <=' '}

        if (validateform(name,email,password,confirmpass)){
            showProgressDialog(getString(R.string.please_wait_string))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
//                hideProgressDialog()
                if (task.isSuccessful) {
                    val firebaseuser: FirebaseUser = task.result!!.user!!
                    val registeredemail = firebaseuser.email!!
//                    Toast.makeText(
//                        this,
//                        "$name You have successfully registered the email address $email",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    FirebaseAuth.getInstance().signOut()
//                    finish()
                    val user = user(firebaseuser.uid,name,registeredemail)

                    FirestoreClass().registeruser(this,user)

                } else {
                    Toast.makeText(this,"Registration failed", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun validateform(name:String , email:String , password : String, confirmpass:String):Boolean{
        return when{
            (password!=confirmpass)->{
                showErrorSnackBar("mismatch between given and confirmpassword!!")
                false
            }
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a Email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a Password")
                false
            }
            TextUtils.isEmpty(confirmpass)->{
                showErrorSnackBar("Please confirm your password")
                false
            }
            else -> {
                true
            }
        }
    }

    fun signupbuttonclicked(view: View) {
        registeruser()
    }

    fun userRegisteredSuccess() {
        Toast.makeText(this, "You have Successfully registered",Toast.LENGTH_SHORT).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

}