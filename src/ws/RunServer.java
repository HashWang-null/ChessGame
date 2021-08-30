package ws;

import org.java_websocket.WebSocketImpl;

/**
 *
 *
 * @author lonelyinnovator
 * @date 2021/8/30
 */
public class RunServer {

    public static void main(String[] args) {
        WebSocketImpl.DEBUG = false;
        int port = 9090; // 端口
        WsServer s = new WsServer(port);//实例化一个监听服务器
        s.start();//启动服务器
    }
}
