import java.util.Scanner;

class ATM {
    private double balance;
    private int PIN;

    public ATM(double initialBalance, int PIN) {
        this.balance = initialBalance;
        this.PIN = PIN;
    }

    public boolean validatePin(int inputPin) {
        return this.PIN == inputPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) { // Fixed typo in method name
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit Successful! New balance: $" + balance);
        } else {
            System.out.println("Invalid amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful! New balance: $" + balance);
        } else {
            System.out.println("Invalid amount or insufficient balance!");
        }
    }
}

// Moved ATMInterface outside ATM class
public class ATMInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM userAccount = new ATM(5000.00, 1234);

        System.out.print("Enter your PIN: ");
        int enteredPIN = scanner.nextInt();

        if (!userAccount.validatePin(enteredPIN)) {
            System.out.println("Incorrect PIN! Exiting...");
            return;
        }

        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Choose an Option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: $" + userAccount.getBalance());
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    userAccount.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    userAccount.withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.println("Exiting... Thank you for using our ATM!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
