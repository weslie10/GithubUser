package com.example.githubuser.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySettingBinding
import com.example.githubuser.other.alarm.AlarmReceiver
import com.example.githubuser.other.alarm.MyPreference
import com.google.android.material.snackbar.Snackbar

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var preference: MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = resources.getString(R.string.setting)
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        alarmReceiver = AlarmReceiver()
        preference = MyPreference(this)
        binding.setAlarm.isChecked = preference.getAlarm()

        binding.languangeSetting.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        binding.setAlarm.setOnClickListener {
            val alarm = binding.setAlarm.isChecked
            preference.setAlarm(alarm)

            if(alarm) {
                alarmReceiver.setRepeatingAlarm(
                    this, AlarmReceiver.TYPE_REPEATING,
                    getString(R.string.alarm_message)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }

            val text = resources.getString(
                if (alarmReceiver.isAlarmSet(this)) {
                    R.string.set_alarm
                } else {
                    R.string.remove_alarm
                }
            )
            Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}