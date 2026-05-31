package services;
import dao.AccountDao;
import connections.DbConnection;
import model.AccountModel;
import util.JBcrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import customexceptions.AccountNotFound;

public class AccountService implements AccountDao{

    public AccountModel loginAccount(String accountNum, String pin) throws Exception {
        JBcrypt bcrypt = new JBcrypt();
        AccountModel account =   new AccountModel();
        Connection connection = DbConnection.StartConnection();
        String foundAccount = "SELECT * FROM accounts WHERE account_number = ?";

//    create a pStatement with the connection method from dbConnection
        PreparedStatement p = connection.prepareStatement(foundAccount);
//        always setInt since each param or ? is counted default 1 onwards
        p.setString(1, accountNum);
//        results of the query using the prepared statement
        ResultSet resultSet = p.executeQuery();

        if(resultSet.next()){
           account.setFName(resultSet.getString("full_name"));
           account.setAccountNumber(resultSet.getString("account_number"));
           account.setPin(resultSet.getString("pin"));
           account.setBalance(resultSet.getInt("balance"));
           account.setDailyDeposit(resultSet.getInt("daily_deposit"));
           account.setIsActive(resultSet.getBoolean("is_active"));
           account.setDailyWithdrawn(resultSet.getInt("daily_withdrawn"));
        } else {
            throw new AccountNotFound("Account not found");
        }

        boolean isSamePass = bcrypt.comparePassword(pin, account.pin);

        if(!isSamePass){
            throw new Exception("Incorrect Password");
        }

        return account;
    }

}
