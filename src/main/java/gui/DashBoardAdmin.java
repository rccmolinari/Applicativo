package gui;

import javax.swing.*;

public class DashBoardAdmin {

    private JPanel dashboardAdminPage;
    private JLabel adminLabel;

    public DashBoardAdmin(String username) {
        adminLabel.setText("Bentornato "+username+" nella sezione admin");
    }

    public JPanel getDashboardAdminPage() {
        return dashboardAdminPage;
    }

    public void setDashboardAdminPage(JPanel dashboardAdminPage) {
        this.dashboardAdminPage = dashboardAdminPage;
    }
}
