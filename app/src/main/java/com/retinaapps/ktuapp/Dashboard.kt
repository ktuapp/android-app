package com.retinaapps.ktuapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.retinaapps.ktuapp.fragments.NotificationsFragment
import com.retinaapps.ktuapp.fragments.ProfileFragment
import com.retinaapps.ktuapp.fragments.ReportFragment
import com.retinaapps.ktuapp.fragments.SemesterFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.io.Serializable
import com.google.firebase.analytics.FirebaseAnalytics




class Dashboard : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener, ReportFragment.OnFragmentInteractionListener, SemesterFragment.OnListFragmentInteractionListener, NotificationsFragment.OnListFragmentInteractionListener{

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onListFragmentInteraction(item: Notification?) {
        val intent = Intent(this,NotificationDetailActivity::class.java)
        intent.putExtra("NOTIFICATION_DATA", item as Serializable)
        startActivity(intent)
    }

    override fun onListFragmentInteraction(item: Semester?) {
        if(item!!.course.size != 0) {
            val intent = Intent(this, SemesterDetailActivity::class.java)
            intent.putExtra("SEMESTER_DATA", item as Serializable)
            startActivity(intent)
        }

    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance("","")
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reports -> {
                val reportFragment = ReportFragment.newInstance("","")
                openFragment(reportFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_semesters -> {
                val semesterFragment = SemesterFragment.newInstance(1)
                openFragment(semesterFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val notificationsFragment = NotificationsFragment.newInstance(1)
                openFragment(notificationsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val profileFragment = ProfileFragment.newInstance("","")
        openFragment(profileFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_refresh -> {
//                val app = applicationContext as KtuApplication
//                app.getDatasfromServer("ADR16CS016","h@l0w33n")
                true
            }
            R.id.action_logout->{
                val sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putBoolean(getString(R.string.is_logged_key), false)
                    apply()
                }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}
