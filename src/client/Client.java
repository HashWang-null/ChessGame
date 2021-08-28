package client;


import com.google.gson.Gson;
import message.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Client {
    Gson gson = new Gson();
    static Socket socket = null;
    BufferedReader br = null;
    BufferedWriter bw = null;

    GameFrame gameFrame = null;
    ListenerThread listener;

    boolean isMatched = false;
    String partnerInfo;
    String position;

    public void setFrame(GameFrame frame) {
        this.gameFrame = frame;
    }

    Client () {
    }

    public void connectionToServer() {
        try {
            Client.socket = new Socket("101.132.73.96", 9876);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ListenerThread listener = new ListenerThread(this);
            listener.start();
        } catch (SocketException se) {
            GameOptionPane.showNetError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String line) {
        if (socket == null || socket.isClosed()) {
            GameOptionPane.showNetError();
            return;
        }
        try {
            bw.write(line + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestPartner() {
        try {
            Message msg = new Message(
                    "ServerCommand",
                    "RequestPartner"
            );
            sendMessage(gson.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void matchPartner(String partnerAddress, String position) {
        if (position.equals("guest")) {
//            this.gameFrame.gamePanel.initSession();
            this.position = position;
            this.isMatched = true;
            this.gameFrame.rightPanel.setPartner(partnerAddress);
        } else if (position.equals("host")){
            this.position = position;
            this.isMatched = true;
            this.gameFrame.rightPanel.setPartner(partnerAddress);
//            this.gameFrame.gamePanel.color = GamePanel.BLACK_TYPE;
//            startSession(GamePanel.WHITE_TYPE);
            this.gameFrame.gamePanel.initSession(GamePanel.BLANK);
            sendStartSession(GamePanel.WHITE_TYPE);
        } else {
            throw new RuntimeException();
        }
    }

    public void setNonePartner() {
        this.isMatched = false;
        this.position = null;
        this.gameFrame.rightPanel.setNonePartner();
    }

    public void setMatchingPartner() {
        this.isMatched = false;
        this.position = null;
        this.gameFrame.rightPanel.setMatchingPartner();
    }

    public void sendStartSession(int p_color) {
        if (!this.position.equals("host")) {
            throw new RuntimeException("不是主持人还想开局？");
        }
        Message t_msg = new Message(
                "ClientCommand",
                "StartSession",
                new HashMap<String, String>() {
                    {
                        put("color", String.valueOf(p_color));
                    }
                });
        this.sendMessage(gson.toJson(t_msg));
        //等待对方确认
    }

    public BufferedReader getBr() {
        return br;
    }
}
