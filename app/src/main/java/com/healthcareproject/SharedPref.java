package com.healthcareproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

class SharedPref {

   @SuppressLint("StaticFieldLeak")
   static private SharedPref instance;  //Instance of this class
   private final SharedPreferences sharedPref;

   private SharedPref(Context context) {
       sharedPref = context.getSharedPreferences(context.getString(R.string.sharedPrefFileName), 0);

   }

   //Method to create the instance of this class only once
   static void contextHandler(Context context) {
       if(instance==null) {
           instance=new SharedPref(context);
       }
   }

    //Method to return the the instance of this class to other class
   static SharedPref getInstance() {
       return instance;
   }

     //Method to set integer in shared preferences
    void setString(String key,String value) {
       SharedPreferences.Editor editor = sharedPref.edit();
       editor.putString(key, value).apply();
   }

      //Method to retrieve integer from shared preferences
      @SuppressWarnings("SameParameterValue")
      String getString(String key) {
       return sharedPref.getString(key,"");
   }

   //Method to delete shared preferences
   void delete()
   {
       sharedPref.edit().clear().apply();
   }

    public void setBoolean(String key,boolean value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key,value).apply();
    }

    public boolean getBoolean(String key){
        return sharedPref.getBoolean(key,false);
    }
}
