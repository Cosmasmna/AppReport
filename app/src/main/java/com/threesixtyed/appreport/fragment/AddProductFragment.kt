package com.threesixtyed.appreport.fragment


import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppVersion
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddProductFragment : Fragment() {

    var isHasImage:Boolean = false
    var firebase:FirebaseDatabase?=null
    var databaseRef:DatabaseReference?=null
    var storage:FirebaseStorage?=null
    var storageReference:StorageReference?=null
    var appnameList:List<String>?=null
    private val RC_SELECT_IMGAE = 103
    private val RC_PERMISSIONS = 101
    private val PERMISSIONS_REQUIRED = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,

        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.fragment_add_product, container, false)

        databaseRef=FirebaseDatabase.getInstance().getReference().child("app")
        storage=FirebaseStorage.getInstance()
        storageReference= storage!!.getReference("appimage")



        var appImage=view.findViewById<ImageView>(R.id.application_image)
        appImage.setOnClickListener {

            askRequiredPermissions()
            val selectImageIntent= Intent(Intent.ACTION_GET_CONTENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(selectImageIntent,RC_SELECT_IMGAE)


        }


        val btn_upload=view.findViewById<Button>(R.id.btn_application_upload)
        btn_upload.setOnClickListener{

            var app_name: String = et_application_name.text.toString()
            var app_version: String = et_application_version.text.toString()
            var app_link: String = et_application_link.text.toString()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            val imageName=app_name+".jpg"


            if(app_name.isEmpty()) {
                et_application_name.setError("fill app Name")

            }else if(app_version.isEmpty()){
                et_application_version.setError("fill app version")

            }else if (app_link.isEmpty()){
                et_application_link.error="fill app link"

            }else {
                Toast.makeText(context,"Choose Application Logo",Toast.LENGTH_SHORT).show()
            }


            if (app_name.isNotEmpty() && app_version.isNotEmpty()&& app_link.isNotEmpty() && isHasImage){
                var storageReference1=storageReference!!.child(imageName)

                appImage.isDrawingCacheEnabled = true
                appImage.buildDrawingCache()
                val bitmap = (appImage.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = storageReference1.putBytes(data)
                uploadTask.addOnFailureListener {
                    Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show()


                }.addOnSuccessListener {
                    storageReference1.downloadUrl.addOnSuccessListener(OnSuccessListener {
                        databaseRef!!.child(app_name).child("img_url").setValue(it.toString())

                        Toast.makeText(context,"Uploaded",Toast.LENGTH_SHORT).show()


                    })
                }
                databaseRef!!.child(app_name).setValue(app_name)
                databaseRef!!.child(app_name).child("app_name").setValue(app_name)

                databaseRef!!.child(app_name).child("latest_version").setValue(AppVersion(app_version,app_link,currentDate))
                databaseRef!!.child(app_name).child("version").child("1").setValue(AppVersion(app_version,app_link,""),object :DatabaseReference.CompletionListener{
                    override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                        if (p0!=null){
                            Toast.makeText(context,"Send Failed",Toast.LENGTH_SHORT).show()


                        }
                        else{

                            et_application_name?.setText("")
                            et_application_link?.setText("")
                            et_application_version?.setText("")
                            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

                        }
                    }
                })
            }

        }
        return view

    }

    private fun askRequiredPermissions() {
        if (!arePermissionGranted()) {

            ActivityCompat.requestPermissions(this.requireActivity(), PERMISSIONS_REQUIRED, RC_PERMISSIONS)

        }
    }

    private fun arePermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(this.requireContext(),

                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&

            ContextCompat.checkSelfPermission(this.requireContext(),

                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){

            return false

        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when (requestCode) {

            RC_SELECT_IMGAE -> {

                if (data != null) {

                    val uri = data.data

                    displaySelectedImage(getBitmapFromUri(uri))

                }

            }

            else -> super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun displaySelectedImage(bitmapFromUri: Bitmap) {
        isHasImage=true

        application_image.setImageBitmap(bitmapFromUri)

    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {

        val parcelFileDescriptor = context!!.contentResolver.openFileDescriptor(uri, "r")

        val fileDescriptor = parcelFileDescriptor?.fileDescriptor

        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)

        parcelFileDescriptor.close()

        return image

    }
}
