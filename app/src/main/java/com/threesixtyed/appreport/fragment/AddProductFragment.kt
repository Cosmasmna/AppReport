package com.threesixtyed.appreport.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*

import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppInfo
import com.threesixtyed.appreport.model.AppVersion
import com.threesixtyed.appreport.model.User
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.fragment_adduser.*

 // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */


class AddProductFragment : Fragment() {

    var firebase:FirebaseDatabase?=null
    var databaseRef:DatabaseReference?=null
    var count:Long?=0
    var appnameList:List<String>?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.fragment_add_product, container, false)

        databaseRef=FirebaseDatabase.getInstance().getReference().child("app")


        val btn_upload=view.findViewById<Button>(R.id.btn_application_upload)
        btn_upload.setOnClickListener{
            Toast.makeText(context,count.toString(),Toast.LENGTH_LONG).show()

            var app_name: String = et_application_name.text.toString()
            var app_version: String = et_application_version.text.toString()
            var app_link: String = et_application_link.text.toString()

            if(app_name.isNotEmpty()&&app_version.isNotEmpty()&&app_link.isNotEmpty()){
                //val key:String= databaseRef!!.push().key.toString()
                databaseRef!!.child(app_name).setValue(app_name)
                databaseRef!!.child(app_name).child("app_name").setValue(app_name)

                databaseRef!!.child(app_name).child("latest_version").setValue(AppVersion(app_version,app_link,""))
                databaseRef!!.child(app_name).child("version").child("1").setValue(AppVersion(app_version,app_link,""))

                databaseRef!!.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show()

                    }

                })
            }else{
                    et_application_name.setError("fill app Name")
                    et_application_link.error="fill app link"
                    et_application_version.setError("fill app version")
            }


        }
        return view

    }


}
