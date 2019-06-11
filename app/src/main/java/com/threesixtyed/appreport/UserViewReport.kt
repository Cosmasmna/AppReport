package com.threesixtyed.appreport

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.google.firebase.database.*
import com.threesixtyed.appreport.adapter.UserViewReportAdapter
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.user_view_report_activity.*
import org.jetbrains.anko.toast

class UserViewReport : AppCompatActivity() {

    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    lateinit var sharedPreferences: SharedPreferences
    var reportList = ArrayList<AppReport>()
    lateinit var name:String
    var appNameList= ArrayList<String>()
    private val p = Paint()
    var reportDeatilActivity:ReportDeatilActivity ?= null
    var versionList= ArrayList<String>()


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
                    val reportId=reportList.get(position).report_id
                    databaseRef!!.child(appNameList.get(position)).child(reportId).removeValue()
                    appNameList.removeAt(position)
                    reportList.removeAt(position)
                    userviewAdapter!!.notifyItemRemoved(position)
                    userviewAdapter!!.notifyItemRangeChanged(position, reportList.size)
                    toast("Left")

                } else {


                   versionList=ReportDeatilActivity().getVersionList(appName)
                    Log.i("VersionListSize",versionList.size.toString())

                    editAlert(position)
                    toast("Right")



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


    private fun editAlert(position: Int) {
        /*val dialog:AlertDialog.Builder=AlertDialog.Builder(this)
        val view =layoutInflater.inflate(R.layout.activity_report_detail,null)
        dialog.setView(view)
        val alert:AlertDialog=dialog.create()
        alert.show()*/
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Phone Model")
            .setMessage("hello")
            .setPositiveButton("Ok") { dialog, which ->
                Log.i("VersionListSize",versionList.size.toString())
                userviewAdapter!!.notifyDataSetChanged()

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
