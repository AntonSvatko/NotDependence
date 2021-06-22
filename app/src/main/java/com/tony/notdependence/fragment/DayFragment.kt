package com.tony.notdependence.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.tony.notdependence.R
import com.tony.notdependence.firebase.FirebaseHelper
import com.tony.notdependence.interfaces.TextWriter
import kotlinx.android.synthetic.main.fragment_day.view.*

class DayFragment(private val mContext: Context) : Fragment() {

    private var topicText: String? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirebaseReference: DatabaseReference
    private lateinit var mFirebase: FirebaseHelper

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicText = it.getString("text").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFirebase = FirebaseHelper(mContext as Activity)

        mAuth = mFirebase.auth
        mFirebaseReference = mFirebase.database

        return inflater.inflate(R.layout.fragment_day, container, false).apply {
            editText = input_text
            editText.setText(topicText)
        }
    }

    override fun onDestroyView() {
        topicText = editText.text.toString()
        super.onDestroyView()
        (activity as TextWriter).update(topicText!!)
    }
}