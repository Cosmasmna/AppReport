package com.threesixtyed.appreport

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var btnReport: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* btn.setOnClickListener{
            *//* val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")*//*
            val database=FirebaseDatabase.getInstance()
            val myRef=database.getReference("message")
            myRef.setValue("hello")
        }*/

        btnReport=findViewById(R.id.btnReport)
        btnReport.setOnClickListener() {
            startActivity(Intent(this, ReportDeatilActivity::class.java))
        }


    }
}
