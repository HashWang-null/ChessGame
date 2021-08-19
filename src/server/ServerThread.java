package server;


import com.google.gson.Gson;
import message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServerThread extends Thread {

    boolean isMatching = false;
    ServerThread partnerThread = null;
    ControlThread controlThread;

    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    Gson gson = new Gson();

    ServerThread(ControlThread controlThread, Socket socket) {
        this.controlThread = controlThread;
        this.socket = socket;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            bw.write(msg + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                parseMessage(line);
            }
        } catch (SocketException ignored) {
            System.out.println(getAddress() + ":" + getPort() + " 断开连接");
            if (this.partnerThread != null) {
                System.out.println(this.partnerThread);
                Message msg = new Message(
                        "ServerReply",
                        "MatchFailed",
                        new HashMap<String, String>() {
                            {
                                put("info", "对手离线");
                            }
                        }
                );
                String s_msg = gson.toJson(msg);
                this.partnerThread.sendMessage(s_msg);
                this.partnerThread.partnerThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (br != null) {
                    br.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseMessage(String line) {
        try {
            Message msg = gson.fromJson(line, Message.class);
            String type = msg.type;
            switch (type) {
                case "ServerCommand":
                    serverCommand(msg);
                    break;
                case "ClientCommand":
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serverCommand(Message message) {
        String command = message.title;
        switch (command) {
            case "RequestPartner":
                if (this.partnerThread != null && this.partnerThread.isAlive()) {
                    System.out.println("已经有匹配对手，不接受新的匹配请求");
                    break;
                }
                ServerThread serverThread = controlThread.getIsMatchingThread(this);
                if (serverThread == null) {
                    Message r_msg = new Message(
                            "ServerReply",
                            "MatchFailed",
                            new HashMap<String, String>(){
                                {
                                    put("bec", "NoPartner");
                                    put("info", "等待匹配对手");
                                }
                            });
                    String reply = gson.toJson(r_msg);
                    sendMessage(reply);
                    this.partnerThread = null;
                    this.isMatching = true;
                } else {
                    this.partnerThread = serverThread;
                    this.partnerThread.partnerThread = this;
                    Message r_msg = new Message(
                            "ServerReply",
                            "MatchSuccessfully",
                            new HashMap<String, String>() {
                                {
                                    put("address", partnerThread.getAddress());
                                    put("port", String.valueOf(partnerThread.getPort()));
                                    put("position", "guest");
                                }
                            });
                    Message t_msg = new Message(
                            "ServerReply",
                            "MatchSuccessfully",
                            new HashMap<String, String>() {
                                {
                                    put("address", getAddress());
                                    put("port", String.valueOf(getPort()));
                                    put("position", "host");
                                }
                            });
                    this.sendMessage(gson.toJson(r_msg));
                    this.partnerThread.sendMessage(gson.toJson(t_msg));
                    this.partnerThread.isMatching = false;
                    this.isMatching = false;
                }
                break;
            case "RemovePartner":
                Message t_msg = new Message(
                        "ServerReply",
                        "PartnerFailed",
                        new HashMap<String, String>() {
                            {
                                put("info", "对手离线");
                            }
                        });
                String reply = gson.toJson(t_msg);
                this.partnerThread.sendMessage(reply);
                this.partnerThread.partnerThread = null;
                this.partnerThread = null;
                break;
            case "RemoveConnection":
                break;
            default:
                break;
        }
    }

    private void clientCommand(Message message) {

    }
}
