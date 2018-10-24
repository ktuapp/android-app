package com.retinaapps.ktuapp.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retinaapps.ktuapp.Course
import com.retinaapps.ktuapp.KtuApplication
import com.retinaapps.ktuapp.R
import com.retinaapps.ktuapp.Semester

import org.json.JSONObject
import org.json.JSONArray


class SemesterFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_semester_list, container, false)

        val app = activity!!.applicationContext as KtuApplication
        val data = app.readFromFile()
        val jsonObj = JSONObject(data)
        val semesters: ArrayList<Semester> = ArrayList()
        for (i in 1..8) {
            val semester = jsonObj.getJSONArray("S$i")
            val courses: ArrayList<Course> = ArrayList()
            for (j in 0..(semester.length() - 1)) {
                val c = semester.getJSONObject(j)
                courses.add(Course(c.getString("course"),c.getString("credit"),c.getString("type"),c.getString("completed"),c.getString("grade"),c.getString("earned")))
            }
            semesters.add(Semester(courses,jsonObj.getString("S"+i+"sgpa"), i))
        }
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = SemesterRecyclerViewAdapter(semesters, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Semester?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                SemesterFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
