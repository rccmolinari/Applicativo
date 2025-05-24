package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.Controller;

public class Login {
    private JTextField login;
    private JButton INVIOButton;
    private JPanel mainPanel;
    private JPanel textPanel;
    private JPanel loginPanel;
    private JPasswordField passwordField1;
    private JButton REGISTRATIButton;
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
            controller.login(username, password);
            if(controller.getUser() != null || controller.getAdmin() != null) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
            JPanel content;

            if (controller.getUser() == null) {
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
    });
        REGISTRATIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Registrazione Utente");
                dialog.setModal(true);
                dialog.setSize(400, 400);
                dialog.setLocationRelativeTo(null);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JTextField emailField = new JTextField();
                JPasswordField passwordField = new JPasswordField();
                JTextField idDocField = new JTextField();
                JTextField nomeField = new JTextField();
                JTextField cognomeField = new JTextField();
                JTextField dataNascitaField = new JTextField();

                panel.add(new JLabel("Email/Username"));
                panel.add(emailField);
                panel.add(new JLabel("Password"));
                panel.add(passwordField);
                panel.add(new JLabel("ID Documento"));
                panel.add(idDocField);
                panel.add(new JLabel("Nome"));
                panel.add(nomeField);
                panel.add(new JLabel("Cognome"));
                panel.add(cognomeField);
                panel.add(new JLabel("Data di nascita (formato: yyyy-mm-dd)"));
                panel.add(dataNascitaField);

                JButton confermaButton = new JButton("Conferma Registrazione");
                panel.add(confermaButton);

                dialog.setContentPane(panel);

                confermaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        String email = emailField.getText().trim();
                        String password = new String(passwordField.getPassword()).trim();
                        String idDoc = idDocField.getText().trim();
                        String nome = nomeField.getText().trim();
                        String cognome = cognomeField.getText().trim();
                        String dataNascita = dataNascitaField.getText().trim();

                        if (email.isEmpty() || password.isEmpty() || idDoc.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Tutti i campi sono obbligatori!");
                            return;
                        }

                        boolean successo = controller.registraUtente(email, password, idDoc, nome, cognome, dataNascita);

                        if (successo) {
                            JOptionPane.showMessageDialog(dialog, "Registrazione completata!");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Registrazione fallita: utente già esistente?");
                        }
                    }
                });
                dialog.setVisible(true);
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

