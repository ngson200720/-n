package vn.edu.englishcenter;

import vn.edu.englishcenter.view.LoginForm;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
