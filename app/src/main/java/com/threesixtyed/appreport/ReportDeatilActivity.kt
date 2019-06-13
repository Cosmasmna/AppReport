package com.threesixtyed.appreport

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
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
    var app_version_list = ArrayList<String>()
    private var databaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("app")
    val vL = arrayOf("4.4", "5.0", "5.1", "6.0", "7.0", "7.1", "8.0", "8.1", "9")


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            android.R.id.home -> {
                finish()
                true
            }
            R.id.item_save -> {

                //--alert dialog for confirmation

                if (isChoice()) {

                    confirmSendDialog()

                    //finish()
                }
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmSendDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)

        var alertDialog = alertDialogBuilder.create()

        val dialogView = LayoutInflater.from(this).inflate(R.layout.comfirm_custom_dialog, null)
        var btn = dialogView.findViewById<Button>(R.id.btnConfirm)
        var txtAppVersion = dialogView.findViewById<TextView>(R.id.txtAppVersion)
        var txtPhoneModel = dialogView.findViewById<TextView>(R.id.txtPhoneModel)
        var txtAndroidVersion = dialogView.findViewById<TextView>(R.id.txtAndroidVersion)
        var txtReportType = dialogView.findViewById<TextView>(R.id.txtReportType)
        var txtReportDetail = dialogView.findViewById<TextView>(R.id.txtReportDetail)
        var btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        txtAppVersion.text = tv_app_version.text.toString()
        txtAndroidVersion.text = tv_android_version.text.toString()
        txtPhoneModel.text = tv_phone_model.text.toString()
        txtReportDetail.text = et_reportdetail.text.toString()
        txtReportType.text = selectedRadio.text.toString()
        btnCancel.setOnClickListener {
            alertDialog.cancel()

        }
        btn.setOnClickListener {
            saveReport()
            alertDialog.dismiss()
        }
        alertDialog.setTitle("Chemistry")
        alertDialog.setView(dialogView)
        alertDialog.show()

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
            name,
            "unresolve"


        )

        databaseReference!!.child(app_name.toString()).child(key)
            .setValue(appReport, object : DatabaseReference.CompletionListener {
                override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                    if (p0 != null) {
                        Toast.makeText(this@ReportDeatilActivity, "Send Failed", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this@ReportDeatilActivity, "Success", Toast.LENGTH_SHORT).show()

                        finish()
                    }
                }
            })


    }


    lateinit var selectedRadio: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        var selected = radioGroup.checkedRadioButtonId
        selectedRadio = findViewById(selected)
        //databaseReference = FirebaseDatabase.getInstance().getReference("app")
        sharePreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE)


        setSupportActionBar(activity_report_toolbar)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        // var intent=Intent()
        app_name = intent.getSerializableExtra("app_name")

        //setSupportActionBar("title")
        // var intent=Intent()
        app_name = intent.getSerializableExtra("app_name")

        activity_report_toolbar.setTitle(app_name.toString())

        app_version_list = getVersionList(app_name.toString())




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

    fun getVersionList(name: String): ArrayList<String> {
        val vList = ArrayList<String>()

        databaseReference!!.child(name).child("version")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (h in dataSnapshot.children) {
                            databaseReference!!.child(name).child("version").child(h.key.toString())
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()) {
                                            val version = p0.getValue(AppVersion::class.java)
                                            Log.i("version##", version!!.v_name)
                                            vList.add(version!!.v_name)
                                        }
                                    }
                                })
                        }
                    }
                }

            })

        return vList
    }

    private fun showDialog(vL: Array<String>, tv_android_version: TextView?, s: String) {

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)
        alertDialogBuilder.setTitle(s)
        alertDialogBuilder.setItems(vL) { dialog, which ->
            tv_android_version!!.text = vL[which]
        }
        alertDialogBuilder.show()
    }

    private fun select_phone_model() {
        val et_ph_model = EditText(this)
        et_ph_model.hint = "Enter phone model"
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Phone Model")
            .setView(et_ph_model)
            .setPositiveButton("Ok") { dialog, which ->
                tv_phone_model.text = et_ph_model.text.toString()
            }.create()
        dialog.show()

    }
}
