package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {
    private JTextField login;
    private JButton INVIOButton;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel loginPanel;
    private JPasswordField passwordField1;

    public Login() {
        login.setText("email/id");
        passwordField1.setText("password");
        login.setEnabled(false);
        passwordField1.setEnabled(false);
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.setText(null);
                login.setEnabled(true);
            }});
        passwordField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordField1.setEnabled(true);
                passwordField1.setText(null);
            }});

        INVIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (login.getText() != null && !(login.getText().equals("email/id")) && passwordField1.getText() != null && !(passwordField1.getText().equals("password"))) {
                    if(login.getText().equals("admin") && passwordField1.getText().equals("admin")) {
                        System.out.println(login.getText() + " " + passwordField1.getText());
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                        String username = login.getText();
                        frame.setContentPane(new DashBoardAdmin(username).getDashboardAdminPage());
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        //SwingUtilities.getWindowAncestor(INVIOButton).setVisible(false);
                        frame.pack();
                    } else {
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                        String username = login.getText();
                        frame.setContentPane(new DashBoardUser(username).getHomePage());
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        //SwingUtilities.getWindowAncestor(INVIOButton).setVisible(false);
                        frame.pack();
                    }
                }
            }
        });
    }

    private void createUIComponents() {


    }

    public JTextField getLogin() {
        return login;
    }

    public void setLogin(JTextField login) {
        this.login = login;
    }

    public JPasswordField getPassword() {
        return passwordField1;
    }

    public void setPassword(JPasswordField password) {
        this.passwordField1 = password;
    }

    public JButton getINVIOButton() {
        return INVIOButton;
    }

    public void setINVIOButton(JButton INVIOButton) {
        this.INVIOButton = INVIOButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(JPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public void setTextPanel(JPanel textPanel) {
        this.textPanel = textPanel;
    }
}

