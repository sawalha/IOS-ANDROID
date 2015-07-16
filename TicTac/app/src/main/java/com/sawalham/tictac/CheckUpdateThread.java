package com.sawalham.tictac;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by mohammad on 5/27/2015.
 */
public class CheckUpdateThread extends Thread {
    public boolean CheckUpdate;
    Activity activity;
    Handler handler;
    String myUserID, SERVER_IP , board;
    int My_Icon, Second_Icon ,My_Icon_Id ,Second_Icon_Id ,response;
    int temp_board[][] = new int[3][3];
    ImageButton btn;
    final int SERVER_PORT = 10000;

    public CheckUpdateThread(Activity activity, Handler handler, String myUserID, String SERVER_IP) {
        this.activity = activity;
        this.handler = handler;
        this.myUserID = myUserID;
        this.SERVER_IP = SERVER_IP;
    }

    @Override
    public void run() {

        while (CheckUpdate) {
            Socket clientSocket;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                clientSocket = new Socket(this.SERVER_IP, SERVER_PORT);
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();
                outputStream.write(2);
                outputStream.write(myUserID.getBytes().length);
                outputStream.write(myUserID.getBytes());
                response = inputStream.read();
                //Log.d("MyApp", "response " + response);
                if (response != 201 && response != -1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    byte[] buffer = new byte[10];
                    inputStream.read(buffer, 0, 10);
                    stringBuilder.append(new String(buffer, 0, 10));
                    board = stringBuilder.toString();
                    Log.d("MyApp", "board " + board);

                    int BoardInput;
                    if (board != null && board.length() == 10) {
                        BoardInput = Integer.parseInt(board);
                        Log.d("MyApp", "BoardInput : " + BoardInput);
                        for (int i = 2; i >= 0; i--) {
                            for (int j = 2; j >= 0; j--) {
                                temp_board[j][i] = BoardInput % 10;
                                BoardInput /= 10;
                            }
                        }
                        OnlineActivity.Board = temp_board;
                        Log.d("MyApp", "BoardInput AFTER FOR: " + BoardInput);

                        handler.post(new Runnable() {
                                         @Override
                                         public void run() {
                                             Log.d("MyApp", "handler");
                                             TextView label = (TextView) activity.findViewById(R.id.moveTextView);
                                             label.setText("your Turn");
                                             for (int board_y = 0; board_y < 3; board_y++) {
                                                 for (int board_x = 0; board_x < 3; board_x++) {
                                                     if (temp_board[board_x][board_y] == 0)
                                                         continue;
                                                     else {
                                                         int btnId = 0;
                                                         if (board_x == 0 && board_y == 0)
                                                             btnId = R.id.button1;
                                                         if (board_x == 1 && board_y == 0)
                                                             btnId = R.id.button2;
                                                         if (board_x == 2 && board_y == 0)
                                                             btnId = R.id.button3;
                                                         if (board_x == 0 && board_y == 1)
                                                             btnId = R.id.button4;
                                                         if (board_x == 1 && board_y == 1)
                                                             btnId = R.id.button5;
                                                         if (board_x == 2 && board_y == 1)
                                                             btnId = R.id.button6;
                                                         if (board_x == 0 && board_y == 2)
                                                             btnId = R.id.button7;
                                                         if (board_x == 1 && board_y == 2)
                                                             btnId = R.id.button8;
                                                         if (board_x == 2 && board_y == 2)
                                                             btnId = R.id.button9;
                                                         btn = (ImageButton) activity.findViewById(btnId);
                                                         if (temp_board[board_x][board_y] == My_Icon)
                                                             btn.setImageResource(My_Icon_Id);
                                                         if (temp_board[board_x][board_y] == Second_Icon)
                                                             btn.setImageResource(Second_Icon_Id);
                                                     }
                                                 }
                                             }
                                             OnlineActivity.My_Turn = true;
                                             if (response > 0 && response < 4) {
                                                 if (response == My_Icon) {
                                                     OnlineActivity.winner = 1; // if 1 - i am winner
                                                     Log.d("MyApp", "i am winner");
                                                 } else if (response == Second_Icon) {
                                                     OnlineActivity.winner = 2; // if 2 - the second player win
                                                     Log.d("MyApp", "second player win");
                                                 }
                                                 if (response == 3) {
                                                     OnlineActivity.winner = 3; // if 3 Draw
                                                     Log.d("MyApp", "Draw ");
                                                 }
                                                 OnlineActivity.h.post(OnlineActivity.r);
                                             }
                                         }
                                     }
                        );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
