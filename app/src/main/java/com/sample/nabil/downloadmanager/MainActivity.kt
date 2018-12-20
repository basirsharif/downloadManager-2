package com.sample.nabil.downloadmanager

import android.Manifest
import android.annotation.TargetApi
import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val list = ArrayList<Long>()

    val onComplete = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            list.remove(referenceId)
            Toast.makeText(this@MainActivity, "Send", Toast.LENGTH_SHORT).show()
            if (list.isEmpty()) {
                sendNotification("GadgetSaint", "All Download completed")
            }

        }
    }


    private val notificationID = 101
    private val channelID = "com.sample.nabil.downloadmanager"

    @TargetApi(Build.VERSION_CODES.O)
    private fun sendNotification(name: String, description: String) {

//        val resultIntent = Intent(this@MainActivity, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(this@MainActivity, channelID)
            .setContentTitle(name)
            .setContentText(description)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .setNumber(10)
//            .setContentIntent(pendingIntent)
            .build()

        Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show()
        notificationManager?.notify(notificationID, notification)

    }

    private var notificationManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String, description: String) {

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        notificationManager?.createNotificationChannel(channel)
    }


    var DOWNLOAD_URI: Uri? = null

    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "Download Manager", "Download Manager Demo")

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        Toast.makeText(this@MainActivity, "Send", Toast.LENGTH_SHORT).show()

        DOWNLOAD_URI =
                Uri.parse("https://site-images.similarcdn.com/url?url=https%3A%2F%2Flh3.googleusercontent.com%2FAx2wQYxjDITuZEpc6K9EDYPG7C839tb4PApia4Tmf18u8XehB-twqhVgDVPgxxExkr4%3Ds180&h=17236808949838518689")


        if (!isStoragePermissionGranted()) {
        }
    }

    private var refid: Long = 0

    fun singleDownload(view: View) {
        list.clear()

        val request = DownloadManager.Request(DOWNLOAD_URI)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("GadgetSaint Downloading " + "Sample" + ".png")
        request.setDescription("Downloading " + "Sample" + ".png")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "/GadgetSaint/" + "/" + "Sample" + ".png"
        )

        refid = downloadManager.enqueue(request)
        list.add(refid)

    }


    fun multipleDownload(view: View) {


        list.clear()

        for (i in 0..1) {
            val request = DownloadManager.Request(DOWNLOAD_URI)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("GadgetSaint Downloading Sample_$i.png")
            request.setDescription("Downloading Sample_$i.png")
            request.setVisibleInDownloadsUi(true)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint//Sample_$i.png")

            refid = downloadManager.enqueue(request)


            Log.e("OUTNM", "" + refid)

            list.add(refid)

        }

    }


    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission granted
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
    }
}
