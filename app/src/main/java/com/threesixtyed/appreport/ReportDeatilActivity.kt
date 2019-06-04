package com.threesixtyed.appreport

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.threesixtyed.appreport.model.AppReport
import com.threesixtyed.appreport.model.AppVersion
import kotlinx.android.synthetic.main.activity_report_detail.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportDeatilActivity : AppCompatActivity() {


    lateinit var sharePreferences: SharedPreferences

    lateinit var app_name: Serializable
    val app_version_list = ArrayList<String>()
    private var databaseReference: DatabaseReference? = null
    val vL = arrayOf("4.4", "4.4W", "5.0", "5.1", "6.0", "7.0", "7.1", "8.0", "8.1", "9")




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_save -> {
               // if (isChoice()) {

                    //--alert dialog for confirmation
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)
                    val dialogView = LayoutInflater.from(this).inflate(R.layout.comfirm_custom_dialog, null)
                    alertDialogBuilder.setTitle("Chemistry")
                    alertDialogBuilder.setView(dialogView)
//                    alertDialogBuilder.setItems(vL) { dialog, which ->
//                        Toast.makeText(this, vL[which], Toast.LENGTH_LONG).show()
//                        tv_android_version!!.text = vL[which]
//                    }
                    alertDialogBuilder.show()


//                    saveReport()
//                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
//                    finish()
              //  }
                if (isChoice()) {


                    confirmSendDialog()
                    saveReport()

                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    finish()
                }
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmSendDialog() {

    }

    private fun isChoice(): Boolean {
        var istrue: Boolean = false
        if (tv_android_version.text.toString().equals("")) {
            tv_android_version.setHintTextColor(Color.RED)
        }
        if (tv_app_version.text.toString().equals("")) {
            tv_app_version.setHintTextColor(Color.RED)
        }
        if (tv_phone_model.text.toString().equals("")) {
            tv_phone_model.setHintTextColor(Color.RED)
        }

        if (et_reportdetail.text.toString().equals("")) {
            et_reportdetail.setHintTextColor(Color.RED)
        }
        if (tv_android_version.text.toString().equals("") || tv_app_version.text.toString().equals("")
            || tv_phone_model.text.toString().equals("") || et_reportdetail.text.toString().equals("")
        ) {
            istrue = false
        } else
            istrue = true
        return istrue
    }

    private fun saveReport() {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        var name = sharePreferences.getString("name", "")
        var selected = radioGroup.checkedRadioButtonId
        var selectedRadio: RadioButton = findViewById(selected)
        databaseReference = FirebaseDatabase.getInstance().getReference("report")
        var key = databaseReference!!.push().key.toString()
        var appReport = AppReport(
            key,
            tv_android_version.text.toString(),
            tv_app_version.text.toString()
            ,
            tv_phone_model.text.toString(),
            currentDate,
            et_reportdetail.text.toString(),
            selectedRadio.text.toString(),
            name
        )

        databaseReference!!.child(app_name.toString()).child(key).setValue(appReport)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        databaseReference = FirebaseDatabase.getInstance().getReference("app")
        sharePreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE)


        setSupportActionBar(activity_report_toolbar)
        // var intent=Intent()
        app_name = intent.getSerializableExtra("app_name")
        activity_report_toolbar.setTitle(app_name.toString())

        databaseReference!!.child(app_name.toString()).child("version")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (h in dataSnapshot.children) {
                            databaseReference!!.child(app_name.toString()).child("version").child(h.key.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()) {
                                            val version = p0.getValue(AppVersion::class.java)
                                            Log.i("version##", version!!.v_name)
                                            app_version_list.add(version!!.v_name)
                                        }
                                    }
                                })
                        }
                    }
                }

            })
        android_version_layout.setOnClickListener {
            showDialog(vL, tv_android_version, "Android Version")
        }
        app_version_layout.setOnClickListener() {
            val someStrings = Array<String>(app_version_list.size) { "it = $it" }
            app_version_list.toArray(someStrings)
            showDialog(someStrings, tv_app_version, "App Version")
        }
        phone_model_layout.setOnClickListener {
            select_phone_model()
        }
    }

    private fun showDialog(vL: Array<String>, tv_android_version: TextView?, s: String) {

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)
        alertDialogBuilder.setTitle(s)
        alertDialogBuilder.setItems(vL) { dialog, which ->
            Toast.makeText(this, vL[which], Toast.LENGTH_LONG).show()
            tv_android_version!!.text = vL[which]
        }
        alertDialogBuilder.show()
    }

    private fun select_phone_model() {
        val et_ph_model = EditText(this)
        et_ph_model.hint="Enter phone model"
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Phone Model")
            .setView(et_ph_model)
            .setPositiveButton("Ok") { dialog, which ->
                tv_phone_model.text = et_ph_model.text.toString()
            }.create()
        dialog.show()

    }
}
