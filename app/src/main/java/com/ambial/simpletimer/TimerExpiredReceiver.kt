package com.ambial.simpletimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ambial.simpletimer.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        PrefUtil.setTimerState(context = context, state = TimerActivity.TimerState.Stopped)
        PrefUtil.setAlarmTime(context = context, time = 0)
    }
}