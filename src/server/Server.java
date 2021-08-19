package server;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ControlThread controlThread = null;
        System.out.println("服务器启动");
        while (true) {

            String line = null;
            if ((line = br.readLine()) == null) {
                break;
            }

            if (line.equals("start")) {
                if (controlThread != null && controlThread.isAlive()) {
                    System.out.println("控制程序已经启动");
                }
                if (controlThread == null) {
                    controlThread = new ControlThread();
                }
                if (!controlThread.isAlive()) {
                    controlThread.start();
                }
            }

            if (line.equals("exit")) {
                if (controlThread != null) {
                    controlThread.exit();
                }
                break;
            }

            if (line.equals("show")) {
                if (controlThread == null) {
                    System.out.println("服务控制程序未启动");
                } else {
                    controlThread.show();
                }
            }

            if (line.equals("test")) {
                try {
                    int i = Integer.parseInt(br.readLine());
                    if (controlThread != null && controlThread.isAlive()) {
                        System.out.println("控制程序已经启动");
                    }
                    if (controlThread == null) {
                        controlThread = new ControlThread(i);
                    }
                    if (!controlThread.isAlive()) {
                        controlThread.start();
                    }
                } catch (Exception ignored) {

                }
            }
        }
        System.out.println("服务器退出");
        br.close();
    }
}
