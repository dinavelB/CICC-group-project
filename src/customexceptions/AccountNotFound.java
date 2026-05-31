package customexceptions;

public class AccountNotFound extends Exception{
    public AccountNotFound(String m){
        super(m);
    }
}
