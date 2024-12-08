import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat; 

// Expense class to hold information about each expense
//ENCAPSULATION 
class Expense {
    private String description;
    private double amount;
    private Date date;
    private String category;

    // Constructor
    public Expense(String description, double amount, Date date, String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters for Expense details
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return "Description: " + description + "\nAmount:  PHP" + String.format("%.2f", amount) 
                + "\nDate: " + dateFormat.format(date) + "\nCategory: " + category;
    }
}

// User class to store the user's name, password, and budget
//encapsulation
class User {
    private String name;
    private String password;
    private double budget;

    // Constructor for login
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Constructor for new user creation
    public User(String name) {
        this.name = name;
        this.password = null;  
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// UserDatabase for saving user credentials 
class UserDatabase {
    private static final String USERS_FILE = "users.txt"; 

    // saveUser method
    public static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getName() + "," + user.getPassword() + "," + user.getBudget() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }


// Method to load users from file
public static Map<String, User> loadUsers() {
    Map<String, User> users = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userData = line.split(",");
            String name = userData[0];
            String password = userData[1];

            User user = new User(name);
            user.setPassword(password);
            users.put(name, user);
        }
    } catch (IOException e) {
        System.out.println("Error loading user data: " + e.getMessage());
    }
    return users;
}

    
    // Method to check if username exists
    public static boolean userExists(String username, Map<String, User> users) {
        return users.containsKey(username);
    }

    // Method to validate login
    public static boolean validateLogin(String username, String password, Map<String, User> users) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
}

// ExpenseDatabase for saving and loading expenses for each user
class ExpenseDatabase {
    public static final String EXPENSES_FILE_PREFIX = "expenses_"; // Prefix for user-specific expense files 

    // Save expenses for a specific user to file
    public static void saveExpenses(User user, List<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPENSES_FILE_PREFIX + user.getName() + ".txt"))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + ","
                        + expense.getAmount() + ","
                        + new SimpleDateFormat("MM-dd-yyyy").format(expense.getDate()) + ","
                        + expense.getCategory() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    // Load expenses for a specific user from file
    public static List<Expense> loadExpenses(User user) {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(EXPENSES_FILE_PREFIX + user.getName() + ".txt"); //represents the file where the user's expenses are stored.


        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] expenseData = line.split(",");
                String description = expenseData[0];
                double amount = Double.parseDouble(expenseData[1]);
                Date date = new SimpleDateFormat("MM-dd-yyyy").parse(expenseData[2]);
                String category = expenseData[3];
                expenses.add(new Expense(description, amount, date, category));
            }
        } catch (IOException | java.text.ParseException e) {
            System.out.println("Error loading expenses: " + e.getMessage()); //catching the error
        }
        return expenses;
    }
}

// ExpenseTracker class for managing user expenses
class ExpenseTracker {
    private List<Expense> expenses;
    private User user;
    private List<String> predefinedCategories;

    // Constructor
    public ExpenseTracker(User user) {
        this.user = user;
        this.expenses = ExpenseDatabase.loadExpenses(user); // Load existing expenses when user logs in
        this.predefinedCategories = Arrays.asList("Food", "Transport", "Entertainment", "Utilities", "Health", "Others");
    }

    // Method to add an expense
    public void addExpense(String description, double amount, Date date, String category) {
        Expense expense = new Expense(description, amount, date, category);
        expenses.add(expense);
        ExpenseDatabase.saveExpenses(user, expenses); // Save the updated expenses
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        System.out.println("\nNew Expense Added:");
        System.out.println("Description: " + expense.getDescription());
        System.out.println("Amount: " + String.format("%.2f", expense.getAmount())+" PHP");
        System.out.println("Date: " + dateFormat.format(expense.getDate()));
        System.out.println("Category: " + expense.getCategory());
    }
    

    // Method to view all expenses
    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to show.");
        } else {
            System.out.println("\n------------------------- LIST OF EXPENSES ----------------------------------------------");
            System.out.printf("| %-4s | %-30s | %-10s    | %-10s | %-15s  |\n", "No.", "Description", "Amount", "Date", "Category");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            for (int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);
                System.out.printf("| %-4d | %-30s | PHP%-10.2f | %-10s | %-15s  |\n",
                        i + 1, expense.getDescription(), expense.getAmount(),
                        dateFormat.format(expense.getDate()),
                        expense.getCategory());
            }
        }
    }

// Method to delete an expense
public void deleteExpense(int index) {
    if (index < 0 || index >= expenses.size()) {
        System.out.println("Invalid expense number. Please try again.");
    } else {
        Expense removedExpense = expenses.remove(index);
        ExpenseDatabase.saveExpenses(user, expenses); // Update the file after deletion
        System.out.println("Expense deleted successfully:");
        System.out.println(removedExpense);
    }
}


   // Method to generate a report based on a date range
public void generateReport() {
    SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
    Map<String, Double> monthlyExpenses = new TreeMap<>(); // Stores total expenses for each month
    Map<String, Map<String, Double>> categoryExpenses = new TreeMap<>(); // Stores category-wise expenses per month
    double totalExpense = 0;

    // Step 1: Group expenses by month and category
    for (Expense expense : expenses) {
        // Format the expense's date into "Month Year" (ex., "January 2024")
        String monthYear = monthYearFormat.format(expense.getDate());

        // Add the expense amount to the total for the month
        if (!monthlyExpenses.containsKey(monthYear)) {
            monthlyExpenses.put(monthYear, 0.0);
        }
        monthlyExpenses.put(monthYear, monthlyExpenses.get(monthYear) + expense.getAmount());

        // Add the expense amount to the correct category for the month
        if (!categoryExpenses.containsKey(monthYear)) {
            categoryExpenses.put(monthYear, new HashMap<>());
        }
        Map<String, Double> categories = categoryExpenses.get(monthYear);
        String category = expense.getCategory();
        if (!categories.containsKey(category)) {
            categories.put(category, 0.0);
        }
        categories.put(category, categories.get(category) + expense.getAmount());

        // Add to the total expense
        totalExpense += expense.getAmount();
    }

    System.out.println("\n========= EXPENSE REPORT =========");
    System.out.println();
    System.out.println("Total Expenses: PHP " + String.format("%.2f", totalExpense));
    System.out.println();
    
    // Step 3: Display expenses grouped by month and category
    for (String monthYear : monthlyExpenses.keySet()) {
        double monthTotal = monthlyExpenses.get(monthYear);
        System.out.println("*************************************");
        System.out.println("           " + monthYear + "           ");
        System.out.println("*************************************");
    
        // Display total expense for the month
        System.out.println("Total Expense for the Month: PHP " + String.format("%.2f", monthTotal));
        System.out.println();
    
        // Display categories and amounts for the month
        System.out.println("Category          Amount Spent");
        System.out.println("---------------------------------");
    
        Map<String, Double> categories = categoryExpenses.get(monthYear);
        for (String category : categories.keySet()) {
            double amountSpent = categories.get(category);
            System.out.printf("%-18s PHP %.2f\n", category, amountSpent);
        }
        System.out.println();
        System.out.println();  
    }
}

    // Method to get predefined categories
    public List<String> getPredefinedCategories() {
        return predefinedCategories;
        
    }
}

// Main program to run the ExpenseTracker system
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, User> users = UserDatabase.loadUsers();
        User loggedInUser = null;

        while (true) {
            System.out.println(" ");
            System.out.println("========================================================================");
            System.out.println("\t\t\tEXPENSE TRACKER");
            System.out.println("========================================================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                // Register a new user
                String username = "";
                while (true) {
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                    if (username.length() >= 8) {
                        if (UserDatabase.userExists(username, users)) {
                            System.out.println("Username already exists. Try logging in.");
                            break;  // Exit the loop and go back to the main menu
                        }
                        break;  // Exit the loop if username is valid and not existing
                    } else {
                        System.out.println("Username must be at least 8 characters long. Please try again.");
                    }
                } 

                if (!UserDatabase.userExists(username, users)) {
                    // Proceed with password setup if the username is new
                    String password = "";
                    while (true) {
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();
                        if (password.length() >= 8 && password.length() <= 12) {
                            break;  // Exit the loop if password is valid
                        } else {
                            System.out.println("Password must be between 8 and 12 characters. Please try again.");
                        }
                    }
        
                    User newUser = new User(username);
                    newUser.setPassword(password);

                    // Save user to map 
                    users.put(username, newUser);  // Save to the map
                    UserDatabase.saveUser(newUser); // Save to file
                    System.out.println("\tRegistration successful! You can now log in.");
                    System.out.println("-----------------------------------------------------");
                }
            } else if (choice == 2) {
                // Log in
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                if (users.containsKey(username)) {
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    if (UserDatabase.validateLogin(username, password, users)) {
                        loggedInUser = users.get(username);
                        ExpenseTracker expenseTracker = new ExpenseTracker(loggedInUser); // Load expenses for logged-in user
                        System.out.println("You have successfully logged in.");
                        System.out.println("\n----------------------------------------------");
                        System.out.println("\nWelcome to Expense Tracker");
                        System.out.println("\t\tSpend Less, Stay on Track!");

                        // Display menu for expense tracker
                        while (true) {
                            System.out.println("");
                            System.out.println("==============================================");
                            System.out.println("\t\tMAIN MENU:");
                            System.out.println("==============================================");
                            System.out.println("1. Add Expense");
                            System.out.println("2. View List of Expenses");
                            System.out.println("3. Delete Expense");
                            System.out.println("4. Generate Total Expense");
                            System.out.println("5. Logout");
                            System.out.print("Choose an option: ");
                            int option = scanner.nextInt();
                            scanner.nextLine(); 

                            if (option == 1) {
                                // Add Expense
                                System.out.print("Enter expense description: ");
                                String description = scanner.nextLine();
                                System.out.print("Enter amount: ");
                                double amount = scanner.nextDouble();
                                scanner.nextLine(); 
                                System.out.print("Enter category (Food, Transport, Entertainment, Utilities, Health, Others): ");
                                String category = scanner.nextLine();
                            
                                // Prompt user to input the date
                                System.out.print("Enter the date (mm-dd-yyyy): ");
                                String dateString = scanner.nextLine();
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("MM-dd-yyyy").parse(dateString);
                                } catch (java.text.ParseException e) {
                                    System.out.println("Invalid date format. Please use MM-dd-yyyy.");
                                    continue; // Restart the loop for adding an expense
                                }

                                expenseTracker.addExpense(description, amount, date, category);

                            } else if (option == 2) {
                                // View Expenses
                                expenseTracker.viewExpenses();
                            } else if (option == 3) {
                                // Delete Expense
                                expenseTracker.viewExpenses(); // Show current expenses
                                System.out.print("Enter the number of the expense to delete: ");
                                int expenseIndex = scanner.nextInt() - 1; 
                                scanner.nextLine();
                                expenseTracker.deleteExpense(expenseIndex);
                            } else if (option == 4) {
                                // Generate Report
                                expenseTracker.generateReport();
                            } else if (option == 5) {
                                // Logout and Exit the Program
                                loggedInUser = null;  
                                System.out.println("Logged out successfully.");
                                System.out.println("Goodbye!");
                                System.exit(0);  // Exit the program
                            } else {
                                System.out.println("Invalid option. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("Invalid password. Please try again.");
                    }
                } else {
                    System.out.println("No such user found. Please register first.");
                }
            } else if (choice == 3) {
                // Exit
                System.out.println("Goodbye!");
                break;  // Exit the main loop and the program
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}