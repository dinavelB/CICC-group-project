package util;
import org.mindrot.jbcrypt.BCrypt;

public class JBcrypt {
    BCrypt bcrypt = new BCrypt();

    public String hashPassword(String password){
        String hashedPassword =  bcrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    public boolean comparePassword (String userInputPass, String dbPassword) throws Exception{
        boolean isCorrect = bcrypt.checkpw(userInputPass, dbPassword);
        return isCorrect;
    }

    public String hashMyPassword(String samplePassword){
        String hashedPassword =  bcrypt.hashpw(samplePassword, BCrypt.gensalt());
        return  hashedPassword;
    }

}

class Run {

    /*
    public static  void main(String[] args){
        JBcrypt test =  new JBcrypt();
        String password = test.hashMyPassword("020606");
        System.out.println("hashed password is: " + password);

    }

     */
}
