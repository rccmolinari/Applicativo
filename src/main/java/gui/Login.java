package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.Controller;

public class Login {

    // Campo di testo per inserire username o email dell'utente
    private JTextField login;

    // Bottone per confermare il login (submit)
    private JButton INVIOButton;

    // Pannello principale che contiene tutta l'interfaccia grafica della schermata di login
    private JPanel mainPanel;

    // Pannelli secondari usati per organizzare la UI in modo più ordinato e gestibile
    private JPanel textPanel;
    private JPanel loginPanel;

    // Campo per l'inserimento della password, con caratteri nascosti (puntini)
    private JPasswordField passwordField1;

    // Bottone che permette di aprire la schermata di registrazione (per utenti nuovi)
    private JButton REGISTRATIButton;

    // Riferimento al controller che gestisce la logica di login e registrazione,
    // separando la UI dalla logica applicativa (pattern MVC)
    private Controller controller;

    /**
     * Costruttore della classe Login.
     * Qui si inizializza la GUI, si settano placeholder, si configurano i listener per gli eventi
     * e si crea il controller che gestirà le operazioni di login e registrazione.
     */
    public Login() {
        // Creo un'istanza del controller per poter chiamare le funzioni di login e registrazione
        controller = new Controller();

        // Imposto un testo "placeholder" nei campi login e password
        // ma disabilito i campi per costringere l'utente a cliccarci sopra per poter scrivere
        login.setText("email/id");
        passwordField1.setText("password");
        login.setEnabled(false);
        passwordField1.setEnabled(false);

        // Aggiungo un listener per il click del mouse sul campo login
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Quando l'utente clicca, svuoto il testo placeholder
                login.setText(null);
                // E abilito il campo per poter scrivere
                login.setEnabled(true);
            }
        });

        // Simile al campo login, aggiungo listener al campo password
        passwordField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Abilito il campo password e cancello il placeholder
                passwordField1.setEnabled(true);
                passwordField1.setText(null);
            }
        });

        // Configuro l'azione da fare quando si preme il bottone "INVIO"
        INVIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Prendo i dati inseriti dall'utente nei campi
                String username = login.getText(); // o email
                String password = new String(passwordField1.getPassword());

                // Chiamo il metodo di login del controller
                controller.login(username, password);

                // Prendo il JFrame contenitore
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(INVIOButton);
                JPanel content;

                // Controllo chi ha effettuato il login
                if (controller.getUtente() != null) {
                    content = new DashBoardUser(username, controller).getHomePage();
                    frame.setContentPane(content);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();

                } else if (controller.getAdmin() != null) {
                    content = new DashBoardAdmin(username, controller).getDashboardAdminPage();
                    frame.setContentPane(content);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();

                } else {
                    // Login fallito
                    JOptionPane.showMessageDialog(null,
                            "Login fallito! Utente non registrato!",
                            "Errore di autenticazione",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });



        // Se clicchi su "REGISTRATI", chiamo il metodo per aprire l'interfaccia di registrazione
        REGISTRATIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Passo questa istanza di Login al controller per gestire la transizione
                controller.interfacciaRegistrazione(Login.this);
            }
        });
    }

    /**
     * Metodo vuoto richiesto da IntelliJ o altri tool per creare componenti personalizzati.
     * Qui si potrebbe implementare la creazione manuale di componenti Swing se necessario.
     */
    private void createUIComponents() {
    }

    // --- Getter e Setter ---
    // Consentono di accedere e modificare i componenti UI da altre classi,
    // utile per aggiornare la UI o recuperarne dati.

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
