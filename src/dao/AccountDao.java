package dao;

public interface AccountDao {
    public boolean loginAccount(int accountNum, int pin) throws Exception;
}
