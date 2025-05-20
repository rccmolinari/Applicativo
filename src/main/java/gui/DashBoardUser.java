package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashBoardUser {
    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JButton ADMINButton;
    private JLabel welcomeTextLabel;
    private JButton prenotaVoloButton;
    private JButton voliButton;
    private JButton cercaNPrenotazioneButton;
    private JButton segnalaNSmarrimentoButton;
    private String username;


    public DashBoardUser(String username) {
        this.username = username;
        welcomeTextLabel.setText("Bentornato "+username+" nella homepage dell'aereoporto di Napoli");
        ADMINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ADMINButton);
                frame.setResizable(false);
                String username = getUsername();
                if(username.equals("admin")) {
                    frame.setContentPane(new DashBoardAdmin(username).getDashboardAdminPage());
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    //SwingUtilities.getWindowAncestor(INVIOButton).setVisible(false);
                    frame.pack();
                }
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
}
