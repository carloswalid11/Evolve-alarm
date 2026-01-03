package com.evolve.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: AlarmAdapter
    private val alarms = mutableListOf<Alarm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Evolve Alarm - Kaka"

        rv = findViewById(R.id.recyclerAlarms)
        adapter = AlarmAdapter(alarms) { alarm -> removeAlarm(alarm) }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(Intent(this, AddAlarmActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadAlarms()
    }

    private fun loadAlarms() {
        val prefs = getSharedPreferences("evolve_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("alarms_json", "[]")
        val listType = object : TypeToken<List<Alarm>>() {}.type
        val loaded: List<Alarm> = Gson().fromJson(json, listType)
        alarms.clear()
        alarms.addAll(loaded)
        adapter.notifyDataSetChanged()
    }

    private fun removeAlarm(alarm: Alarm) {
        alarms.remove(alarm)
        saveAlarms()
        cancelScheduledAlarm(alarm)
        adapter.notifyDataSetChanged()
    }

    private fun saveAlarms() {
        val prefs = getSharedPreferences("evolve_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("alarms_json", Gson().toJson(alarms)).apply()
    }

    private fun cancelScheduledAlarm(alarm: Alarm) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("alarm_id", alarm.id)
        val pi = PendingIntent.getBroadcast(this, alarm.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        am.cancel(pi)
    }
}