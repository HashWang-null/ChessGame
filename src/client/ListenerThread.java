package client;


import com.google.gson.Gson;
import message.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ListenerThread extends Thread{
    static Socket socket = null;
    BufferedWriter bw = null;
    BufferedReader br = null;

    Client client;

    Gson gson = new Gson();

    ListenerThread(Client client) {
        this.client = client;
        initConnection();
    }

    public void initConnection() {
        ListenerThread.socket = Client.socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                line = br.readLine();
                System.out.println(line);
                if (line == null) {
                    break;
                }
                Message msg = gson.fromJson(line, Message.class);
                switch (msg.type) {
                    case "ServerReply":
                        doServerReply(msg);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doServerReply(Message msg) {
        try {
            switch (msg.title) {
                case "ConnectionFailed":
                    GameOptionPane.showServerError(msg.info.get("info"));
                    break;
                case "PartnerFailed":
                    GameOptionPane.showPartnerError(msg.info.get("info"));
                    break;
                case "MatchFailed":
//                    GameOptionPane.showPartnerError(msg.info.get("info"));
                    if (msg.info.get("bec") != null) {
                        client.gameFrame.rightPanel.setMatchingPartner();
                    }
                    break;
                case "MatchSuccessfully":
                    client.matchPartner(msg.info.get("address"), msg.info.get("position"));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
