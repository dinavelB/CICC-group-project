package menu;

import java.util.Scanner;

import model.AccountModel;
import services.AccountService;
import services.AccountService;

public class Processes {
    private static AccountService account = AccountService.getInstance();

    public static void handleMenuChoice(int choice, String accountNumber) throws Exception {
        switch (choice) {
            case 1 -> account.checkBalance();
            case 2 -> account.depositMoney();
            case 3 -> withdrawMoney(accountNumber);
            case 4 -> account.transferMoney();
            // case 5 -> changePin(accountNumber);
            //case 5 -> exitApplication();
            default -> System.out.println("Invalid choice. Please try again.");
        };// Private constructor prevents 'new AccountService()' from outside
    }

    public static void withdrawMoney (String accountNum) throws Exception{
        boolean isWithdraw = false;
        do {
            Scanner input = new Scanner(System.in);

            System.out.print("Enter the amount you want to withdraw: ");
            int withdrawAmount = input.nextInt();
            input.nextLine();

            try{
                account.withdraw(accountNum, withdrawAmount);
                isWithdraw = true;
            } catch (Exception error) {
                System.out.println("The error: " + error.getMessage());
            }
        } while (!isWithdraw);
    }

}
