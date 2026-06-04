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


    @Override
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

    @Override
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

    @Override
    public void checkBalance(String accountNum) throws Exception {
        Connection connection = DbConnection.StartConnection();
        String getBalance = "SELECT balance FROM accounts WHERE account_number = ?";
        PreparedStatement p = connection.prepareStatement(getBalance);
        p.setString(1, accountNum);
        ResultSet resultSet = p.executeQuery();

        if(!resultSet.next()) {
            throw new AccountNotFound("Account not found");
        }

        double balance = resultSet.getDouble("balance");
        System.out.println("Current balance: " + balance);
    }

    @Override
    public void depositMoney(String accountNum, double amount) throws Exception {
        Connection connection = DbConnection.StartConnection();
        String getAccount = "SELECT balance, daily_deposit FROM accounts WHERE account_number = ?";
        PreparedStatement p1 = connection.prepareStatement(getAccount);
        p1.setString(1, accountNum);
        ResultSet resultSet = p1.executeQuery();

        if(!resultSet.next()) {
            throw new AccountNotFound("Account not found");
        }

        if(amount <= 0) {
            throw new Exception("Amount must be greater than 0");
        }

        double currentBalance = resultSet.getDouble("balance");
        double currentDailyDeposit = resultSet.getDouble("daily_deposit");
        double newBalance = currentBalance + amount;
        double newDailyDeposit = currentDailyDeposit + amount;

        String updateBalance = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        PreparedStatement p2 = connection.prepareStatement(updateBalance);
        p2.setDouble(1, newBalance);
        p2.setString(2, accountNum);
        p2.executeUpdate();

        String updateDailyDeposit = "UPDATE accounts SET daily_deposit = ? WHERE account_number = ?";
        PreparedStatement p3 = connection.prepareStatement(updateDailyDeposit);
        p3.setDouble(1, newDailyDeposit);
        p3.setString(2, accountNum);
        p3.executeUpdate();

        System.out.println("Deposited successfully: " + amount);
    }

    @Override
    public void transferMoney(String fromAccount, String toAccount, double amount) throws Exception {
        Connection connection = DbConnection.StartConnection();

        String getFromAccount = "SELECT balance, daily_transferred FROM accounts WHERE account_number = ?";
        PreparedStatement p1 = connection.prepareStatement(getFromAccount);
        p1.setString(1, fromAccount);
        ResultSet rs1 = p1.executeQuery();

        if(!rs1.next()) {
            throw new AccountNotFound("Source account not found");
        }

        double currentBalance = rs1.getDouble("balance");
        double currentTransferred = rs1.getDouble("daily_transferred");

        if(currentBalance < amount) {
            throw new Exception("Insufficient balance");
        }

        if(currentTransferred + amount > 50000) {
            throw new Exception("Daily transfer limit of 50000 exceeded");
        }

        String getToAccount = "SELECT account_number FROM accounts WHERE account_number = ?";
        PreparedStatement p2 = connection.prepareStatement(getToAccount);
        p2.setString(1, toAccount);
        ResultSet rs2 = p2.executeQuery();

        if(!rs2.next()) {
            throw new AccountNotFound("Destination account not found");
        }

        double newBalance = currentBalance - amount;
        double newTransferred = currentTransferred + amount;

        String updateFromBalance = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        PreparedStatement p3 = connection.prepareStatement(updateFromBalance);
        p3.setDouble(1, newBalance);
        p3.setString(2, fromAccount);
        p3.executeUpdate();

        String updateFromTransferred = "UPDATE accounts SET daily_transferred = ? WHERE account_number = ?";
        PreparedStatement p4 = connection.prepareStatement(updateFromTransferred);
        p4.setDouble(1, newTransferred);
        p4.setString(2, fromAccount);
        p4.executeUpdate();

        String updateToBalance = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        PreparedStatement p5 = connection.prepareStatement(updateToBalance);
        p5.setDouble(1, amount);
        p5.setString(2, toAccount);
        p5.executeUpdate();

        System.out.println("Transferred successfully: " + amount + " from " + fromAccount + " to " + toAccount);
    }

    @Override
    public void changePin(String accountNum, String oldPin, String newPin) throws Exception {
        JBcrypt bcrypt = new JBcrypt();
        Connection connection = DbConnection.StartConnection();

        String getPin = "SELECT pin FROM accounts WHERE account_number = ?";
        PreparedStatement p1 = connection.prepareStatement(getPin);
        p1.setString(1, accountNum);
        ResultSet resultSet = p1.executeQuery();

        if(!resultSet.next()) {
            throw new AccountNotFound("Account not found");
        }

        String storedPin = resultSet.getString("pin");
        boolean isValid = bcrypt.comparePassword(oldPin, storedPin);

        if(!isValid) {
            throw new Exception("Incorrect current PIN");
        }

        String newHashedPin = bcrypt.hashPassword(newPin);
        String updatePin = "UPDATE accounts SET pin = ? WHERE account_number = ?";
        PreparedStatement p2 = connection.prepareStatement(updatePin);
        p2.setString(1, newHashedPin);
        p2.setString(2, accountNum);
        p2.executeUpdate();

        System.out.println("PIN changed successfully");
    }
}

