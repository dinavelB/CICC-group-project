package dao;
import model.AccountModel;

public interface AccountDao {
    public AccountModel loginAccount(String accountNum, String pin) throws Exception;

    public void withdraw(String accountNum, double amount) throws Exception;

    public void checkBalance(String accountNum) throws Exception;

    public void depositMoney(String accountNum, double amount) throws Exception;

    public void transferMoney(String fromAccount, String toAccount, double amount) throws Exception;

    public void changePin(String accountNum, String oldPin, String newPin) throws Exception;
}
