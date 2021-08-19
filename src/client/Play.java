package client;

public class Play {

    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        GameFrame frame = GameFrame.getInstance(gamePanel);
        Client client = new Client();
        GameOptionPane.initGameFrame(frame, client);
        frame.setVisible(true);
        frame.setClient(client);
        client.connectionToServer();

    }
}
