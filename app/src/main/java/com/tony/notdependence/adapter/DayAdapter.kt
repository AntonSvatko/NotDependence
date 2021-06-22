package com.tony.notdependence.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.tony.notdependence.data.DayItem
import com.tony.notdependence.R
import com.tony.notdependence.firebase.FirebaseHelper
import com.tony.notdependence.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.item_recycler_view.view.*



class DayAdapter(
    private var items: MutableList<DayItem> = mutableListOf(),
    private val listener: OnItemClickListener,
    private val mContext: Context
) :
    RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirebaseReference: DatabaseReference
    private lateinit var mFirebase: FirebaseHelper

    fun updateItem(dayItem: DayItem, position: Int){
        items[position] = dayItem
        notifyItemChanged(position)
    }

    fun updateList(items: List<DayItem>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler_view,
            parent,
            false
        )
        mFirebase = FirebaseHelper(mContext as Activity)

        mAuth = mFirebase.auth
        mFirebaseReference = mFirebase.database

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item, position)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(dayItem: DayItem, position: Int) {
            itemView.setOnClickListener {
                listener.onItemClick(dayItem, position)
            }
                itemView.item_text.text = dayItem.topicText
                itemView.day_count.text = dayItem.dayCount.toString()
        }
    }

}