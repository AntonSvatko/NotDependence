package com.tony.notdependence.fragment.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.tony.notdependence.R
import com.tony.notdependence.activity.MainActivity
import com.tony.notdependence.data.Aim
import com.tony.notdependence.firebase.FirebaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*

open class ChangeAimDialog : DialogFragment() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mFirebaseReference: DatabaseReference

    private lateinit var mFirebase: FirebaseHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mFirebase = FirebaseHelper(context as Activity)
        mFirebaseReference = mFirebase.database
        mAuth = mFirebase.auth

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_fragment, null)
        return AlertDialog.Builder(context!!)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                sendAim(view.aim_change_input.text.toString())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                //do nothing
            }
            .setTitle("All progress will be deleted are you sure?")
            .create()
    }

    fun sendAim(message: String) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val mainActivity = activity as? MainActivity

        val clickedStart = dateFormat.parse(dateFormat.format(currentDate))?.time
        val aim = Aim(clickedStart, message)
        mFirebaseReference.child("aims").child(mAuth.currentUser!!.uid).setValue(aim)
            .addOnSuccessListener {
                mainActivity?.updateItems()
            }
        mainActivity?.supportActionBar?.title = message
        mainActivity?.collapsing_toolbar?.title = message
    }
}