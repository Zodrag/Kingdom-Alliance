package com.example.aaron.kingdomalliance;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    Variables.User user;
    Boolean loggedIn;
    SharedPreferences userLocalDataBase;

    public UserLocalStore(Context context){
        userLocalDataBase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void storeUserData(Variables.User user){
        SharedPreferences.Editor userLocalDataBaseEditor = userLocalDataBase.edit();
        userLocalDataBaseEditor.putString("username", user.username);
        userLocalDataBaseEditor.putString("password", user.password);
        userLocalDataBaseEditor.apply();
    }

    public Variables.User getUserData(){
        String username = userLocalDataBase.getString("username", "");
        String password = userLocalDataBase.getString("password", "");
        user = new Variables(). new User(username, password);
        return user;
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDataBase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.apply();
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDataBase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.apply();
    }

    public boolean checkUserLoggedIn(){
        loggedIn = userLocalDataBase.getBoolean("loggedIn", false);
        return loggedIn;
    }
}
