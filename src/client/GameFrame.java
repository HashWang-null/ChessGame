package client;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private static final int FRAME_HEIGHT   = 840;
    private static final int FRAME_WIDTH    = 915;

    public Client client;
    public RightPanel rightPanel;
    public SouthPanel southPanel;
    public GamePanel gamePanel;

    private static GameFrame instance;



    public static GameFrame getInstance(GamePanel gamePanel) {
        if (instance == null) {
            instance = new GameFrame(gamePanel);
        }
        return instance;
    }


    public void setClient(Client client) {
        this.client = client;
        this.southPanel.setClient(client);
        this.rightPanel.setClient(client);
        this.gamePanel.setClient(client);
    }

    private GameFrame(GamePanel gamePanel) {
        this.setTitle("五子棋");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(getOwner());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.initPanels(gamePanel);
        this.gamePanel.setEnabled(false);
    }

    private void initPanels(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        rightPanel = new RightPanel();
        southPanel = new SouthPanel();
        rightPanel.setPreferredSize(new Dimension(240, 915));
        southPanel.setPreferredSize(new Dimension(840, 150));
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }
}
