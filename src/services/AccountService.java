package services;
import dao.AccountDao;
import config.DbConnection;
import model.AccountModel;
import util.JBcrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import exceptions.AccountNotFound;

public class AccountService implements AccountDao{
    private static AccountService instance;

    private AccountService() {};

    // everytime you call this method, it cheks if its indeed iniitalize or not
    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }


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

        boolean isSamePass = bcrypt.comparePassword(pin, account.getAccountPin());

        if(!isSamePass){
            throw new Exception("Incorrect Password");
        }

        return account;
    }

    public void withdraw(String accountNum, double amount) throws Exception {
        Connection connection = DbConnection.StartConnection();
        String getAccount = "SELECT balance, daily_withdrawn FROM accounts WHERE account_number = ?";
        PreparedStatement p1 = connection.prepareStatement(getAccount);
        p1.setString(1, accountNum);
        ResultSet resultSet = p1.executeQuery();

        if(!resultSet.next()) {
            throw new AccountNotFound("Account not found");
        }

        // get the values
        double currentBalance = resultSet.getDouble("balance");
        double currentWithdrawn = resultSet.getDouble("daily_withdrawn");

        if(currentBalance < amount) {
            throw new Exception("Insufficient balance");

        }

        if(currentWithdrawn + amount > 50000) {
            throw new Exception("Daily withdrawal limit of 50000 exceeded");
        }

        double newBalance = currentBalance - amount;
        double newWithdrawn = currentWithdrawn + amount;

        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        PreparedStatement p2 = connection.prepareStatement(updateBalance);
        p2.setDouble(1, newBalance);
        p2.setString(2, accountNum);
        p2.executeUpdate();

        String updateWithdrawn = "UPDATE accounts SET daily_withdrawn = ? WHERE account_number = ?";
        PreparedStatement p3 = connection.prepareStatement(updateWithdrawn);
        p3.setDouble(1, newWithdrawn);
        p3.setString(2, accountNum);
        p3.executeUpdate();

        System.out.println("Withdrawn an amount successfully: " + amount);
    }

    public void checkBalance () {}

    public void depositMoney () {}

    public void transferMoney () {}



}
