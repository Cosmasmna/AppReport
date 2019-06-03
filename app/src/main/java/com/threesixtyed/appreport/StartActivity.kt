package com.threesixtyed.appreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        sharedPreferences=getSharedPreferences("mypref",Context.MODE_PRIVATE)

        val timer=object : CountDownTimer(2000,1000) {
            override fun onFinish() {

                if (sharedPreferences.contains("name")){
                    if (sharedPreferences.getString("name","").equals("admin")){
                        startActivity(Intent(this@StartActivity,AdminMainActivity::class.java))
                        finish()

                    }else{
                        startActivity(Intent(this@StartActivity,MainActivity::class.java))
                        finish()
                    }

                }else {
                    startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                    finish()
                }

            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }
        timer.start()

    }
}
