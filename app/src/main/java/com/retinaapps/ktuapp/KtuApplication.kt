package com.retinaapps.ktuapp

import android.app.Application
import android.content.Context
import android.util.Log
import okhttp3.*
import java.io.*

class KtuApplication: Application(){
    val JSON_FILE = "data.json"
    val NOTIFICATION_FILE = "notifications.json"
    val JSON = MediaType.parse("application/json; charset=utf-8")

    fun getDatasfromServer(userid: String, password: String): Call {
        val url = resources.getString(R.string.api_url) + "/data"
        val apikey = resources.getString(R.string.api_key)
        val data = "{\n" +
                "\t\"userid\": \"$userid\",\n" +
                "\t\"password\": \"$password\",\n" +
                "\t\"key\": \"$apikey\"\n" +
                "}"
        val client = OkHttpClient()
        val requestBody: RequestBody? =  RequestBody.create(JSON,data)
        val request: Request = Request.Builder().url(url).post(requestBody).build()

        val call: Call = client.newCall(request)
//        call.enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("Error-ktu-app",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response){
//
//                this@KtuApplication.run {
//                    if(!fileExist(JSON_FILE))
//                        writeToFile(response.body()!!.string())
//                }
//            }
//        })

        return call
    }

    fun fileExist(fname: String): Boolean {
        val file = baseContext.getFileStreamPath(fname)
        return file.exists()
    }


    fun writeToFile(data: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(openFileOutput(JSON_FILE, Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

    }

    fun readFromFile(): String {

        var ret = ""

        try {
            val inputStream = baseContext.openFileInput(JSON_FILE)

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString = bufferedReader.readLine()
                val stringBuilder = StringBuilder()

                while (receiveString != null) {
                    stringBuilder.append(receiveString)
                    receiveString = bufferedReader.readLine()
                }

                inputStream!!.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: " + e.toString())
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: " + e.toString())
        }

        return ret
    }

    fun readFromNotificationFile(): String {

        var ret = ""

        try {
            val inputStream = baseContext.openFileInput(NOTIFICATION_FILE)

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString = bufferedReader.readLine()
                val stringBuilder = StringBuilder()

                while (receiveString != null) {
                    stringBuilder.append(receiveString)
                    receiveString = bufferedReader.readLine()
                }

                inputStream!!.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: " + e.toString())
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: " + e.toString())
        }

        return ret
    }

    fun getNotifications(): Call {
        val url = resources.getString(R.string.api_url) + "/notifications"
        val client = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()

        val call: Call = client.newCall(request)

        return call
    }

    fun writeNotificationToFile(data: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(openFileOutput(NOTIFICATION_FILE, Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

    }
}