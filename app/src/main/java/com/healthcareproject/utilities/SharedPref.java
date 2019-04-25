package com.healthcareproject.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.healthcareproject.R;

public class SharedPref {

   @SuppressLint("StaticFieldLeak")
   static private SharedPref instance;  //Instance of this class
   private final SharedPreferences sharedPref;

   private SharedPref(Context context) {
       sharedPref = context.getSharedPreferences(context.getString(R.string.sharedPrefFileName), 0);

   }

   //Method to create the instance of this class only once
   public static void contextHandler(Context context) {
       if(instance==null) {
           instance=new SharedPref(context);
       }
   }

    //Method to return the the instance of this class to other class
   public static SharedPref getInstance() {
       return instance;
   }

     //Method to set integer in shared preferences
     public void setString(String key, String value) {
       SharedPreferences.Editor editor = sharedPref.edit();
       editor.putString(key, value).apply();
   }

      //Method to retrieve integer from shared preferences
      @SuppressWarnings("SameParameterValue")
      public String getString(String key) {
       return sharedPref.getString(key,"");
   }

   //Method to delete shared preferences
   public void delete()
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
