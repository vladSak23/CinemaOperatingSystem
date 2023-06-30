package logic;

import UI.LoginUI;

public class Main {
    public static String filePathToAccounts ="accounts.txt";

    public static void main(String[] args) {
        LoginUI.isFirstProgramStart = true;
        LoginUI loginUI = new LoginUI(filePathToAccounts);
    }
}
