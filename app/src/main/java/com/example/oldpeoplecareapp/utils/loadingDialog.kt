package com.example.oldpeoplecareapp

import android.app.Activity
import android.app.AlertDialog
import com.example.oldpeoplecareapp.R

class LoadingDialog(val mActivity:Activity) {
    val bulider = AlertDialog.Builder(mActivity)
    private  var isdialog:AlertDialog=bulider.create()


    fun startLoading(){
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item,null)
        /**set Dialog*/
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}






//package com.example.oldpeoplecareapp
//
//import android.app.Activity
//import android.app.AlertDialog
//import com.example.oldpeoplecareapp.R
//
//class LoadingDialog(val mActivity:Activity) {
//    private lateinit var isdialog:AlertDialog
//    fun startLoading(){
//        /**set View*/
//        val infalter = mActivity.layoutInflater
//        val dialogView = infalter.inflate(R.layout.loading_item,null)
//        /**set Dialog*/
//        val bulider = AlertDialog.Builder(mActivity)
//        bulider.setView(dialogView)
//        bulider.setCancelable(false)
//        isdialog = bulider.create()
//        isdialog.show()
//    }
//    fun isDismiss(){
//        isdialog.dismiss()
//    }
//}