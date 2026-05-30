package services;
import dao.AccountDao;
import connections.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Account implements AccountDao{

    public boolean loginAccount(int accountNum, int pin) throws Exception {
        Connection connection = DbConnection.StartConnection();
        String foundAccount = "SELECT * FROM accounts WHERE accountNum = ?";

//    create a pStatement with the connection method from dbConnection
        PreparedStatement p = connection.prepareStatement(foundAccount);
//        always setInt since each param or ? is counted default 1 onwards
        p.setInt(1, accountNum);
//        results of the query using the prepared statement
        ResultSet resultSet = p.executeQuery();

        if(resultSet.next()){
            String userPin =  resultSet.getString("pin");
        }
    }
}
