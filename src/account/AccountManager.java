package account;

import java.io.*;

public class AccountManager {
    private String filePathToAccounts = "accounts.txt";

    public AccountManager(String filePathToAccounts) {
        this.filePathToAccounts = filePathToAccounts;
    }

    public Account findAccount(String name, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathToAccounts))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String accountTypeString = parts[0];
                String nickname = parts[1];
                String storedPassword = parts[2];

                if (nickname.equals(name) && storedPassword.equals(password)) {
                    AccountType accountType = AccountType.valueOf(accountTypeString);
                    return new Account(nickname, accountType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addClient(String name, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathToAccounts, true))) {
            String line = AccountType.CLIENT + "," + name + "," + password;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
