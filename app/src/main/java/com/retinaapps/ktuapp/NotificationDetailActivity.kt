package com.retinaapps.ktuapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest

import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.content_notification.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.ads.AdView



class NotificationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val notification: Notification= intent.getSerializableExtra("NOTIFICATION_DATA") as Notification

        val formatter = SimpleDateFormat("EEE MMM dd", Locale.ENGLISH)
        val d = formatter.parse( notification.date)

        val formatterDate = SimpleDateFormat("dd MMM", Locale.ENGLISH)

        notification_detail_date.text = formatterDate.format(d)
        notification_detail_data.text = notification.data

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

}
