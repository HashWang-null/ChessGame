package client;

import javax.swing.*;
import java.awt.*;


public class SouthPanel extends JPanel {
    private Kernel kernel;

    public SouthPanel(Kernel kernel) {
        this.kernel = kernel;
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

    public void setTextArea(String line) {
        String str = this.textArea1.getText();
        str += line + "\n";
        this.textArea1.setText(str);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
