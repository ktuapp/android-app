package com.retinaapps.ktuapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.retinaapps.ktuapp.Course
import com.retinaapps.ktuapp.KtuApplication

import com.retinaapps.ktuapp.R
import com.retinaapps.ktuapp.Semester
import kotlinx.android.synthetic.main.fragment_report.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_report, container, false)

        val adRequest = AdRequest.Builder().build()
        view.reportAdView.loadAd(adRequest)

        val app = activity!!.applicationContext as KtuApplication
        val data = app.readFromFile()
        val jsonObj = JSONObject(data)
        val cgpa = jsonObj.getString("CGPA:")+"/10"

        var credits_earned_first = 0f
        var total_credits_earned_first = 0f
        for (i in 1..2) {
            val semester = jsonObj.getJSONArray("S$i")
            for (j in 0..(semester.length() - 1)) {
                val c = semester.getJSONObject(j)
                if(c.getString("course") != "Course" && c.getString("earned") != "Result Not Published") {
                    if (c.getString("earned") != "")
                        credits_earned_first += c.getString("earned").toFloat()
                    total_credits_earned_first += c.getString("credit").toFloat()
                }
            }
        }

        var credits_earned_second = 0f
        var total_credits_earned_second = 0f
        for (i in 1..4) {
            val semester = jsonObj.getJSONArray("S$i")
            for (j in 0..(semester.length() - 1)) {
                val c = semester.getJSONObject(j)
                if(c.getString("course") != "Course" && c.getString("earned") != "Result Not Published") {
                    if (c.getString("earned") != "")
                        credits_earned_second += c.getString("earned").toFloat()
                    total_credits_earned_second += c.getString("credit").toFloat()
                }
            }
        }

        var credits_earned = 0f
        var total_credits_earned = 0f
        for (i in 1..8) {
            val semester = jsonObj.getJSONArray("S$i")
            for (j in 0..(semester.length() - 1)) {
                val c = semester.getJSONObject(j)
                if(c.getString("course") != "Course" && c.getString("earned") != "Result Not Published") {
                    if (c.getString("earned") != "")
                        credits_earned += c.getString("earned").toFloat()
                    total_credits_earned += c.getString("credit").toFloat()
                }
            }
        }



        var s5Perc = 0f
        var s7Perc = 0f

        if(credits_earned_first - 26 > 0)
            s5Perc = 100f
        else
            s5Perc = (credits_earned_first)*100/26

        if(credits_earned_second - 52 > 0)
            s7Perc = 100f
        else
            s7Perc = (credits_earned_second)*100/52

        view.report_cgpa.text = cgpa
        val credits = credits_earned.toInt().toString() + "/" + total_credits_earned.toInt().toString()
        view.report_credits.text = credits
        val credistsS5 = credits_earned_first.toInt().toString() + "/26"
        val credistsS7 = credits_earned_second.toInt().toString() + "/52"
        view.creditsS5.text = credistsS5
        view.creditsS7.text = credistsS7

        if(s5Perc < 100)
            view.progressS5.color = resources.getColor(R.color.colorRed)
        if(s7Perc < 100)
            view.progressS7.color = resources.getColor(R.color.colorRed)
        view.progressS5.setProgressWithAnimation(s5Perc, 800)
        view.progressS7.setProgressWithAnimation(s7Perc, 800)

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ReportFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
