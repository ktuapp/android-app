package com.retinaapps.ktuapp

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_semester_detail.view.*

class SemesterDetailRecyclerAdapter (
        private val mValues: List<Course>)
    :RecyclerView.Adapter<SemesterDetailRecyclerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_semester_detail, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mCourseName.text = item.course
        val credits = "Credits                                   " + item.credit
        var creditsEarned = "Credits Earned                      " + item.earned
        if(item.earned == "")
        {
            creditsEarned = "Credits Earned                      " + 0
        }
        holder.mCourseCreditsTotal.text = credits
        holder.mCourseCredits.text = creditsEarned
        val grade = "Grade                                     " + item.grade
        holder.mCourseGrade.text = grade
        val status = "Status                                    " + item.completed
        holder.mCourseStatus.text = status
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mCourseName: TextView = mView.semester_detail_course_name
        val mCourseCredits: TextView = mView.semester_detail_course_credits
        val mCourseCreditsTotal: TextView = mView.semester_detail_course_credits_total
        val mCourseGrade: TextView = mView.semester_detail_course_grade
        val mCourseStatus: TextView = mView.semester_detail_course_status
    }
}
