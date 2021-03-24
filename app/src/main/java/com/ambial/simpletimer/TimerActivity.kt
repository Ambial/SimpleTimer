package com.ambial.simpletimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ambial.simpletimer.databinding.ActivityMainBinding
import com.ambial.simpletimer.util.PrefUtil
import com.google.android.material.snackbar.Snackbar
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

class TimerActivity : AppCompatActivity() {

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var timer : CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsLeft = 0L

    private lateinit var tv:TextView
    private lateinit var progressBar: MaterialProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "    Timer"

        //fragmentBinding = TimerContentBinding.inflate(layoutInflater)
        tv = findViewById(R.id.textview_countdown)
        progressBar = findViewById(R.id.progress_circle)

        binding.fabPlay.setOnClickListener { view ->
            Snackbar.make(view, "Start!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        binding.fabPause.setOnClickListener { view ->
            Snackbar.make(view, "Pause!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        binding.fabStop.setOnClickListener { view ->
            Snackbar.make(view, "Stop!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        //TODO: Remove background timer, hide notification
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()

            //TODO: Start background timer & show notification
        } else if (timerState == TimerState.Paused){
            //TODO: Show notification
        }

        PrefUtil.setPreviousTimerLength(context = this,seconds = timerLengthSeconds)
        PrefUtil.setSecondsRemaining(context = this, seconds = secondsLeft)
        PrefUtil.setTimerState(context = this, state = timerState)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(context = this)

        if (timerState == TimerState.Stopped){
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        secondsLeft = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(context = this)
        else
            timerLengthSeconds

        //TODO: Change secondsLeft according to where the background timer stopped

        //resume where we left off
        if (timerState == TimerState.Running){
            startTimer()
        }

        updateButtons()
        updateCountDownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        setNewTimerLength()

        progressBar.progress = 0

        PrefUtil.setSecondsRemaining(context = this, timerLengthSeconds)
        secondsLeft = timerLengthSeconds

        updateButtons()
        updateCountDownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object :CountDownTimer(secondsLeft*1000, 1000){
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsLeft = millisUntilFinished/1000
                updateCountDownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(context = this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progressBar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLenght(context = this)

        progressBar.max = timerLengthSeconds.toInt()
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running -> {
                binding.fabPlay.isEnabled = false
                binding.fabPause.isEnabled = true
                binding.fabStop.isEnabled = true
            }
            TimerState.Paused -> {
                binding.fabPlay.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.fabPlay.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = false
            }
        }
    }

    private fun updateCountDownUI(){
        val minutesUntilFinished = secondsLeft / 60
        val secondsInMinuteUntilFinished = secondsLeft - (minutesUntilFinished*60)

        val secondsStr = secondsInMinuteUntilFinished.toString()

        tv.text = "$minutesUntilFinished:${
            if(secondsStr.length==2)
            secondsStr 
            else "0" + secondsStr}"

        progressBar.progress = (timerLengthSeconds - secondsLeft).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}