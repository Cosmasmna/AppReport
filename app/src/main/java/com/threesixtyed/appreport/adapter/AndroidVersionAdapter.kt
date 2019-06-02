package com.threesixtyed.appreport.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.Version
import kotlinx.android.synthetic.main.android_version_item.view.*

class AndroidVersionAdapter(val context:Context,val app_version_list:ArrayList<Version>) :RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.android_version_item, p0, false))

    }

    override fun getItemCount(): Int {
        return app_version_list.size

     }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0?.app_version_name?.text= app_version_list.get(p1).v_name

    }

}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val app_version_name=view.txt_phone_version_name

}
