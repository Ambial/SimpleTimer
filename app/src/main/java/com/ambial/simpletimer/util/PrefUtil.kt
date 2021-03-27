package com.ambial.simpletimer.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.ambial.simpletimer.TimerActivity


class PrefUtil {
    companion object {

        private const val TIMER_LENGTH_ID = "com.ambial.simpletimer.timer_length"

        fun getTimerLength(context:Context):Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 10)
        }

        private val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.ambial.simpletimer.previous_timer_length"

        fun getPreviousTimerLenght(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0L)
        }

        fun setPreviousTimerLength(context: Context, seconds:Long){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private val TIMER_STATE_ID = "com.ambial.simpletimer.timer_state"

        fun getTimerState(context: Context):TimerActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            //enum values are represented as Integers, 0 being the first
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(context: Context, state:TimerActivity.TimerState){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private val SECONDS_REMAINING_ID = "com.ambial.simpletimer.seconds_remaining"

        fun getSecondsRemaining(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0L)
        }

        fun setSecondsRemaining(context: Context, seconds:Long){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private val ALARM_SET_TIME_ID = "com.ambial.simpletimer.background_time"

        fun getAlarmTime(context: Context):Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0L)
        }

        fun setAlarmTime(context: Context, time:Long){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}