package com.tony.notdependence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tony.notdependence.R
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment: Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onRegistration()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_continue_register.setOnClickListener {
            mListener.onRegistration()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}