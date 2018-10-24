package com.retinaapps.ktuapp.fragments

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.retinaapps.ktuapp.Notification
import com.retinaapps.ktuapp.R


import com.retinaapps.ktuapp.fragments.NotificationsFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import android.text.method.TextKeyListener.clear
import kotlin.collections.ArrayList


class NotificationsRecyclerViewAdapter(
        private val mValues: ArrayList<Notification>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Notification
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_notifications, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        val formatter = SimpleDateFormat("EEE MMM dd", Locale.ENGLISH)
        val d = formatter.parse(item.date)

        val formatterDate = SimpleDateFormat("dd", Locale.ENGLISH)
        val formatterMonth = SimpleDateFormat("MMM", Locale.ENGLISH)

        holder.mDateView.text =  formatterDate.format(d)
        holder.mMonthView.text =  formatterMonth.format(d)
        holder.mContentView.text = item.data

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun updateList(items: List<Notification>?) {
        if (items != null && items.size > 0) {
            mValues.clear()
            mValues.addAll(items)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mDateView: TextView = mView.not_date
        val mMonthView: TextView = mView.not_month
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
