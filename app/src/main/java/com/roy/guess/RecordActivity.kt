package com.roy.guess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val count: Int = intent.getIntExtra("COUNTER", -1)
        record_counter.setText(count.toString())
        record_save.setOnClickListener { view ->
            val nick = record_nickname.text.toString()
            getSharedPreferences("guess", MODE_PRIVATE)
                .edit()
                .putInt("REC_COUNTER", count)
                .putString("REC_NICKNAME", nick)
                .apply()
            val intent = Intent()
            intent.putExtra("NICK", nick)
            setResult(RESULT_OK, intent)
            finish()

        }
    }
}