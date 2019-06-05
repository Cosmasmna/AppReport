package com.threesixtyed.appreport.model

data class AppReport(var report_id:String="",var android_version:String="",
                     var app_version:String="",var phone_model:String="",
                     var report_date:String="",var report_detail:String="",
                     var report_type:String="",var user_name:String="") {
}