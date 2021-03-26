package com.ambial.simpletimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ambial.simpletimer.util.NotificationUtil
import com.ambial.simpletimer.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        when(intent.action){
            AppConstants.ACTION_STOP -> {
                TimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(context = context, state = TimerActivity.TimerState.Stopped)
                NotificationUtil.hideNotification(context)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L

                val wakeUpTime = TimerActivity.setAlarm(context = context,
                    TimerActivity.nowSeconds,
                    secondsRemaining = secondsRemaining)
                PrefUtil.setTimerState(context = context, state = TimerActivity.TimerState.Running)
                PrefUtil.setSecondsRemaining(context = context, seconds = secondsRemaining)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmTime(context)
                val nowSeconds = TimerActivity.nowSeconds
                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(context = context, seconds = secondsRemaining)
                TimerActivity.removeAlarm(context = context)
                PrefUtil.setTimerState(context = context, state = TimerActivity.TimerState.Paused)
                NotificationUtil.showTimerPaused(context = context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = TimerActivity.setAlarm(context = context,
                                                        TimerActivity.nowSeconds,
                                                        secondsRemaining = secondsRemaining)
                PrefUtil.setTimerState(context = context, state = TimerActivity.TimerState.Running)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}