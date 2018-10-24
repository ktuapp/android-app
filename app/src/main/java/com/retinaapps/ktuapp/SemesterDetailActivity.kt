package com.retinaapps.ktuapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_semester_detail.*
import kotlinx.android.synthetic.main.content_semester_detail.*

class SemesterDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semester_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val semester: Semester = intent.getSerializableExtra("SEMESTER_DATA") as Semester

        semester.course.removeAt(0)
        semester_courses_rv.layoutManager = LinearLayoutManager(this)
        semester_courses_rv.adapter = SemesterDetailRecyclerAdapter(semester.course)

        supportActionBar?.title = "Semester "+semester.sem


    }

}
