package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {
    private JTextField login;
    private JTextField password;
    private JButton INVIOButton;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel loginPanel;

    public Login() {
        login.setText("email/id");
        password.setText("password");
        login.setEnabled(false);
        password.setEnabled(false);
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.setEnabled(true);
                login.setText(null);
            }});
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                password.setEnabled(true);
                password.setText(null);
            }});

        INVIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                String username = login.getText();
                frame.setContentPane(new DashBoardUser(username).getHomePage());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //SwingUtilities.getWindowAncestor(INVIOButton).setVisible(false);
                frame.pack();
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

    public JTextField getPassword() {
        return password;
    }

    public void setPassword(JTextField password) {
        this.password = password;
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

