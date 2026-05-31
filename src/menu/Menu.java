package menu;
import services.AccountService;

import java.util.Scanner;

public class Menu {
    AccountService AccountService = new AccountService();
    Scanner scanner = new Scanner(System.in);

        public void startMenu() throws Exception {
            boolean isCorrectCreds = false;
            do {
                isCorrectCreds = loginMenu(scanner);
            } while (!isCorrectCreds);

            printMenu();

        }

        private void printMenu() {
            System.out.println("  [1] Check Balance");
            System.out.println("  [2] Deposit Money");
            System.out.println("  [3] Withdraw Money");
            System.out.println("  [4] Transfer Money");
            System.out.println("  [5] Change PIN");
            System.out.println("  [6] Transaction History");
            System.out.println("  [7] Exit");
            System.out.print("Enter your choice: ");
        }

        private boolean loginMenu(Scanner scanner) throws Exception {
            try {
            System.out.print("Enter Account Number: ");
            String accountNum = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

           AccountService.loginAccount(accountNum, pin);
           System.out.println("hello, " + accountNum);
           return true;

            } catch (Exception e){
                System.out.println("Login failed: " + e.getMessage());
                return false;
            }
        }
}


