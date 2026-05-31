import config.DbConnection;
import menu.Menu;

class Main {

    public static void main (String[] args) throws  Exception{
        Menu menu = new Menu();
        DbConnection.StartConnection();

        menu.startMenu();
    }
}