package com.tony.notdependence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tony.notdependence.R
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment: Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onNext()
        fun toRegistration()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_continue.setOnClickListener{
            mListener.onNext()
        }
        registration_text_view.setOnClickListener {
            mListener.toRegistration()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}