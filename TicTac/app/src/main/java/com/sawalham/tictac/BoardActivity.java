package com.sawalham.tictac;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by mohammad on 5/12/2015.
 */

public class BoardActivity extends Activity {
    static boolean My_Turn = true;
    int My_Icon = 2, Second_Icon = 1, My_Icon_Id = R.drawable.x, Second_Icon_Id = R.drawable.oo;
    int id, board_x = 0, board_y = 0;
    int[][] Board;
    public ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Board = new int[3][3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Board[j][i] = 0;
            }
        }
        if(MainActivity.gameType== MainActivity.GameType.OneVsOne)
            My_Turn = true;
        if (MainActivity.MyIcon == 1 && My_Turn) {
            My_Icon_Id = R.drawable.oo;
            My_Icon = 1;
            Second_Icon_Id = R.drawable.x;
            Second_Icon = 2;
        }
        if (!My_Turn && MainActivity.gameType != MainActivity.GameType.OneVsOne) {
            PlayVsMobile();
            My_Turn = true;
        }
    }

    public void Play(View v) {
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
        if (Board[board_x][board_y] != 0) {
            Dialog("InValid", "not empty!", false);
            return;
        }
        btn = (ImageButton) findViewById(id);
        TextView label = (TextView) findViewById(R.id.moveTextView);
        if (My_Turn) {
            btn.setImageResource(My_Icon_Id);
            Board[board_x][board_y] = My_Icon;
            if (MainActivity.gameType == MainActivity.GameType.OneVsOne) {
                if (My_Icon == 2)
                    label.setText("O Turn");
                else
                    label.setText("X Turn");
                My_Turn = false;
            } else {
                if ((MainActivity.gameType == MainActivity.GameType.Normal) || (MainActivity.gameType == MainActivity.GameType.Easy)) {
                    PlayVsMobile();
                /*
                 String s = "";
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        s += Board[j][i];
                    }
                }
                Log.d("MyApp", "PlayVsMobile Board changed : " + s);
                */
                }
            }
        } else {
            btn.setImageResource(Second_Icon_Id);
            Board[board_x][board_y] = Second_Icon;
            My_Turn = true;
            if (My_Icon == 2)
                label.setText("X Turn");
            else
                label.setText("O Turn");
        }
        checkForWinner();
    }

    public void MobilePlay(int board_x, int board_y) {
        int btnid = 0;
        if (board_x == 0 && board_y == 0)
            btnid = R.id.button1;
        if (board_x == 1 && board_y == 0)
            btnid = R.id.button2;
        if (board_x == 2 && board_y == 0)
            btnid = R.id.button3;
        if (board_x == 0 && board_y == 1)
            btnid = R.id.button4;
        if (board_x == 1 && board_y == 1)
            btnid = R.id.button5;
        if (board_x == 2 && board_y == 1)
            btnid = R.id.button6;
        if (board_x == 0 && board_y == 2)
            btnid = R.id.button7;
        if (board_x == 1 && board_y == 2)
            btnid = R.id.button8;
        if (board_x == 2 && board_y == 2)
            btnid = R.id.button9;
        btn = (ImageButton) findViewById(btnid);
        Board[board_x][board_y] = Second_Icon;
        btn.setImageResource(Second_Icon_Id);
    }

    public void Dialog(String title, String text, Boolean setPositiveButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (title != null)
            alert.setTitle(title);
        else
            alert.setTitle("congratulations");
        if (text.equals("O") || text.equals("X"))
            alert.setMessage(text + " Player wins!");
        else
            alert.setMessage(text);
        alert.setCancelable(false);

        final Boolean p = setPositiveButton;
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (p)
                            finish();
                    }
                });
        alert.create().show();
    }

    public void checkForWinner() {
        String icon_me = "X";
        String icon_second = "O";
        if (My_Icon == 1) {
            icon_me = "O";
            icon_second = "X";
        }
        boolean empty = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (Board[j][i] == 0) {
                    empty = true;
                    break;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (Board[i][0] == Board[i][1] && Board[i][2] == Board[i][1])
                if (Board[i][1] == My_Icon)
                    Dialog(null, icon_me, true);
                else if (Board[i][1] == Second_Icon)
                    Dialog(null, icon_second, true);
        }
        for (int i = 0; i < 3; i++) {
            if (Board[0][i] == Board[1][i] && Board[1][i] == Board[2][i]) {
                if (Board[0][i] == My_Icon)
                    Dialog(null, icon_me, true);
                else if (Board[0][i] == Second_Icon)
                    Dialog(null, icon_second, true);
            }
        }
        if (Board[0][0] == Board[1][1] && Board[1][1] == Board[2][2]) {
            if (Board[0][0] == My_Icon)
                Dialog(null, icon_me, true);
            else if (Board[0][0] == Second_Icon)
                Dialog(null, icon_second, true);
        }
        if (Board[0][2] == Board[1][1] && Board[1][1] == Board[2][0]) {
            if (Board[0][2] == My_Icon)
                Dialog(null, icon_me, true);
            else if (Board[0][2] == Second_Icon)
                Dialog(null, icon_second, true);
        }

        if (!empty) {
            Dialog("Draw!", "Draw", true);
        }
    }

    public void PlayVsMobile() {
        if (Board[1][1] == 0 && MainActivity.gameType == MainActivity.GameType.Normal) {
            MobilePlay(1, 1);
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (((Board[i][0] == Board[i][1]) && (Board[i][0] != 0)) || ((Board[i][0] == Board[i][2]) && (Board[i][0] != 0)) || ((Board[i][1] == Board[i][2]) && (Board[i][2] != 0))) {
                for (int j = 0; j < 3; j++) {
                    if (Board[i][j] == 0) {
                        //Log.d("MyApp", "2 i=" + i + "j" + j);
                        MobilePlay(i, j);
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (((Board[0][i] == Board[1][i]) && (Board[0][i] != 0)) || ((Board[0][i] == Board[2][i]) && (Board[0][i] != 0)) || ((Board[1][i] == Board[2][i]) && (Board[2][i] != 0))) {
                for (int j = 0; j < 3; j++) {
                    if (Board[j][i] == 0) {
                        // Log.d("MyApp", "3 j=" + j + "i" + i);
                        MobilePlay(j, i);
                        return;
                    }
                }
            }
        }

        if (Board[0][0] == Board[1][1] && Board[2][2] == 0) {
            MobilePlay(2, 2);
            return;
        }

        if (Board[2][2] == Board[1][1] && Board[0][0] == 0) {
            MobilePlay(0, 0);
            return;
        }

        if (Board[2][0] == Board[1][1] && Board[0][2] == 0) {
            MobilePlay(0, 2);
            return;
        }
        if (Board[0][2] == Board[1][1] && Board[2][0] == 0) {
            MobilePlay(2, 0);
            return;
        }
        if (MainActivity.gameType == MainActivity.GameType.Normal) {
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j += 2) {
                    if (Board[i][j] == 0) {
                        MobilePlay(i, j);
                        return;
                    }
                }
            }
        }
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (Board[i][j] == 0) {
                    MobilePlay(i, j);
                    return;
                }
            }
        }
    }
}
