package com.example.aaron.kingdomalliance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements View.OnClickListener {

    Button bBack, bRegister;
    Intent i;
    EditText etUsername, etPassword, etConfirmPassword;
    TextView tvError;
    Variables.User user;
    String username, password, confirmPassword;
    ServerRequests serverRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        serverRequests = new ServerRequests(this);
        bBack = (Button) findViewById(R.id.bBack);
        bRegister = (Button) findViewById(R.id.bRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        tvError = (TextView) findViewById(R.id.tvError);
        bBack.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bBack:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.bRegister:
                username = etUsername.getText().toString();
                user = new Variables(). new User(username);
                CheckUsername(user);
                break;
        }
    }

    private void CheckUsername(Variables.User user){
        serverRequests.CheckUsernameInBackground(user, new GetUserCallBack() {
            @Override
            public void done(Variables.User returnedUser) {
                if (returnedUser == null){
                    username = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    confirmPassword = etConfirmPassword.getText().toString();
                    if (password.equals(confirmPassword) && !password.equals("")){
                        Variables.User user = new Variables(). new User(username, password);
                        Register(user);
                        i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                    else{
                        tvError.setText("Passwords don't match");
                        tvError.setTextColor(Color.RED);
                    }
                }
                else{
                    tvError.setText("Username unavailable");
                    tvError.setTextColor(Color.RED);
                }
            }
        });
    }

    private void Register(Variables.User user) {
        serverRequests.storeUserDataInBackground(user);
    }

    @Override
    public void onBackPressed(){}
}
