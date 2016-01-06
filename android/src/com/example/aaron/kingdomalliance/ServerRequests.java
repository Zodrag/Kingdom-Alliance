package com.example.aaron.kingdomalliance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServerRequests {

    AlertDialog alertDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://kingdomalliance.netai.net/";
    Context context;
    HttpURLConnection httpURLConnection = null;
    String json_url;
    String JSON_STRING;

    public ServerRequests(Context context) {
        this.context = context;
    }

    public void storeUserDataInBackground(Variables.User user) {
        new StoreUserDataAsyncTask(user).execute();
    }

    public void CheckUsernameInBackground (Variables.User user, GetUserCallBack userCallBack){
        new CheckUsernameAsyncTask(user, userCallBack).execute();
    }

    public void GetUserDataInBackground (Variables.User user, GetUserCallBack userCallBack){
        new GetUserDataAsyncTask(user, userCallBack).execute();
    }

    public class CheckUsernameAsyncTask extends AsyncTask<Void, Void, Variables.User> {
        Variables.User user, returnedUser;
        GetUserCallBack userCallBack;

        public CheckUsernameAsyncTask(Variables.User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected void onPreExecute() {
            json_url = SERVER_ADDRESS + "CheckUsername.php";
        }

        @Override
        protected Variables.User doInBackground(Void... params) {

            try {
                URL url = new URL(json_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.username, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(IS));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = reader.readLine()) != null){
                    stringBuilder.append(JSON_STRING);
                }
                reader.close();
                IS.close();
                httpURLConnection.disconnect();
                String newData = stringBuilder.toString();
                JSONObject jObject = new JSONObject(newData);
                if (jObject.length() == 0) {
                    returnedUser = null;
                }
                else{
                    String username = jObject.getString("username");
                    returnedUser = new Variables(). new User(username);
                }
            }catch (IOException | JSONException e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Variables.User user) {
            super.onPostExecute(user);
            userCallBack.done(user);
        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<String, Void, Void> {
        Variables.User user;

        public StoreUserDataAsyncTask(Variables.User user) {
            this.user = user;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Information...");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(SERVER_ADDRESS + "RegisterUser.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user.password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                httpURLConnection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class GetUserDataAsyncTask extends AsyncTask<Void, Void, Variables.User> {
        Variables.User user, returnedUser;
        GetUserCallBack userCallBack;

        public GetUserDataAsyncTask(Variables.User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected void onPreExecute() {
            json_url = SERVER_ADDRESS + "FetchUserData.php";
        }

        @Override
        protected Variables.User doInBackground(Void... params) {

            try {
                URL url = new URL(json_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user.password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(IS));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = reader.readLine()) != null){
                    stringBuilder.append(JSON_STRING);
                }
                reader.close();
                IS.close();
                httpURLConnection.disconnect();
                String newData = stringBuilder.toString();
                JSONObject jObject = new JSONObject(newData);
                if (jObject.length() == 0) {
                    returnedUser = null;
                }
                else{
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");
                    returnedUser = new Variables(). new User(username, password);
                }
            }catch (IOException | JSONException e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Variables.User user) {
            super.onPostExecute(user);
            userCallBack.done(user);
        }
    }
}
