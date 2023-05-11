package com.serwylo.beatgame
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import androidx.core.content.ContextCompat.startActivity
//import com.example.restapiidemo.home.data.User
//
//class SessionManagement constructor(context:Context){
//    var sPreferences:SharedPreferences;
//    var editor:SharedPreferences.Editor;
//    private var sharedPrefName:String = "session";
//    private var sessionKey:String = "session_user";
//    init {
//        sPreferences = context.getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE)
//        editor = sPreferences.edit()
//    }
//
//    fun getSession(): Int {
//        return sPreferences.getInt(sessionKey, -1)
//    }
//
//    fun saveSesssion(user: User, context: Context){
//        var id: Int = user.getId()
//        editor.putInt(sessionKey, id).commit()
//        moveToMainActivity(context)
//    }
//    fun moveToMainActivity(context:Context){
//        val intent = Intent(context, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(context,intent,null)
//    }
//    fun removeSession(context: Context){
//        editor.putInt(sessionKey, -1).commit()
//        moveToMainActivity(context)
//    }
//
//
//}