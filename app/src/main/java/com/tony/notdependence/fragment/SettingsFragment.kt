package com.tony.notdependence.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.tony.notdependence.R
import com.tony.notdependence.activity.LoginActivity
import com.tony.notdependence.activity.MainActivity
import com.tony.notdependence.firebase.FirebaseHelper
import com.tony.notdependence.fragment.dialog.ChangeAimDialog
import com.tony.notdependence.utils.showToast
import kotlinx.android.synthetic.main.setting_fragment.view.*
import java.lang.reflect.Executable
import java.text.SimpleDateFormat
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mFirebaseReference: DatabaseReference

    private lateinit var mFirebase: FirebaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFirebase = FirebaseHelper(context as Activity)

        mAuth = mFirebase.auth

        mFirebaseReference = mFirebase.database

        mAuth = FirebaseAuth.getInstance()
        val intent = Intent(context, LoginActivity::class.java)
        return inflater.inflate(R.layout.setting_fragment, container, false).apply {
            settings_toolbar.setNavigationIcon(R.drawable.ic_back)
            settings_toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
            sign_out.setOnClickListener {
                mAuth.signOut()
                startActivity(intent)
                activity?.finish()
            }

            aim_change_text.setOnClickListener {
                activity?.supportFragmentManager?.let { aim ->
                    ChangeAimDialog().show(
                        aim,
                        "dialog_fragment"
                    )
                }
            }

            restart_text.setOnClickListener {
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                val clickedStart = dateFormat.parse(dateFormat.format(currentDate))?.time

                mFirebaseReference.child("aims").child(mAuth.currentUser!!.uid).child("date")
                    .setValue(
                        clickedStart
                    ).addOnSuccessListener {
                        updateFirebase()
                    }
                context.showToast("Restart")
            }
        }
    }

    private fun updateFirebase() {
        val ma = activity as? MainActivity
        ma?.updateItems()
    }

}