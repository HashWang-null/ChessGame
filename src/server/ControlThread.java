package server;


import com.google.gson.Gson;
import message.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ControlThread extends Thread {
    Gson gson = new Gson();
    ServerSocket ss;
    ServerThread[] threads;

    ControlThread () throws IOException {
        ss = new ServerSocket(9876);
        threads = new ServerThread[50];
    }
    ControlThread (int threadNum) throws IOException {
        ss = new ServerSocket(9876);
        threads = new ServerThread[threadNum];
    }

    private ServerThread getServerThread(Socket socket) {
        for (int i = 0; i < threads.length; ++i) {
            if (threads[i] == null || !threads[i].isAlive()) {
                threads[i] = new ServerThread(this, socket);
                return threads[i];
            }
        }
        return null;
    }

    public void show() {
        for (int i = 0; i < threads.length; ++i) {
            if (threads[i] == null) {
                System.out.println(i +  "- NULL");
            } else {
                String s = i + "- " + threads[i].getAddress() + ":" + threads[i].getPort() + " IsMatching:" + threads[i].isMatching + " ";
                if (threads[i].isAlive()) {
                    if (threads[i].partnerThread != null) {
                        s += " P: " + threads[i].partnerThread.getAddress() + ":" + threads[i].partnerThread.getPort();
                    } else {
                        s += " NULL";
                    }
                    System.out.println(s);
                } else {
                    s += " [NOT ALIVE] ";
                    System.out.println(s);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("服务器控制线程启动");
            while (!isInterrupted()) {
                if (ss.isClosed()) {
                    break;
                }
                Socket socket = ss.accept();
                ServerThread thread = getServerThread(socket);
                String address = socket.getInetAddress().getHostAddress();
                String port = String.valueOf(socket.getPort());
                if (thread == null) {
                    // 创建新线程失败，线程池已满
                    System.out.println("创建新线程失败，线程池已满," + address + ":" + port);
                    Message message = new Message(
                            "ServerReply",
                            "ConnectionFailed",
                            new HashMap<String, String>() {
                                {
                                    put("info", "服务器已满，请稍后再试");
                                }
                            });
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write(gson.toJson(message) + "\n");
                    bw.flush();
                    bw.close();
                    socket.close();
                } else {
                    // 创建新线程成功
                    System.out.println("创建新线程成功," + address + ":" + port);
                    thread.start();
                    Message message = new Message(
                            "ServerReply",
                            "ConnectionSuccessfully",
                            new HashMap<String, String>(){
                                {
                                    put("info", "连接服务器成功");
                                }
                            });
                    thread.sendMessage(gson.toJson(message));
                }
            }
        } catch (SocketException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.out.println("触发controlThread.run()中的finally");
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        endThreads();
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务器控制线程退出");
    }

    private void endThreads() {
        Message message = new Message(
                "ServerReply",
                "ConnectionFailed",
                new HashMap<String, String>(){
                    {
                        put("info", "服务器失去连接");
                    }
                });
        String info = gson.toJson(message);
        if (threads != null) {
            for (ServerThread thread : threads) {
                if (thread != null && thread.isAlive()) {
                    thread.sendMessage(info);
                    thread.interrupt();
                }
            }
            threads = null;
        }
    }

    public ServerThread getIsMatchingThread(ServerThread requestThread) {
        for (ServerThread thread : threads) {
            if (thread != null && thread.isAlive() && thread.isMatching && requestThread != thread) {
                return thread;
            }
        }
        return null;
    }
}
