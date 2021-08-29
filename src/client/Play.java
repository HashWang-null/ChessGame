package client;


public class Play {
    public static void main(String[] args) {
        Kernel kernel = new Kernel();

        GamePanel gamePanel = new GamePanel(kernel);
        EastPanel eastPanel = new EastPanel(kernel);
        SouthPanel southPanel = new SouthPanel(kernel);
        MainFrame mf = new MainFrame(kernel, gamePanel, eastPanel, southPanel);


        kernel.setEastPanel(eastPanel);
        kernel.setMainFrame(mf);
        kernel.setGamePanel(gamePanel);
        kernel.setSouthPanel(southPanel);

        mf.setVisible(true);
        //自动连接到服务器
        kernel.connectionToServer();
    }
}
