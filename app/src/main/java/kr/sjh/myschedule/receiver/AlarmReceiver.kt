package kr.sjh.myschedule.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import kr.sjh.myschedule.R
import kr.sjh.myschedule.ui.theme.SoftBlue
import kr.sjh.myschedule.utill.Common.CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE") ?: return
        val content = intent.getStringExtra("CONTENT") ?: return
        context?.let {
            showNotification(it, title, content)
        }
    }

    private fun showNotification(context: Context, title: String, content: String) {

        val largeIcon =
            BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID).setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(context.resources.getColor(R.color.soft_blue)).setContentTitle(title)
            .setContentText(content).build()

        notificationManager.notify(1, notification)
    }
}