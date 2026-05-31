package dao;
import model.AccountModel;

public interface AccountDao {
    public AccountModel loginAccount(String accountNum, String pin) throws Exception;
}
