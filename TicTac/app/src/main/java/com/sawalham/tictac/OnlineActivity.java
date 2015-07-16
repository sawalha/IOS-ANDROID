package com.sawalham.tictac;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mohammad on 5/30/2015.
 */
public class OnlineActivity extends Activity {
    static boolean My_Turn = true;
    static int[][] Board;
    static int winner = 0, a;
    ImageButton btn;
    String myUserID, SERVER_IP, friend, text;
    final int SERVER_PORT = 10000;
    TextView label;
    Handler handler = new Handler();
    CheckUpdateThread checkUpdateThread;
    static android.os.Handler h = null;
    static Runnable r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        SERVER_IP = getIntent().getExtras().getString("SERVER_IP");
        friend = getIntent().getExtras().getString("friend");
        myUserID = getIntent().getExtras().getString("myUserID");
        text = "waiting for " + friend;
        label = (TextView) findViewById(R.id.moveTextView);
        int whoStart = getIntent().getExtras().getInt("whoStart");
        //Log.d("MyApp", "who will start? " + whoStart);
        checkUpdateThread = new CheckUpdateThread(this, handler, myUserID, SERVER_IP);
        checkUpdateThread.CheckUpdate = true;
        if (whoStart == 1) {
            checkUpdateThread.My_Icon = 2;
            checkUpdateThread.Second_Icon = 1;
            checkUpdateThread.My_Icon_Id = R.drawable.x;
            checkUpdateThread.Second_Icon_Id = R.drawable.o;
            My_Turn = true;
        }
        if (whoStart == 2) {
            label.setText(text);
            checkUpdateThread.My_Icon = 1;
            checkUpdateThread.Second_Icon = 2;
            checkUpdateThread.My_Icon_Id = R.drawable.o;
            checkUpdateThread.Second_Icon_Id = R.drawable.x;
            My_Turn = false;
        }
        Board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Board[j][i] = 0;
            }
        }
        checkUpdateThread.start();

        h = new android.os.Handler();
        r = new Runnable() {
            @Override
            public void run() {
                Log.d("MyApp", "searching for winners " + winner);
                if (winner != 0) {
                    if (winner == 1) {
                        Dialog("We have a winner", "You are the WINNER ", null);
                    }
                    if (winner == 2) {
                        Dialog("We have a winner", friend + " is the WINNER ", null);
                    }
                    if (winner == 3) {
                        Dialog("Draw", "Draw", null);
                    }
                }
            }

        };
    }

    public void Play(View v) {
        int id;
        int board_x = 0;
        int board_y = 0;
        id = v.getId();
        switch (id) {
            case R.id.button1:
                board_x = 0;
                board_y = 0;
                break;
            case R.id.button2:
                board_x = 1;
                board_y = 0;
                break;
            case R.id.button3:
                board_x = 2;
                board_y = 0;
                break;
            case R.id.button4:
                board_x = 0;
                board_y = 1;
                break;
            case R.id.button5:
                board_x = 1;
                board_y = 1;
                break;
            case R.id.button6:
                board_x = 2;
                board_y = 1;
                break;
            case R.id.button7:
                board_x = 0;
                board_y = 2;
                break;
            case R.id.button8:
                board_x = 1;
                board_y = 2;
                break;
            case R.id.button9:
                board_x = 2;
                board_y = 2;
                break;
        }

        btn = (ImageButton) findViewById(id);
        if (My_Turn) {
            if (Board[board_x][board_y] != 0) {
                Dialog("InValid", "not empty!", false);
                return;
            }
            btn.setImageResource(checkUpdateThread.My_Icon_Id);
            Board[board_x][board_y] = checkUpdateThread.My_Icon;
            label.setText(text);

            My_Turn = false;
            String BoardOutput = "1";
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    BoardOutput += Board[j][i];
                }
            }
            Log.d("MyApp", " BoardOutput  : " + BoardOutput);
            checkUpdateThread.CheckUpdate = false;
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Socket clientSocket;
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String result = "";
                    try {
                        clientSocket = new Socket(params[0], SERVER_PORT);
                        inputStream = clientSocket.getInputStream();
                        outputStream = clientSocket.getOutputStream();
                        outputStream.write(1);
                        outputStream.write(myUserID.getBytes().length);
                        outputStream.write(myUserID.getBytes());
                        outputStream.write(friend.getBytes().length);
                        outputStream.write(friend.getBytes());
                        outputStream.write(params[1].getBytes());
                        a = inputStream.read();
                        if (a > 0 && a < 4) {

                            if (a == checkUpdateThread.My_Icon) {
                                winner = 1; // if 1 - i am winner
                            } else if (a == checkUpdateThread.Second_Icon) {
                                winner = 2; // if 2 - the second player win
                            }
                            if (a == 3) {
                                winner = 3; // if 3 Draw
                            }
                            Log.d("MyApp", "winner " + winner);
                            h.post(r);
                        } else {
                            if (a == 200)
                                result = "okay..";
                            else
                                result = "not okay";
                        }
                    } catch (IOException e) {
                        Log.d("MyApp", "error in doInBackground of upload: " + e);
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
                    return result;
                }

                @Override
                protected void onPostExecute(String s) {
                    Log.d("MyApp", "upload : " + s);
                }
            }.execute(SERVER_IP, BoardOutput);

        } else {
            Dialog("InValid", "it's not your turn", false);
        }
        checkUpdateThread.CheckUpdate = true;
    }

    public void Dialog(String title, String text, Boolean gameEnd) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(text);
        final Boolean p = gameEnd;
        if (p != null) {
            /*
            if (p == true) {
                alert.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alert.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
            } else */
            if (p == false) {
                alert.setCancelable(true);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        } else {
            alert.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }
        alert.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkUpdateThread.CheckUpdate = false;
        checkUpdateThread = null;
    }
}
