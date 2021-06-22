package com.tony.notdependence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tony.notdependence.R
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment: Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onStartApp(text: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_start.setOnClickListener{
            mListener.onStartApp(edit_text_aim.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as StartFragment.Listener
    }
}