package com.sawalham.tictac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mohammad on 6/2/2015.
 */

public class SignUpActivity extends Activity {
    final int SERVER_PORT = 10000;
    boolean isNetworkAvailable = false;
    int c1, c2;
    String friend, name, SERVER_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SERVER_IP = getIntent().getExtras().getString("SERVER_IP");
        if (getApplicationContext() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {
                if (tempNetworkInfo.isConnected()) {
                    isNetworkAvailable = true;
                    break;
                }
            }
        }
    }

    public void Search(View v) {
        if (isNetworkAvailable) {
            EditText editText1 = (EditText) findViewById(R.id.friend);
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            name = sp.getString("NAME", "name");
            friend = editText1.getText().toString();
            c1 = name.length();
            c2 = friend.length();
            new AsyncTask<String, Void, Integer>() {
                @Override
                protected Integer doInBackground(String... params) {
                    int r = 0;
                    Socket clientSocket;
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        //Log.d("MyApp", "search for friend");
                        clientSocket = new Socket(params[0], SERVER_PORT);
                        inputStream = clientSocket.getInputStream();
                        outputStream = clientSocket.getOutputStream();
                        outputStream.write(3);
                        outputStream.write(c1);
                        outputStream.write(params[1].getBytes());
                        outputStream.write(c2);
                        outputStream.write(params[2].getBytes());
                        r = inputStream.read();
                        if (r == 1) {
                            Log.d("MyApp", "yes.. i will start");
                        }
                        if (r == 2) {
                            Log.d("MyApp", "i will be second");
                        }
                    } catch (IOException e) {
                        Log.d("MyApp", "error in doInBackground: " + e);
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //Log.d("MyApp", "return "+ r);
                    return r;
                }

                @Override
                protected void onPostExecute(Integer integer) {

                    Intent intent = new Intent(getBaseContext(), OnlineActivity.class);
                    intent.putExtra("whoStart", integer);
                    intent.putExtra("friend", friend);
                    intent.putExtra("myUserID", name);
                    intent.putExtra("SERVER_IP", SERVER_IP);
                    startActivity(intent);

                }
            }.execute(SERVER_IP, name, friend);

        } else {
            Toast.makeText(this, "there is no internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
