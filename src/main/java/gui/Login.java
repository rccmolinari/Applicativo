package gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import controller.Controller;

public class Login {

    // Campo di testo per inserire username o email dell'utente
    private JTextField login;

    // Bottone per confermare il login (submit)
    private JButton buttonInvio;

    // Pannello principale che contiene tutta l'interfaccia grafica della schermata di login
    private JPanel mainPanel;

    // Pannelli secondari usati per organizzare la UI in modo più ordinato e gestibile
    private JPanel textPanel;
    private JPanel loginPanel;

    // Campo per l'inserimento della password, con caratteri nascosti (puntini)
    private JPasswordField passwordField1;

    // Bottone che permette di aprire la schermata di registrazione (per utenti nuovi)
    private JButton buttonRegistrati;

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
        buttonInvio.addActionListener(e -> {
            // Prendo i dati inseriti dall'utente nei campi
            String username = login.getText(); // o email
            String password = new String(passwordField1.getPassword());

            // Chiamo il metodo di login del controller
            controller.login(username, password);

            // Prendo il JFrame contenitore
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(buttonInvio);
            JPanel content;

            // Controllo chi ha effettuato il login
            if (controller.getUtente() != null) {
                content = new DashBoardUser(username, controller).getHomePage();
                frame.setContentPane(content);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                frame.pack();

            } else if (controller.getAdmin() != null) {
                content = new DashBoardAdmin(username, controller).getDashboardAdminPage();
                frame.setContentPane(content);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                frame.pack();

            } else {
                // Login fallito
                JOptionPane.showMessageDialog(null,
                        "Login fallito! Utente non registrato!",
                        "Errore di autenticazione",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });




        // Se clicchi su "REGISTRATI", chiamo il metodo per aprire l'interfaccia di registrazione
        buttonRegistrati.addActionListener(e -> controller.interfacciaRegistrazione());

    }


    // --- Getter e Setter ---
    // Consentono di accedere e modificare i componenti UI da altre classi,
    // utile per aggiornare la UI o recuperarne dati.

    public JPasswordField getPassword() {
        return passwordField1;
    }

    public void setPassword(JPasswordField password) {
        this.passwordField1 = password;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

}
