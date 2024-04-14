import java.util.*;

public class ComprehensiveATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("Welcome to the Comprehensive ATM System!");
            System.out.println("1. Login");
            System.out.println("2. Create an Account");
            System.out.println("3. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    bank.login(scanner);
                    break;
                case 2:
                    bank.createAccount(scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using the Comprehensive ATM System.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

class Bank {
    static Map<Integer, Account> accounts = new HashMap<>();
    static Random random = new Random();

    public void login(Scanner scanner) {
        System.out.print("Enter your account number: ");
        int accountNumber = scanner.nextInt();

        Account account = accounts.get(accountNumber);
        if (account != null && !account.isLocked()) {
            System.out.print("Enter your password: ");
            String password = scanner.next();
            if (account.authenticate(password)) {
                System.out.println("Login successful!");
                account.unlock(); // Reset account lock status
                account.showOptions(scanner);
            } else {
                System.out.println("Invalid password.");
                account.incrementLoginAttempts();
                if (account.getLoginAttempts() >= 3) {
                    System.out.println("Your account has been locked. Please contact customer support.");
                    account.lock(); // Lock the account after 3 failed login attempts
                }
            }
        } else {
            System.out.println("Invalid account number or account locked.");
        }
    }

    public void createAccount(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter a password: ");
        String password = scanner.next();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        System.out.print("Enter currency (USD, EUR, INR): ");
        String currency = scanner.next().toUpperCase();
        
        if (!currency.equals("USD") && !currency.equals("EUR") && !currency.equals("INR")) {
            System.out.println("Invalid currency. Only USD, EUR, and INR are supported.");
            return;
        }
        
        int accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, name, password, balance, currency);
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account created successfully. Your account number is: " + account.getAccountNumber());
    }
    
    private int generateAccountNumber() {
        // Generate a random 6-digit account number
        return 100000 + random.nextInt(900000);
    }
}

class Account {
    private int accountNumber;
    private String name;
    private String password;
    private double balance;
    private String currency;
    private boolean locked;
    private int loginAttempts;
    private ArrayList<String> transactionHistory;

    public Account(int accountNumber, String name, String password, double balance, String currency) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.currency = currency;
        this.transactionHistory = new ArrayList<>();
        this.locked = false;
        this.loginAttempts = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public boolean authenticate(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    public void showOptions(Scanner scanner) {
        while (true) {
            System.out.println("\nAccount Options:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Change Password");
            System.out.println("5. Transfer Money");
            System.out.println("6. View Transaction History");
            System.out.println("7. Logout");
            System.out.print("Please select an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit(scanner);
                    break;
                case 3:
                    withdraw(scanner);
                    break;
                case 4:
                    changePassword(scanner);
                    break;
                case 5:
                    transfer(scanner);
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                case 7:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void checkBalance() {
        System.out.println("Current balance: " + formatCurrency(balance));
    }

    private void deposit(Scanner scanner) {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid amount. Deposit amount must be greater than zero.");
            return;
        }
        balance += amount;
        transactionHistory.add("Deposited " + formatCurrency(amount));
        System.out.println("Deposit successful. Current balance: " + formatCurrency(balance));
    }

    private void withdraw(Scanner scanner) {
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid amount. Withdrawal amount must be greater than zero.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            balance -= amount;
            transactionHistory.add("Withdrawn " + formatCurrency(amount));
            System.out.println("Withdrawal successful. Current balance: " + formatCurrency(balance));
        }
    }

    private void changePassword(Scanner scanner) {
        System.out.print("Enter current password: ");
        String currentPassword = scanner.next();
        if (!authenticate(currentPassword)) {
            System.out.println("Incorrect password.");
            return;
        }
        System.out.print("Enter new password: ");
        String newPassword = scanner.next();
        password = newPassword;
        System.out.println("Password changed successfully.");
    }

    private void transfer(Scanner scanner) {
        System.out.print("Enter recipient's account number: ");
        int recipientAccountNumber = scanner.nextInt();
        Account recipient = Bank.accounts.get(recipientAccountNumber);
        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }
        System.out.print("Enter transfer amount: ");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid amount. Transfer amount must be greater than zero.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            balance -= amount;
            recipient.balance += amount;
            transactionHistory.add("Transferred " + formatCurrency(amount) + " to account " + recipientAccountNumber);
            System.out.println("Transfer successful. Current balance: " + formatCurrency(balance));
        }
    }

    private void viewTransactionHistory() {
        System.out.println("Transaction history for account number: " + accountNumber);
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
        loginAttempts = 0; // Reset login attempts after unlocking
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void incrementLoginAttempts() {
        loginAttempts++;
    }

    private String formatCurrency(double amount) {
        return currency + " " + String.format("%.2f", amount);
    }
}
