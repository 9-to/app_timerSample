package com.example.mycountdowntimer

import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.mycountdowntimer.databinding.ActivityMainBinding
import android.media.AudioAttributes

import android.os.Build




class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var soundPool: SoundPool
    private var soundResId = 0
    inner class MyCountDownTimer(millisInFuture:Long,countDownInterval:Long):
                CountDownTimer(millisInFuture,countDownInterval){
        var isRunning = false

        override fun onTick(p0: Long) {
            val minute = p0/1000L/60L
            val second = p0/1000L%60L
            binding.timerText.text = "%1d:%2$02d".format(minute,second)
        }

        override fun onFinish() {
            binding.timerText.text = "0:00"
            soundPool.play(soundResId,1.0f,100f,0,0,1.0f)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timerText.text = "3:00"
        val timer = MyCountDownTimer(3 * 60 * 1000,100)
        binding.playStop.setOnClickListener {
            timer.isRunning = when (timer.isRunning){
                true->{
                    timer.cancel()
                    binding.playStop.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    false
                }
                false->{
                    timer.start()
                    binding.playStop.setImageResource(R.drawable.ic_baseline_stop_24)
                    true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        soundPool = buildSoundPool()
        soundResId = soundPool.load(this, R.raw.untitled, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
    private fun buildSoundPool(): SoundPool{
        var pool: SoundPool
        pool = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SoundPool(2, AudioManager.STREAM_ALARM, 0)
        } else {
            val attr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build()
        }
        return pool
    }
}