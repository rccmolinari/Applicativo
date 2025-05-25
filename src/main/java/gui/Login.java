package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.Controller;

public class Login {
    // Campo per inserire username o email
    private JTextField login;
    // Bottone per confermare il login
    private JButton INVIOButton;
    // Pannello principale che contiene tutta la UI di login
    private JPanel mainPanel;
    // Pannelli secondari per organizzare meglio la UI
    private JPanel textPanel;
    private JPanel loginPanel;
    // Campo password con i classici puntini nascosti
    private JPasswordField passwordField1;
    // Bottone per aprire la schermata di registrazione
    private JButton REGISTRATIButton;
    // Controller che gestisce la logica di login e registrazione
    private Controller controller;

    public Login() {
        // Creo il controller per poter chiamare le funzioni di login e registrazione
        controller = new Controller();

        // Qui metto dei placeholder iniziali, ma disabilito i campi così l’utente li deve cliccare per scrivere
        login.setText("email/id");
        passwordField1.setText("password");
        login.setEnabled(false);
        passwordField1.setEnabled(false);

        // Quando clicchi nel campo login, cancello il testo e abilito la scrittura
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.setText(null);
                login.setEnabled(true);
            }
        });

        // Stessa cosa per la password, al clic si abilita il campo e si svuota il testo
        passwordField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordField1.setEnabled(true);
                passwordField1.setText(null);
            }
        });

        // Quando premi il bottone “Invio” provo a fare il login chiamando il controller
        INVIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prendo username e password dai campi
                String username = login.getText();
                String password = new String(passwordField1.getPassword());
                // Provo il login
                controller.login(username, password);
                // Se il login va a buon fine (c’è utente o admin)
                if(controller.getUser() != null || controller.getAdmin() != null) {
                    // Prendo il frame principale attuale
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                    JPanel content;

                    // Se è admin, carico la sua dashboard, altrimenti la home dell’utente
                    if (controller.getUser() == null) {
                        content = new DashBoardAdmin(username, controller).getDashboardAdminPage();
                    } else {
                        content = new DashBoardUser(username, controller).getHomePage();
                    }
                    // Aggiorno il contenuto della finestra con la dashboard giusta
                    frame.setContentPane(content);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                } else {
                    // Se il login fallisce, mostro un messaggio di errore con le credenziali di prova
                    JOptionPane.showMessageDialog(null, "Login Failed" + "\n" +
                            " Le uniche credenziali disponibili sono \n" +
                            " user, user \n" +
                            " admin, admin");
                }
            }
        });

        // Se clicchi su “Registrati” apro la schermata di registrazione tramite il controller
        REGISTRATIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.interfacciaRegistrazione(Login.this);
            }
        });
    }

    // Metodo vuoto usato da IntelliJ o altri tool per creare componenti personalizzati
    private void createUIComponents() {
    }

    // Qui sotto tutti i getter e setter per accedere e modificare i componenti da altre parti
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
