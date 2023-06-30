package UI;

import account.Account;
import account.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {

    private String filePathToLogin;
    private CinemaSystemApp cinemaSystem;
    private AccountManager accountManager;
    private JLabel labelUzytkownik;
    private JTextField nicknameField;
    private JLabel labelHaslo;
    private JPasswordField passwordField;
    private JButton logButton;
    private JButton registrationButton;
    public static boolean isFirstProgramStart;

    public boolean isFirstProgramStart() {
        return isFirstProgramStart;
    }

    public void setFirstProgramStart(boolean firstProgramStart) {
        isFirstProgramStart = firstProgramStart;
    }

    public LoginUI(String filePathToLogin) {
        cinemaSystem = new CinemaSystemApp();
        init(filePathToLogin);
    }

    public LoginUI(String filePathToLogin, CinemaSystemApp cinemaSystemApp) {
        this.cinemaSystem = cinemaSystemApp;
        init(filePathToLogin);
    }

    private void login(String username, String password) {
        Account account = accountManager.findAccount(username,password);
        if (account != null) {
            switch (account.getAccountType()){
                case ADMIN:
                    ConfigEditorUI configEditorUI = new ConfigEditorUI();
                    break;
                case CLIENT:
                    if (isFirstProgramStart) {
                        cinemaSystem.initialize(account);
                    } else {
                        cinemaSystem.getMainFrame().setVisible(true);
                        cinemaSystem.getNameLabel2().setText(account.getName());
                        cinemaSystem.getMoneyTextField().setText("");
                    }
                    dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Account not found");
        }
    }

    private void init(String filePathToLogin) {
        this.filePathToLogin = filePathToLogin;
        accountManager = new AccountManager(filePathToLogin);

        setTitle("Logowanie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        labelUzytkownik = new JLabel("Nickname:");
        nicknameField = new JTextField();
        labelHaslo = new JLabel("Pass:");
        passwordField = new JPasswordField();
        logButton = new JButton("Login");
        registrationButton = new JButton("Registration");

        logButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = nicknameField.getText();
                String password = new String(passwordField.getPassword());
                login(username, password);
            }
        });

        registrationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registration();
            }
        });

        add(labelUzytkownik);
        add(nicknameField);
        add(labelHaslo);
        add(passwordField);
        add(new JLabel());
        add(new JLabel());
        add(logButton);
        add(registrationButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registration() {
        JFrame frame = new JFrame("Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            accountManager.addClient(username,password);

            frame.dispose();
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(enterButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}



