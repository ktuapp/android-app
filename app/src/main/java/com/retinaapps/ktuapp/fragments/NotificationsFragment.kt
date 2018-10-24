package com.retinaapps.ktuapp.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retinaapps.ktuapp.KtuApplication
import com.retinaapps.ktuapp.Notification
import com.retinaapps.ktuapp.R
import com.retinaapps.ktuapp.Semester

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [NotificationsFragment.OnListFragmentInteractionListener] interface.
 */
class NotificationsFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null
    var app: KtuApplication? = null

    var notificationsRv : RecyclerView? = null
    var notificationsRecyclerViewAdapter : NotificationsRecyclerViewAdapter? = null
    val notifications: ArrayList<Notification> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notifications_list, container, false)


        notificationsRv = view as RecyclerView
        notificationsRv!!.layoutManager = LinearLayoutManager(context)


        app = activity!!.applicationContext as KtuApplication
        notificationsRecyclerViewAdapter = NotificationsRecyclerViewAdapter(notifications, listener)
        notificationsRv!!.adapter = notificationsRecyclerViewAdapter

        if(!app!!.fileExist(app!!.NOTIFICATION_FILE)) {
            val getNotifications = GetNotifications()
            getNotifications.execute()
        }
        else
        {
            populateDatas()
        }

        return view
    }

    fun populateDatas()
    {
        val data = app!!.readFromNotificationFile()
        val jsonObj = JSONObject(data)
        val notificationObj = jsonObj.getJSONArray("notifications")
        val notifications: ArrayList<Notification> = ArrayList()

        for (j in 0..(notificationObj.length() - 1)) {
            val n = notificationObj.getJSONObject(j)
            notifications.add(Notification(n.getString("date"),n.getString("data")))
        }


        notificationsRecyclerViewAdapter!!.updateList(notifications)
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Notification?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                NotificationsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }

    inner class GetNotifications: AsyncTask<String,String,String>(){
        override fun doInBackground(vararg p0: String?): String {
            try {
                val response:Response =  app!!.getNotifications().execute()
                app!!.writeNotificationToFile(response.body()!!.string())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            populateDatas()
        }
    }
}
