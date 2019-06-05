package com.threesixtyed.appreport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.report_view_activity.*

class ReportViewActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_view_activity)
        setSupportActionBar(toolbar)
        toolbar.title="DTP(Chemistry)"
        toolbar.subtitle="v2.2.4"
    }
}