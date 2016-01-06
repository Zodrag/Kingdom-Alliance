package com.example.aaron.kingdomalliance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener {

    Button bLogin;
    TextView tvRegisterHere, tvError;
    EditText etUsername, etPassword;
    ServerRequests serverRequests;
    UserLocalStore userLocalStore;
    Variables.User user;
    String username, password;
    Intent i;
    boolean loggedIn;

    @Override
    protected void onStart() {
        super.onStart();
        loggedIn = userLocalStore.checkUserLoggedIn();
        if (loggedIn == true ){
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        serverRequests = new ServerRequests(this);
        userLocalStore = new UserLocalStore(this);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);
        tvError = (TextView) findViewById(R.id.tvError);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin.setOnClickListener(this);
        tvRegisterHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRegisterHere:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.bLogin:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                user = new Variables().new User(username, password);
                Login(user);
                break;
        }
    }

    private void Login(final Variables.User user) {
        serverRequests.GetUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(Variables.User returnedUser) {
                if (returnedUser != null){
                    userLocalStore.storeUserData(returnedUser);
                    loggedIn = true;
                    userLocalStore.setUserLoggedIn(loggedIn);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    tvError.setText("Incorrect User Details");
                    tvError.setTextColor(Color.RED);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){}
}
