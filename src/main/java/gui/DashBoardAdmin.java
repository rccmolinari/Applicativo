package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DashBoardAdmin {

    private JPanel dashboardAdminPage;
    private JLabel adminLabel;
    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JLabel welcomeTextLabel;
    private JButton LOGOUTUSER;
    private JComboBox comboBox1;
    private JLabel outputLabel;

    public JLabel getAdminLabel() {
        return adminLabel;
    }

    public void setAdminLabel(JLabel adminLabel) {
        this.adminLabel = adminLabel;
    }

    public JPanel getHomePage() {
        return homePage;
    }

    public void setHomePage(JPanel homePage) {
        this.homePage = homePage;
    }

    public JPanel getWelcomePanel() {
        return welcomePanel;
    }

    public void setWelcomePanel(JPanel welcomePanel) {
        this.welcomePanel = welcomePanel;
    }

    public JPanel getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(JPanel welcomeText) {
        this.welcomeText = welcomeText;
    }

    public JLabel getWelcomeTextLabel() {
        return welcomeTextLabel;
    }

    public void setWelcomeTextLabel(JLabel welcomeTextLabel) {
        this.welcomeTextLabel = welcomeTextLabel;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public DashBoardAdmin(String username) {
        comboBox1.setModel(new DefaultComboBoxModel(new String[] {"", "Visualizza Voli", "Inserisci Volo", "Aggiorna Volo", "Modifica Gate", "Aggiorna Bagaglio", "Visualizza Smarrimento"}));
        this.username = username;
        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String item = e.getItem().toString();
                outputLabel.setVisible(true);

                outputLabel.setText("Hai scelto "+item+"... work in progress king");

            }
        });
        welcomeTextLabel.setText("Bentornato "+username+" nella homepage dell'aereoporto di Napoli");
        LOGOUTUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public JPanel getDashboardAdminPage() {
        return dashboardAdminPage;
    }

    public void setDashboardAdminPage(JPanel dashboardAdminPage) {
        this.dashboardAdminPage = dashboardAdminPage;
    }
}
