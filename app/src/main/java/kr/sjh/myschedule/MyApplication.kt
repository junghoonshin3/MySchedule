package kr.sjh.myschedule

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import kr.sjh.myschedule.utill.Common.CHANNEL_ID

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(
            CHANNEL_ID,
            applicationContext,
            NotificationManager.IMPORTANCE_HIGH
        )
    }

    private fun createNotificationChannel(channelId: String, context: Context, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MY_SCHEDULE_CHANNEL"
            val descriptionText = "My Schedule Channel"
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}