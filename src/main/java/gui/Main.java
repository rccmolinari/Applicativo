package gui;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Home");
        Login login = new Login();
        frame.setResizable(false);
        frame.setContentPane(login.getMainPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
