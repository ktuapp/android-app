package com.retinaapps.ktuapp

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.OutputStreamWriter

class LoginActivity : AppCompatActivity() {

    val JSON_FILE = "data.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val constraint1 = ConstraintSet()
        constraint1.clone(login_constraint_layout)
        val constraint2 = ConstraintSet()
        constraint2.clone(this, R.layout.content_login_anim)
        TransitionManager.beginDelayedTransition(login_constraint_layout)

        val app = applicationContext as KtuApplication
        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if(sharedPref.getBoolean(getString(R.string.is_logged_key), false))
        {
            startActivity(Intent(this, Dashboard::class.java))
        }
        else{
            constraint2.applyTo(login_constraint_layout)
        }
        loginSubmit.setOnClickListener {
            val userid = login_userid.editText!!.text.toString()
            val password = login_password.editText!!.text.toString()

            if(userid.isEmpty())
            {
                login_userid.error = "User id is required"
            }
            else if(password.isEmpty())
            {
                login_userid.error = null
                login_password.error = "Password is required"
            }
            else {
                constraint1.applyTo(login_constraint_layout)
                val call: Call = app.getDatasfromServer(userid, password)

                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        this@LoginActivity.runOnUiThread(java.lang.Runnable {
                            constraint2.applyTo(login_constraint_layout)

                        })
                    }
                    override fun onResponse(call: Call, response: Response){
                        if(response.isSuccessful)
                        {
                            val data = response.body()!!.string()
                            this@LoginActivity.runOnUiThread(java.lang.Runnable {
                                app.writeToFile(data)
                                openDashboard()
                            })
                        }
                        else{
                            this@LoginActivity.runOnUiThread(java.lang.Runnable {
                                constraint2.applyTo(login_constraint_layout)
                                showDialog()
                            })
                        }
                    }
                })
            }
        }
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Invalid user name or password")
        builder.setMessage("You have only 2 attempts to login. If fails try logging into app.ktu.ac.in")
        builder.setPositiveButton("ok"){dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun openDashboard(){

        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putBoolean(getString(R.string.is_logged_key), true)
            putString(getString(R.string.logged_userid),login_userid.editText!!.text.toString())
            apply()
        }

        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
