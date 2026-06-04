package menu;

import java.util.Scanner;

import model.AccountModel;
import services.AccountService;
import services.AccountService;

public class Processes {
    private static AccountService account = AccountService.getInstance();

    public static void handleMenuChoice(int choice, String accountNumber) throws Exception {
        switch (choice) {
            case 1 -> checkBalance(accountNumber);
            case 2 -> depositMoney(accountNumber);
            case 3 -> withdrawMoney(accountNumber);
            case 4 -> transferMoney(accountNumber);
            case 5 -> changePin(accountNumber);
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

    public static void checkBalance(String accountNum) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Press enter to view your balance");
        input.nextLine();

        try {
            account.checkBalance(accountNum);
        } catch (Exception error) {
            System.out.println("The error: " + error.getMessage());
        }
    }

    public static void depositMoney(String accountNum) throws Exception {
        boolean isDeposit = false;
        do {
            Scanner input = new Scanner(System.in);

            System.out.print("Enter the amount you want to deposit: ");
            int depositAmount = input.nextInt();
            input.nextLine();

            try {
                account.depositMoney(accountNum, depositAmount);
                isDeposit = true;
            } catch (Exception error) {
                System.out.println("The error: " + error.getMessage());
            }
        } while (!isDeposit);
    }

    public static void transferMoney(String accountNum) throws Exception {
        boolean isTransfer = false;
        do {
            Scanner input = new Scanner(System.in);

            System.out.print("Enter destination account number: ");
            String toAccount = input.nextLine();

            System.out.print("Enter the amount you want to transfer: ");
            int transferAmount = input.nextInt();
            input.nextLine();

            try {
                account.transferMoney(accountNum, toAccount, transferAmount);
                isTransfer = true;
            } catch (Exception error) {
                System.out.println("The error: " + error.getMessage());
            }
        } while (!isTransfer);
    }

    public static void changePin(String accountNum) throws Exception {
//        boolean isPinChanged = false;

        try {
            Scanner input = new Scanner(System.in);

            System.out.print("Enter your current PIN: ");
            String oldPin = input.nextLine();

            System.out.print("Enter your new PIN: ");
            String newPin = input.nextLine();

            System.out.print("Confirm your new PIN: ");
            String confirmPin = input.nextLine();

            if (!newPin.equals(confirmPin)) {
                System.out.println("Error: New PIN and confirmation do not match");
            }

            account.changePin(accountNum, oldPin, newPin);
        } catch (Exception error){
            System.out.println("Error: " + error.getMessage());
        }
    }

}
