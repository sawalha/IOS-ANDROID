package com.sawalham.TicTacServer;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by mohammad on 5/25/2015.
 */

public class ClientTread extends Thread {
    final static int DATA_UPLOADED_SUCCESSFUL = 200;

    Socket clientSocket;

    public ClientTread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    InputStream inputStream = null;
    OutputStream outputStream = null;
    static ArrayList<GamePlay> gamePlays = new ArrayList<GamePlay>();
    static ArrayList<Players> onlinePlayers = new ArrayList<Players>();

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();

            int action = inputStream.read();
            switch (action) {
                case 1:
                    upload();
                    break;
                case 2:
                    download();
                    break;
                case 3:
                    checkForPlayers();
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception ex) {

            }
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception ex) {

            }
        }
    }

    public void checkForPlayers() {
        try {
            int c1 = inputStream.read();
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[c1];
            inputStream.read(buffer, 0, c1);
            stringBuilder.append(new String(buffer, 0, c1));
            String player1 = stringBuilder.toString();
            //System.out.println("player1 " + player1 + " c1 " + c1);
            int c2 = inputStream.read();
            stringBuilder = new StringBuilder();
            buffer = new byte[c2];
            inputStream.read(buffer, 0, c2);
            stringBuilder.append(new String(buffer, 0, c2));
            String player2 = stringBuilder.toString();
            //System.out.println("player2 " + player2 + " c2 " + c2);

            boolean invited = false;
            // System.out.println("size " + onlinePlayers.size());
            if (onlinePlayers.size() > 0) {
                for (Players p : onlinePlayers) {
                    // System.out.println("for");
                    if (p.p2.equals(player1) && p.p1.equals(player2)) {
                        invited = true;
                        // System.out.println("true");
                        // if return 1 player 1 will play first and icon is X
                        outputStream.write(1);
                        outputStream.close();
                        onlinePlayers.remove(p);
                        //System.out.println("return 1");
                        break;
                    }
                }
            }
            if (!invited) {
                Players players = new Players();
                players.p1 = player1;
                players.p2 = player2;
                onlinePlayers.add(players);
                outputStream.write(2);
                outputStream.close();
                //System.out.println("return 2 and size " + onlinePlayers.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download() {
        try {
            int c = inputStream.read();
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[c];
            inputStream.read(buffer, 0, c);
            stringBuilder.append(new String(buffer, 0, c));
            String player = stringBuilder.toString();
            //System.out.println("download for player " + player);
            if (gamePlays.size() > 0) {
                for (GamePlay gamePlay : gamePlays) {
                    if (gamePlay.playerTurn.equals(player) && !gamePlay.downloaded) {

                        int response = checkForWinner(gamePlay.BoardArray);
                        System.out.println("download for " + player + " response " + response);

                        outputStream.write(response);
                        outputStream.write(gamePlay.BoardString.getBytes());
                        gamePlay.downloaded = true;
                        System.out.println("Downloaded: " + gamePlay.BoardString + "from : " + gamePlay.played + " to: " + gamePlay.playerTurn);
                        gamePlays.remove(gamePlay);
                        break;
                    }
                }
            } else {
                outputStream.write(201); // there is no update
                System.out.println("no update for " + player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void upload() {
        GamePlay gamePlay = new GamePlay();
        try {
            gamePlay.downloaded = false;
            gamePlay.played_count = inputStream.read();
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[gamePlay.played_count];
            inputStream.read(buffer, 0, gamePlay.played_count);
            stringBuilder.append(new String(buffer, 0, gamePlay.played_count));
            gamePlay.played = stringBuilder.toString();

            gamePlay.playerTurn_count = inputStream.read();
            stringBuilder = new StringBuilder();
            buffer = new byte[gamePlay.playerTurn_count];
            inputStream.read(buffer, 0, gamePlay.playerTurn_count);
            stringBuilder.append(new String(buffer, 0, gamePlay.playerTurn_count));
            gamePlay.playerTurn = stringBuilder.toString();

            buffer = new byte[10];
            stringBuilder = new StringBuilder();
            inputStream.read(buffer, 0, 10);
            stringBuilder.append(new String(buffer, 0, 10));
            gamePlay.BoardString = stringBuilder.toString();
            gamePlay.BoardArray = new int[3][3];
            int BoardInput = 0;
            int response = 9;
            if (gamePlay.BoardString != null && gamePlay.BoardString.length() == 10) {
                BoardInput = Integer.parseInt(gamePlay.BoardString);
                System.out.println("BoardInput : " + BoardInput);
                for (int i = 2; i >= 0; i--) {
                    for (int j = 2; j >= 0; j--) {
                        gamePlay.BoardArray[j][i] = BoardInput % 10;
                        BoardInput /= 10;
                    }
                }
                response = checkForWinner(gamePlay.BoardArray);
                gamePlays.add(gamePlay);
            }

            System.out.println("download response " + response);
            if (response > 0 && response < 3)
                outputStream.write(response);
            else
                outputStream.write(DATA_UPLOADED_SUCCESSFUL);
            System.out.println("DATA_UPLOADED_SUCCESSFUL from:" + gamePlay.played + "to:" + gamePlay.playerTurn + "and the board :" + gamePlay.BoardString);

        } catch (IOException e) {
            System.out.println("DATA_UPLOADED_ERROR");
            e.printStackTrace();
        }

    }

    public int checkForWinner(int[][] Board) {

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
            if (Board[i][0] == 1 && Board[i][1] == 1 && Board[i][2] == 1)
                return 1;
            if (Board[i][0] == 2 && Board[i][1] == 2 && Board[i][2] == 2)
                return 2;
        }
        for (int i = 0; i < 3; i++) {
            if (Board[0][i] == Board[1][i] && Board[1][i] == Board[2][i]) {
                if (Board[0][i] == 1)
                    return 1;
                else if (Board[0][i] == 2)
                    return 2;
            }
        }
        if (Board[0][0] == Board[1][1] && Board[1][1] == Board[2][2]) {
            if (Board[0][0] == 1)
                return 1;
            else if (Board[0][0] == 2)
                return 2;
        }
        if (Board[0][2] == Board[1][1] && Board[1][1] == Board[2][0]) {
            if (Board[0][2] == 1)
                return 1;
            else if (Board[0][2] == 2)
                return 2;
        }

        if (!empty) {
            return 3;
        }
        return 0;

    }

}
