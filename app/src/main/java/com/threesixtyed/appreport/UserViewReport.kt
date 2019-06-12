package com.threesixtyed.appreport

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import com.threesixtyed.appreport.adapter.UserViewReportAdapter
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.user_view_report_activity.*
import org.jetbrains.anko.find
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.toast

class UserViewReport : AppCompatActivity() {
    val vL = arrayOf("4.4", "5.0", "5.1", "6.0", "7.0", "7.1", "8.0", "8.1", "9")

    lateinit  var alert:AlertDialog

    lateinit var d_et_reportdetail:EditText
    lateinit var d_app_version:TextView
    lateinit var d_android_version:TextView
    lateinit var d_phone_model:TextView
    lateinit var dRadioGroup:RadioGroup
    lateinit var d_radio_bugreport:RadioButton
    lateinit var d_radio_requestfeatures:RadioButton
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    lateinit var sharedPreferences: SharedPreferences
    var reportList = ArrayList<AppReport>()
    lateinit var name:String
    var appNameList= ArrayList<String>()
    private val p = Paint()
    var reportDeatilActivity:ReportDeatilActivity ?= null
    var versionList= ArrayList<String>()
    lateinit var selectedRadio:RadioButton



    var userviewAdapter:UserViewReportAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_view_report_activity)
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE)

        database = FirebaseDatabase.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("report")
        name = sharedPreferences!!.getString("name", "")
        bindAdapter()
        bindData()
        initSwipe()
    }

    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewholder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewholder.adapterPosition
               val appName= appNameList.get(position)

                if (direction == ItemTouchHelper.LEFT) {
                    //Log.i("Status",reportList.get(position).status)
                    if(reportList.get(position).status.equals("progress")){
                        Warning()
                    }
                    else {
                        confirmDelete(position)
                    }

                } else {

                    versionList=ReportDeatilActivity().getVersionList(appName)
                    Log.i("VersionListSize",versionList.size.toString())
                    editAlert(position)
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {


                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        p.color = Color.parseColor("#388E3C")
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_edit_white)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)

                       // val icon_dest=RectF(200.0F,200.0F,200.0F, 200.0F)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p.color = Color.parseColor("#D32F2F")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete_white)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    }



                }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(user_view_report_recycle)
    }

    private fun Warning() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage("This report is solving now!!!")
            .setPositiveButton("Ok") { dialog, which ->
                userviewAdapter!!.notifyDataSetChanged()
                dialog.dismiss()

            }
        val ad=dialog.create()
        ad.show()
    }

    private fun confirmDelete(position: Int) {

           val dialog = AlertDialog.Builder(this)
               .setTitle("Delete")
               .setMessage("Are you sure want to delete")
               .setNeutralButton("Cancel"){dialog, which ->
                   userviewAdapter!!.notifyDataSetChanged()

               }
               .setPositiveButton("Ok") { dialog, which ->
                   val reportId=reportList.get(position).report_id
                   databaseRef!!.child(appNameList.get(position)).child(reportId).removeValue()
                   appNameList.removeAt(position)
                   reportList.removeAt(position)
                   userviewAdapter!!.notifyItemRemoved(position)
                   userviewAdapter!!.notifyItemRangeChanged(position, reportList.size)
                   userviewAdapter!!.notifyDataSetChanged()
                   dialog.dismiss()

               }.setCancelable(false)
        val ad=dialog.create()
        ad.show()

    }


    private fun editAlert(position: Int) {
        val dialog=AlertDialog.Builder(this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        val view=layoutInflater.inflate(R.layout.edit_user_report,null)
        val edit_app_version_layout=view.find<LinearLayout>(R.id.edit_app_version_layout)
        val edit_android_version_layout=view.find<LinearLayout>(R.id.edit_android_version_layout)
        val edit_phone_model_layout=view.find<LinearLayout>(R.id.edit_phone_model_layout)

        d_et_reportdetail=view.find<EditText>(R.id.dialog_et_reportdetail)
        d_app_version=view.find<TextView>(R.id.dialog_app_version)
        d_android_version=view.find<TextView>(R.id.dialog_android_version)
        d_phone_model=view.find<TextView>(R.id.dialog_phone_model)
        dRadioGroup=view.find<RadioGroup>(R.id.dialogRadioGroup)

        val btnSaveEdit=view.findViewById<Button>(R.id.btnSaveEdit)
        d_radio_bugreport=view.find<RadioButton>(R.id.dialog_radio_bugreport)
        d_radio_requestfeatures=view.find<RadioButton>(R.id.dialog_radio_requestfeatures)

        d_radio_bugreport.isChecked=reportList.get(position).report_type.equals("Bug Report")
        d_radio_requestfeatures.isChecked=reportList.get(position).report_type.equals("Request Features")

        d_app_version.text=reportList.get(position).app_version
        d_android_version.text=reportList.get(position).android_version
        d_phone_model.text=reportList.get(position).phone_model
        d_et_reportdetail.append(reportList.get(position).report_detail)
        Log.i("Status",reportList.get(position).status)
        if(reportList.get(position).status.equals("complete")) {
            btnSaveEdit.isEnabled = false
            btnSaveEdit.visibility=View.GONE
            view.find<TextView>(R.id.txt_alert_message).visibility=View.VISIBLE

        }
        
        btnSaveEdit.setOnClickListener {
            if (isCheck()){
            successEdit(position)
            }
        }

        edit_app_version_layout.setOnClickListener {
            val someStrings = Array<String>(versionList.size) { "it = $it" }
            versionList.toArray(someStrings)
            showDialog(someStrings, d_app_version, "App Version")        }
        edit_android_version_layout.setOnClickListener {

            showDialog(vL, d_android_version, "Android Version")

        }
        edit_phone_model_layout.setOnClickListener {
            select_phone_model(d_phone_model)
        }

        val toolbar = view.findViewById<Toolbar>(R.id.activity_report_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_close)
        toolbar.title = appNameList.get(position)

        dialog.setView(view)
        alert=dialog.create()

        toolbar.setNavigationOnClickListener {  alert.dismiss()
            userviewAdapter!!.notifyDataSetChanged()
        }

        alert.window!!.attributes.windowAnimations = R.style.DialogThemeAnim
        alert.show()
        userviewAdapter!!.notifyDataSetChanged()


    }

    private fun successEdit(position: Int){
        var seletctedText:String
        if (d_radio_bugreport.isChecked){
            seletctedText= "Bug Report"
        }
        else{
            seletctedText= "Request Features"
        }
        var appReport = AppReport(
            reportList.get(position).report_id,
            d_android_version.text.toString(),
            d_app_version.text.toString()
            ,
            d_phone_model.text.toString(),
            reportList.get(position).report_date,
            d_et_reportdetail.text.toString(),
            seletctedText.toString(),
            name,reportList.get(position).status


        )

        databaseRef!!.child(appNameList.get(position)).child(reportList.get(position).report_id)
            .setValue(appReport, object : DatabaseReference.CompletionListener {
                override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                    if (p0 != null) {
                        Toast.makeText(this@UserViewReport, "Send Failed", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this@UserViewReport, "Success", Toast.LENGTH_SHORT).show()
                        alert.dismiss()


                        userviewAdapter!!.notifyDataSetChanged()
                    }
                }
            })

    }

    private fun isCheck(): Boolean {
        var istrue: Boolean = false

        if (d_phone_model.text.toString().trim().equals("")) {
            d_phone_model.setHintTextColor(Color.RED)
        }

        if (d_et_reportdetail.text.toString().trim().equals("")) {
            d_et_reportdetail.error="Enter detail"
            d_et_reportdetail.text.clear()
            d_et_reportdetail.setHintTextColor(Color.RED)
        }
        if (d_phone_model.text.toString().trim().equals("") || d_et_reportdetail.text.toString().trim().equals("")
        ) {
            istrue = false
        } else
            istrue = true

return istrue

    }

    fun showDialog(vL: Array<String>, tv_android_version: TextView?, s: String) {

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)
        alertDialogBuilder.setTitle(s)
        alertDialogBuilder.setItems(vL) { dialog, which ->
            tv_android_version!!.text = vL[which]
        }
        alertDialogBuilder.show()
    }
    fun select_phone_model(tv_phone_model: TextView) {
        val et_ph_model = EditText(this)
        et_ph_model.singleLine=true
        et_ph_model.setPadding(35,35,35,35)
        if (tv_phone_model.text.trim().equals("")) {
            et_ph_model.text.clear()
            et_ph_model.hint = "Enter phone model"

        }else{
            et_ph_model.append(tv_phone_model.text)
        }
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Phone Model")
            .setView(et_ph_model)
            .setPositiveButton("Ok") { dialog, which ->
                if (et_ph_model.text.toString().trim().equals("")){
                    et_ph_model.text.clear()
                }
                else {
                    tv_phone_model.text = et_ph_model.text.toString()
                }
            }.create()
        dialog.show()

    }
    private fun bindData() {
        databaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                reportList.clear()
                for (h in p0.children) {

                    databaseRef!!.child(h.key.toString()).addValueEventListener(object : ValueEventListener {

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.exists()) {
                                Log.i("keyyyyyyyyyy", h.key.toString())


                                for (data in p0.children) {


                                    val appreport = data.getValue(AppReport::class.java)
                                    val user_name: String = appreport!!.user_name

                                    if (name.equals(user_name)) {
                                        appNameList.add(h.key.toString())
                                        reportList!!.add(appreport!!)
                                    }


                                }

                                userviewAdapter!!.notifyDataSetChanged()

                            }
                        }

                    })
                }

            }

        })
    }
    private fun bindAdapter() {
        user_view_report_recycle.layoutManager =
            LinearLayoutManager(applicationContext)
        userviewAdapter = UserViewReportAdapter(applicationContext, reportList!!,appNameList)
        user_view_report_recycle.adapter = userviewAdapter

    }
}
