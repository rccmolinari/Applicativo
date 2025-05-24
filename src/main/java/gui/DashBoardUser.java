package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;


public class DashBoardUser {
    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JButton LOGOUTUSER;
    private JLabel welcomeTextLabel;
    private JComboBox comboBox1;
    private JLabel outputLabel;
    private String username;
    private JDialog choiceDialog = null;
    private Controller controller;

    public DashBoardUser(String username, Controller controller) {
        this.controller = controller;
        String [] choices = {"", "Visualizza Voli", "Prenota Volo", "Cerca Prenotazione", "Segnala Smarrimento"};
        comboBox1.setModel(new DefaultComboBoxModel(choices));
        this.username = username;
        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    controller.selectedItem((String) e.getItem(),DashBoardUser.this);

                }
            }
        });
        welcomeTextLabel.setText("ID: "+username);
        LOGOUTUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });
    }

    public JLabel getWelcomeTextLabel() {
        return welcomeTextLabel;
    }

    public void setWelcomeTextLabel(JLabel welcomeTextLabel) {
        this.welcomeTextLabel = welcomeTextLabel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWelcomeText(JPanel welcomeText) {
        this.welcomeText = welcomeText;
    }

    public JPanel getHomePage() {
        return homePage;
    }

    public void setHomePage(JPanel homePage) {
        this.homePage = homePage;
    }

    public JPanel getWelcomeText() {
        return welcomeText;
    }

    public JPanel getWelcomePanel() {
        return welcomePanel;
    }

    public void setWelcomePanel(JPanel welcomePanel) {
        this.welcomePanel = welcomePanel;
    }

    public JButton getLOGOUTUSER() {
        return LOGOUTUSER;
    }

    public void setLOGOUTUSER(JButton LOGOUTUSER) {
        this.LOGOUTUSER = LOGOUTUSER;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
    }

    public JLabel getOutputLabel() {
        return outputLabel;
    }

    public void setOutputLabel(JLabel outputLabel) {
        this.outputLabel = outputLabel;
    }

    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
