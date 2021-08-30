package client;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final int FRAME_HEIGHT   = 840;
    private static final int FRAME_WIDTH    = 915;

    private Kernel kernel;

    public MainFrame(Kernel kernel, GamePanel gamePanel, EastPanel eastPanel, SouthPanel southPanel) {
        this.kernel = kernel;
        this.setTitle("五子棋");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(getOwner());
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        initPanels(gamePanel, eastPanel, southPanel);
    }

    private void initPanels(GamePanel gamePanel, EastPanel eastPanel, SouthPanel southPanel) {
        eastPanel.setPreferredSize(new Dimension(240, 915));
        southPanel.setPreferredSize(new Dimension(840, 150));
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }

}
