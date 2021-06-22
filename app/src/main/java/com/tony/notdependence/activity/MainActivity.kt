package com.tony.notdependence.activity


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tony.notdependence.data.DayItem
import com.tony.notdependence.R
import com.tony.notdependence.adapter.DayAdapter
import com.tony.notdependence.data.Aim
import com.tony.notdependence.firebase.FirebaseHelper
import com.tony.notdependence.fragment.DayFragment
import com.tony.notdependence.fragment.SettingsFragment
import com.tony.notdependence.interfaces.AppTextWatcher
import com.tony.notdependence.interfaces.OnItemClickListener
import com.tony.notdependence.interfaces.TextWriter
import com.tony.notdependence.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_recycler_view.*
import kotlinx.android.synthetic.main.start_fragment.*
import java.util.*


class MainActivity : AppCompatActivity(), AppTextWatcher, TextWriter {

    private lateinit var mAuth: FirebaseAuth

    lateinit var adapter: DayAdapter

    lateinit var recyclerView: RecyclerView

    private lateinit var mFirebaseReference: DatabaseReference

    private lateinit var mFirebase: FirebaseHelper


    var dayItem: DayItem? = null
    var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
    }

    fun updateItems(){
        dayCount {
            adapter.updateList(it)
        }
    }

    private fun setupRecyclerView(){
        mFirebase = FirebaseHelper(this)
        mFirebaseReference = mFirebase.database
        mAuth = mFirebase.auth

        val clicked = object : OnItemClickListener {
            override fun onItemClick(dayItem: DayItem, position: Int) {
                this@MainActivity.dayItem = dayItem
                this@MainActivity.position = position
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .add(
                        android.R.id.content,
                        DayFragment(this@MainActivity).apply {
                            arguments = Bundle().also {
                                it.putString("text", dayItem.topicText)
                            }
                        }).commit()
            }
        }

        dayCount {
            adapter = DayAdapter(it, clicked, this)

            adapter.notifyDataSetChanged()

            recyclerView = recyclerview_day
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }
    }

    private fun dayCount(onSuccess: (MutableList<DayItem>) -> Unit) {
        val uid = mAuth.currentUser!!.uid

        val data = mutableListOf<DayItem>()

        val currentDate = Date()

        val aimReference =
            mFirebase.aimRef(uid)


        aimReference.get().addOnSuccessListener {

            val aim = it.getValue(Aim::class.java)
            val clickDate = aim!!.date
            val milliseconds: Long = currentDate.time - (clickDate as Long)
            val currentDay = (milliseconds / (24 * 60 * 60 * 1000)) + 1

            (1..currentDay).forEach { count ->
                data.add(
                    DayItem(
                        aim.days?.get("day${count}") ?: "new",
                        count.toInt()
                    )
                )
            }
            onSuccess(data)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if ((dayItem != null) && (position != null)) {
            dayItem?.topicText = s.toString()
            adapter.updateItem(dayItem!!, position!!)
        }
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initToolbar() {
        mFirebaseReference.child("aims").child(mAuth.currentUser!!.uid).child("aim").get()
            .addOnSuccessListener { aimText ->
                setSupportActionBar(toolbar)
                with(supportActionBar!!) {
                    setHomeButtonEnabled(false)
                    title = aimText.value.toString()
                    collapsing_toolbar.title = supportActionBar?.title
                    Log.d("test1", "${aimText.value}")
                }
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.basic_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, SettingsFragment()).addToBackStack(null).commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun update(topicText: String) {
        val uid = mFirebase.currentUid.toString()
        mFirebase.aimRef(uid).child("days").child("day${(position!! + 1)}")
            .setValue(topicText)
        adapter.updateItem(DayItem(topicText, position!! + 1), position!!)
    }

}


