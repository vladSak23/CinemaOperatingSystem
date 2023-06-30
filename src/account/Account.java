package account;

public class Account {
    private String name;
    private AccountType accountType;

    public Account(String name, AccountType accountType) {
        this.name = name;
        this.accountType = accountType;
    }

//    public Account(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
