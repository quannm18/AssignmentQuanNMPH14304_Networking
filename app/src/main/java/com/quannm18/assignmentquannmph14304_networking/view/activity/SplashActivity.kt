package com.quannm18.assignmentquannmph14304_networking.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.quannm18.assignmentquannmph14304_networking.R

class SplashActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.statusBarColor = R.color.color_green
        window.navigationBarColor = R.color.color_green
        val cdt = object : CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                finish()
            }

        }.start()
    }
}