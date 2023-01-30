package com.example.projemanaj.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projemanaj.activities.MainActivity
import com.example.projemanaj.activities.MyProfileActivity
import com.example.projemanaj.activities.SigninActivity
import com.example.projemanaj.activities.SignupActivity
import com.example.projemanaj.models.user
import com.example.projemanaj.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFirestore = Firebase.firestore

    fun registeruser(activity: SignupActivity, userinfo: user) {

        mFirestore.collection(Constants.USERS)
            .document(getcurrentuserid())
            .set(userinfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e)

            }

    }

    fun updateuserprofiledata(activity : MyProfileActivity , userhashMAP: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS)
            .document(getcurrentuserid()).update(userhashMAP).addOnSuccessListener {
                Log.d("Profiledataupdata","profiledata updated successfully")
                Toast.makeText(activity,"Profiledata updated Succesfully",Toast.LENGTH_SHORT).show()
                activity.profileupdatesuccessful()
            }.addOnFailureListener{
                e->
                activity.hideProgressDialog()
                Log.e("ErrorUpdate","Error while creating a board")
                Toast.makeText(activity,"Error while updating Profile",Toast.LENGTH_SHORT).show()
            }
    }

    fun loaduserdata(activity: Activity){

        mFirestore.collection(Constants.USERS)
            .document(getcurrentuserid())
            .get()
            .addOnSuccessListener {
                val loggedinuser=it.toObject<user>()

                when(activity){
                    is SigninActivity->{
                        activity.signInSuccess(loggedinuser)
                    }
                    is MainActivity->{
                        activity.updateNzvigationUserDeyails(loggedinuser)
                    }
                    is MyProfileActivity ->{
                            activity.setUserDataInUI(loggedinuser!!)
                    }
                }

            }
            .addOnFailureListener{

            }

    }

    fun getcurrentuserid(): String {
        var currentuser=FirebaseAuth.getInstance().currentUser
        var currentuserid=""
        if (currentuser!=null){
            currentuserid=currentuser.uid
        }
        return currentuserid
    }

}