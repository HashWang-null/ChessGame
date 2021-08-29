package client;

import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EastPanel extends JPanel {
    private Kernel kernel;

    public EastPanel(Kernel kernel) {
        this.kernel = kernel;
        initComponents();
        initListeners();
        setNoPartnerStatus();
    }

    //-------------------------------------公共方法------------------------------------------

    //设置为匹配状态
    public void setMatchingPartnerStatus() {
        this.label_PartnerInfo.setText("暂无匹配");
        this.button_MatchPartner.setText("匹配中");
        this.button_MatchPartner.setEnabled(false);
        this.button_Send.setEnabled(false);
        this.button_ReMatch.setEnabled(false);
        this.button_UndoChess.setEnabled(false);
        this.button_Surrender.setEnabled(false);
        this.textField1.setEnabled(false);
    }

    //设置为无匹配状态
    public void setNoPartnerStatus() {
        this.label_PartnerInfo.setText("暂无匹配");
        this.button_MatchPartner.setText("匹配对手");
        this.button_MatchPartner.setEnabled(true);
        this.button_Send.setEnabled(false);
        this.button_ReMatch.setEnabled(false);
        this.button_UndoChess.setEnabled(false);
        this.button_Surrender.setEnabled(false);
        this.textField1.setEnabled(false);
    }

    //设置为有匹配状态
    public void setPartnerStatus(String partnerInfo) {
        this.label_PartnerInfo.setText("匹配成功");
        this.button_MatchPartner.setText("匹配对手");
        this.button_MatchPartner.setEnabled(false);
        this.button_Send.setEnabled(true);
        this.button_ReMatch.setEnabled(true);
        this.button_UndoChess.setEnabled(true);
        this.button_Surrender.setEnabled(true);
        this.textField1.setEnabled(true);
        this.label_PartnerInfo.setText(partnerInfo);
    }

    //--------------------------------监听器-----------------------------------------------------
    private void initListeners() {

        this.button_MatchPartner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kernel == null) {
                    throw new RuntimeException("kernel为空");
                }
                kernel.request_partner();
            }
        });

        this.button_UndoChess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kernel == null) {
                    throw new RuntimeException("kernel为空");
                }
                kernel.undoDropChess(true);
            }
        });

        this.button_ReMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.button_Surrender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.button_Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line = textField1.getText();
                kernel.doChat(line, true);
            }
        });
    }

    //--------------------------------------------------------------------------------------
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label_PartnerInfo = new JLabel();
        button_MatchPartner = new JButton();
        button_ReMatch = new JButton();
        button_UndoChess = new JButton();
        button_Surrender = new JButton();
        textField1 = new JTextField();
        button_Send = new JButton();

        //======== this ========
        setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[59,fill]" +
                        "[61,fill]" +
                        "[38,fill]" +
                        "[55,fill]" +
                        "[33,fill]",
                // rows
                "[28]" +
                        "[25]" +
                        "[25]" +
                        "[26]" +
                        "[36]" +
                        "[29]" +
                        "[31]" +
                        "[35]" +
                        "[24]" +
                        "[]" +
                        "[21]" +
                        "[30]"));

        //---- label_PartnerInfo ----
        label_PartnerInfo.setText("\u672a\u5339\u914d");
        add(label_PartnerInfo, "cell 1 1");

        //---- button_MatchPartner ----
        button_MatchPartner.setText("\u5339\u914d\u5bf9\u624b");
        add(button_MatchPartner, "cell 1 3");

        //---- button_ReMatch ----
        button_ReMatch.setText("\u91cd\u65b0\u5339\u914d");
        add(button_ReMatch, "cell 1 5");

        //---- button_UndoChess ----
        button_UndoChess.setText("\u64a4\u9500");
        add(button_UndoChess, "cell 1 7");

        //---- button_Surrender ----
        button_Surrender.setText("\u8ba4\u8f93");
        add(button_Surrender, "cell 1 9");
        add(textField1, "cell 0 11 3 1");

        //---- button_Send ----
        button_Send.setText("\u53d1\u9001");
        add(button_Send, "cell 3 11");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label_PartnerInfo;
    private JButton button_MatchPartner;
    private JButton button_ReMatch;
    private JButton button_UndoChess;
    private JButton button_Surrender;
    private JTextField textField1;
    private JButton button_Send;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    //---------------------------------------------------------------------------------------
}
