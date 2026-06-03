package menu;
import model.AccountModel;
import services.AccountService;

import java.util.Scanner;

public class Menu {
    AccountService accountUser =  AccountService.getInstance();
    Scanner scanner = new Scanner(System.in);

        public void startMenu() throws Exception {
            String accountNumber;
            do {
                accountNumber = loginMenu(scanner);
            } while (accountNumber == null);

            int choice = printMenu();
            Processes.handleMenuChoice(choice, accountNumber);

        }


    private String loginMenu(Scanner scanner) throws Exception {
        try {
            System.out.print("Enter Account Number: ");
            String accountNum = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            AccountModel account = accountUser.loginAccount(accountNum, pin);
            String accountNo = account.getAccountNumber();
            System.out.println("hello, " + accountNum);
            return accountNo;

        } catch (Exception e){
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    public static int printMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("  [1] Check Balance");
        System.out.println("  [2] Deposit Money");
        System.out.println("  [3] Withdraw Money");
        System.out.println("  [4] Transfer Money");
        System.out.println("  [5] Change PIN");
        System.out.println("  [6] Transaction History");
        System.out.println("  [7] Exit");
        System.out.print("Enter your choice: ");

        int choice = input.nextInt();

        return choice;
    }
}


