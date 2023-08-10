package kr.sjh.myschedule

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import kr.sjh.myschedule.utill.Common.CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE") ?: return
        val content = intent.getStringExtra("CONTENT") ?: return



        println("Alarm trigger : $title")
        context?.let {
            showNotification(it, title, content)
        }
    }

    private fun showNotification(context: Context, title: String, content: String) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(content)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}