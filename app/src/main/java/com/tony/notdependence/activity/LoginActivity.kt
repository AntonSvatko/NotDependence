package com.tony.notdependence.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.tony.notdependence.R
import com.tony.notdependence.fragment.LoginFragment
import com.tony.notdependence.fragment.RegisterFragment
import com.tony.notdependence.fragment.StartFragment
import com.tony.notdependence.data.Aim
import com.tony.notdependence.firebase.FirebaseHelper
import com.tony.notdependence.utils.showToast
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.register_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity(), LoginFragment.Listener, StartFragment.Listener,
    RegisterFragment.Listener {

    private val CONST_INTENT = "KEY"

    private lateinit var mAuth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var mFirebaseReference: DatabaseReference

    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        mFirebase = FirebaseHelper(this)

        mAuth = mFirebase.auth

        mFirebaseReference = mFirebase.database

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, LoginFragment())
                .commit()
        }



        if (mAuth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish()
        }

    }

    override fun onNext() {
        email = edit_text_login.text.toString()
        password = edit_text_password.text.toString()

        Log.d("hui", email + password)
        if (validate(email, password)) {
            Log.d("hui", "30cm")
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("hui", "daniel")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Enter correct password or login")
                }
            }
        } else {
            showToast("Enter correct password or login")
        }
    }

    override fun toRegistration() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onRegistration() {
        val passwordConfirm = edit_text_password_confirm_register.text.toString()
        password = edit_text_password_register.text.toString()
        email = edit_text_login_register.text.toString()

        if (validateAndConfirm(email, password, passwordConfirm)) {
            Log.d("hui", "$password $passwordConfirm")
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("hui", "daniel")
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, StartFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        when (it.exception) {
                            is FirebaseAuthInvalidCredentialsException -> showToast("The email address is badly formatted.")
                            is FirebaseAuthUserCollisionException -> showToast("The email address is already in use by another account.")
                        }
                        Log.d("hui", it.exception.toString())
                    }
                }
        } else {
            showToast("Write correct password, the password must be at least 6 characters long. ")
        }
    }


    override fun onStartApp(text: String) {
        val intent = Intent(this, MainActivity::class.java)


        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")

        val clickedStart = dateFormat.parse(dateFormat.format(currentDate))?.time

        Log.d("Login", "1")

        val aim = Aim(clickedStart, text)
        intent.putExtra(CONST_INTENT, clickedStart)
        Log.d("Login", mFirebaseReference.child("aims").child(mAuth.currentUser!!.uid).toString())
        mFirebaseReference.child("aims").child(mAuth.currentUser!!.uid).setValue(aim)
            .addOnCompleteListener {
                Log.d("Login", "2")
                if (it.isSuccessful){
                    startActivity(intent)
                    finish()
                }else{
                    showToast("Error save of data")
                }
            }



    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty() && (password.length >= 6)

    private fun validateAndConfirm(email: String, password: String, passwordConfirm: String) =
        validate(email, password) && password == passwordConfirm

}