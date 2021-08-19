package client;


import javax.swing.*;

public class GameOptionPane {
    private static GameFrame gf = null;
    private static Client client;

    public static void initGameFrame(GameFrame gf, Client client) {
        GameOptionPane.gf = gf;
        GameOptionPane.client = client;
    }

    public static void showNetError() {
        if (gf == null) {
            throw new RuntimeException("未初始化!");
        }
        Object[] options = {"重新连接", "退出"};
        int option = JOptionPane.showOptionDialog(
                gf,
                "无法连接到服务器",
                "网络错误",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (option == 0) {
            client.connectionToServer();
        } else {
            System.exit(0);
        }
    }

    public static void showServerError(String info) {
        if (gf == null) {
            throw new RuntimeException("未初始化!");
        }
        Object[] options = {"重新连接", "退出"};
        int option = JOptionPane.showOptionDialog(
                gf,
                info,
                "服务器连接错误",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (option == 0) {
            client.connectionToServer();
        } else {
            System.exit(0);
        }
    }

    public static void showPartnerError(String info) {
        if (gf == null) {
            throw new RuntimeException("未初始化!");
        }
        Object[] options = {"重新匹配", "退出"};
        int option = JOptionPane.showOptionDialog(gf,
            info,
            "警告",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
        if (option == 0) {
            client.setMatchingPartner();
            client.requestPartner();
        } else {
            System.exit(0);
        }
    }
}
