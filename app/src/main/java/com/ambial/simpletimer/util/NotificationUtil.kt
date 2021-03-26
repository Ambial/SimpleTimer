package com.ambial.simpletimer.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.ambial.simpletimer.AppConstants
import com.ambial.simpletimer.R
import com.ambial.simpletimer.TimerActivity
import com.ambial.simpletimer.TimerNotificationActionReceiver
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "SimpleTimerAppTimer"
        private const val TIMER_ID = 0

        fun showTimerExpired(context: Context){
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    startIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)

            notificationBuilder.setContentTitle("Timer expired!")
                    .setContentText("Start again?")
                    .setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                    .addAction(R.drawable.ic_play, "Start", startPendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelName = CHANNEL_NAME_TIMER, playSound = true, channelId = CHANNEL_ID_TIMER)
            notificationManager.notify(TIMER_ID, notificationBuilder.build())
        }

        fun showTimerRunning(context: Context, wakeUpTime:Long){
            val pausetIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            pausetIntent.action = AppConstants.ACTION_PAUSE
            val pausePendingIntent =
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            pausetIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )

            val stoptIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            stoptIntent.action = AppConstants.ACTION_STOP
            val stopPendingIntent =
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            stoptIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )

            val dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)

            notificationBuilder.setContentTitle("Timer running.")
                    .setContentText("End: ${dateFormat.format(Date(wakeUpTime))}")
                    .setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                    .setOngoing(true)
                    .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                    .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelName = CHANNEL_NAME_TIMER, playSound = true, channelId = CHANNEL_ID_TIMER)
            notificationManager.notify(TIMER_ID, notificationBuilder.build())
        }

        fun showTimerPaused(context: Context){
            val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            resumeIntent.action = AppConstants.ACTION_RESUME
            val resumePendingIntent =
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            resumeIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )

            val notificationBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)

            notificationBuilder.setContentTitle("Timer paused")
                    .setContentText("Resume?")
                    .setContentIntent(getPendingIntentWithStack(context, TimerActivity::class.java))
                    .setOngoing(true)
                    .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelName = CHANNEL_NAME_TIMER, playSound = true, channelId = CHANNEL_ID_TIMER)
            notificationManager.notify(TIMER_ID, notificationBuilder.build())
        }

        private fun getBasicNotificationBuilder(
                context: Context,
                channelId: String,
                playSound: Boolean): NotificationCompat.Builder {
            val notificationSound:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_timer)
                    .setAutoCancel(true)
                    .setDefaults(0)

            if (playSound){
                notificationBuilder.setSound(notificationSound)
            }
            return notificationBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context,javaClass:Class<T>): PendingIntent? {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //extension function
        private fun NotificationManager.createNotificationChannel(
                channelName:String,
                playSound: Boolean,
                channelId: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                                        else NotificationManager.IMPORTANCE_LOW

                val notificationChannel = NotificationChannel(channelId,channelName,channelImportance)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.BLUE

                this.createNotificationChannel(notificationChannel)
            }
        }

        fun hideNotification(context: Context){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(TIMER_ID)
        }
    }
}