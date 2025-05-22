package gui;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.Controller;
import model.Amministratore;
import model.Utente;

public class Login {
    private JTextField login;
    private JButton INVIOButton;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private Controller controller;

    public Login() {
        controller = new Controller();

        login.setText("email/id");
        passwordField1.setText("password");
        login.setEnabled(false);
        passwordField1.setEnabled(false);

        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.setText(null);
                login.setEnabled(true);
            }
        });

        passwordField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordField1.setEnabled(true);
                passwordField1.setText(null);
            }
        });

        INVIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = login.getText();
                String password = new String(passwordField1.getPassword());

                if (username != null && !username.equals("email/id") && password != null && !password.equals("password")) {
                        Utente utente = controller.login(username, password);

                        if(utente != null) {
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                        JPanel content;

                        if (utente instanceof Amministratore) {
                            content = new DashBoardAdmin(username, controller).getDashboardAdminPage();
                        } else {
                            content = new DashBoardUser(username, controller).getHomePage();
                        }
                        frame.setContentPane(content);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Failed" + "\n" +
                                " Le uniche credenziali disponibili sono \n" +
                                " user, user \n" +
                                " admin, admin");
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

