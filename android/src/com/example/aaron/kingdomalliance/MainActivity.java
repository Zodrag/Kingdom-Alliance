package com.example.aaron.kingdomalliance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button bLogout, bBattle;
    UserLocalStore userLocalStore;
    Intent i;
    Variables.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bLogout = (Button) findViewById(R.id.bLogout);
        bBattle = (Button) findViewById(R.id.bBattle);
        bBattle.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        TestFetchUserFromSP();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.bBattle:
                i = new Intent(this, AndroidLauncher.class);
                startActivity(i);
                break;
        }
    }

    public void TestFetchUserFromSP (){
        user = userLocalStore.getUserData();
        bLogout.setText("Logout from Username: '" + user.username + "' with Password: '" + user.password + "'");
    }
}
