package com.sawalham.tictac;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {
    final String SERVER_IP = "192.168.1.15";
    public enum GameType {OneVsOne, Normal, Easy}
    static GameType gameType;
    static int MyIcon=2; // X - 2     O - 1
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.hello);
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        if(sp.getBoolean("KEY", false)==false){
            Intent intent= new Intent(this,UserActivity.class);
            startActivity(intent);
            finish();}
        String name= sp.getString("NAME","name");
        textView.setText("Hello "+name );
    }
    public void Play(View v) {
        switch (v.getId()){
            case R.id.oneVsOneBtn :
                gameType = GameType.OneVsOne;
                Dialog(gameType);
                break;
            case R.id.normalBtn:
                gameType = GameType.Normal;
                Dialog(gameType);
                break;
            case R.id.easyBtn:
                gameType = GameType.Easy;
                Dialog(gameType);
                break;
            case R.id.onlineBtn:
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                intent.putExtra("SERVER_IP",SERVER_IP);
                startActivity(intent);
                break;
            case  R.id.settings:
                 Intent intent1 = new Intent(getBaseContext(), UserActivity.class);
                startActivity(intent1);
                break;
        }

    }
    public void Dialog(GameType gameType) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        if (gameType == GameType.OneVsOne) {
            alert.setTitle("who is start?");
            alert.setMessage("which player will start?");
            alert.setPositiveButton("O", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MyIcon = 1;
                    Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("X", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyIcon = 2;
                    Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (gameType == GameType.Easy || gameType == GameType.Normal) {
            final Intent intent = new Intent(MainActivity.this, BoardActivity.class);
            MyIcon = 2;
            alert.setTitle("who will start?");
            alert.setMessage("which player will start?");
            alert.setPositiveButton("I am", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }});
            alert.setNegativeButton("mobile", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BoardActivity.My_Turn = false;
                    startActivity(intent);
                }});
        }
        alert.create().show();
    }
}