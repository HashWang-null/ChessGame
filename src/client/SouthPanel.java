/*
 * Created by JFormDesigner on Thu Aug 19 22:04:28 CST 2021
 */

package client;

import java.awt.*;
import javax.swing.*;

/**
 * @author Brainrain
 */
public class SouthPanel extends JPanel {
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }


    public SouthPanel() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();

        //======== this ========
        setLayout(new FlowLayout());

        //======== scrollPane1 ========
        {

            //---- textArea1 ----
            textArea1.setColumns(80);
            textArea1.setRows(8);
            textArea1.setEnabled(false);
            scrollPane1.setViewportView(textArea1);
        }
        add(scrollPane1);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
