package com.example.projemanaj.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projemanaj.R
import com.example.projemanaj.firebase.FirestoreClass
import com.example.projemanaj.models.user
import com.example.projemanaj.utils.Constants
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class MyProfileActivity : BaseActivity() {


    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserdetails:user
    private var mprofileimageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirestoreClass().loaduserdata(this@MyProfileActivity)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            mSelectedImageFileUri = data.data

            try {

                Glide
                    .with(this@MyProfileActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString()))
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    val permissions= arrayOf("android.permission.READ_EXTERNAL_STORAGE","android.permission.READ_MEDIA_IMAGES")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==10){
            if (grantResults[1]==PackageManager.PERMISSION_GRANTED|| grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }else{

            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ios_back)
            actionBar.title = resources.getString(R.string.nav_my_profile)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }


    fun setUserDataInUI(user: user) {
        mUserdetails= user
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        et_name.setText(user.name)
        et_email.setText(user.email)
        if (user.mobile != 0L) {
            et_mobile.setText(user.mobile.toString())
        }
    }


    private fun showImageChooser() {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    companion object {

        private const val PICK_IMAGE_REQUEST_CODE = 2

    }

    fun Updateclicked(view: View) {
        uploaduserImages()
        showProgressDialog("Please Wait")
        updateUserProfiledata()
    }

    fun roundprofileclicked(view: View) {
        requestPermissions(permissions,10)
    }

    private fun uploaduserImages(){
        showProgressDialog("Please Wait")

        if (mSelectedImageFileUri != null){
            val storage = Firebase.storage
            var storageRef = storage.reference
            var imagesRef: StorageReference? = storageRef.child("userimage"+ System.currentTimeMillis()+"."+fileextension(mSelectedImageFileUri))
            imagesRef?.putFile(mSelectedImageFileUri!!)?.addOnSuccessListener {
                Log.e("Firebase image url", it.metadata?.reference?.downloadUrl.toString())

                it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                    Log.i("Downloadable image uri", it.toString())
                    mprofileimageURL=it.toString()
                   // hideProgressDialog()
                    //update user profile data
                    updateUserProfiledata()
                }
            }?.addOnFailureListener{
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }
    }

    fun profileupdatesuccessful(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun fileextension(uri: Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun updateUserProfiledata(){
        val userHashMap = HashMap<String, Any>()
        if (mprofileimageURL.isNotEmpty() && mUserdetails.image!=mprofileimageURL){
            userHashMap[Constants.Image]=mprofileimageURL
        }
        if (et_name.text.toString() != mUserdetails.name){
            userHashMap[Constants.UserName] = et_name.text.toString()
        }
        if (et_mobile.text.toString()!="" && et_mobile.text.toString() != mUserdetails.mobile.toString()){
            userHashMap[Constants.Mobile]= et_mobile.text?.toString()!!.toLong()
        }
        FirestoreClass().updateuserprofiledata(this,userHashMap)
    }

    fun Emailclicked(view: View) {
        showdialog("Email Cannot be updated","Email id Should be Unique and Cannot be Updated")
    }

}