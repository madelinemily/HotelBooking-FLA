package main;

import java.util.Scanner;

import model.BookingService;
import model.Room;
import model.User;
import singleton.Database;
import singleton.DatabaseProxy;

public class Main {
//    Database database = Database.getDatabaseInstances();
	DatabaseProxy database = new DatabaseProxy();
    BookingService bookingSystem = new BookingService();
    Scanner sc = new Scanner(System.in);

    public void MenuAfterRegister(User user) {
    	bookingSystem.readRoomsFromFile();
        while(true) {
            System.out.println("Hotel Booking System");
            System.out.println("1. Book Available Room");
            System.out.println("2. See Current Booking");
            System.out.println("3. Checkout Booking");
            System.out.println("4. Back to register page");
            System.out.println("5. See History Booking");
            System.out.print(">> ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    database.showAvailableRooms();
                    System.out.print("Enter your number hotel that you want to book: ");
                    int option = sc.nextInt() - 1;
                    sc.nextLine();
                    Room roomToBook = database.getAvailableRooms().get(option);
                    bookingSystem.bookRoom(user, roomToBook);
                    break;
                case 2:
                    System.out.println("List Booking: ");
                    bookingSystem.showCurrentBookings(user);
                    break;
                case 3:
                    bookingSystem.checkout(user);
                    break;
                case 4:
                    return;
                case 5:
                	bookingSystem.showBookingHistory(user);
                	break;
            }
        }
    }

    public void MenuBeforeLogin() {
        User.loadUsers();

        while(true) {
            System.out.println("Hotel Booking System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print(">> ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Welcome to Register");
                    System.out.print("Enter your name to register: ");
                    String name = sc.nextLine();
                    System.out.print("Enter your username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter your password: ");
                    String password = sc.nextLine();
                    System.out.print("Are you sure to register with this " + name + " Y/N ?: ");
                    String confirmation = sc.nextLine();
                    if(confirmation.equalsIgnoreCase("Y")) {
                        if(User.register(username, password, name)) {
                           System.out.println("Registration successful!");
                        } 
                        else{
                            System.out.println("Username already taken.");
                        }
                    } 
                    else{
                        System.out.println("Back to register page");
                        System.out.print("Press Enter to Continue");
                        sc.nextLine();
                    }
                    
                    break;
                case 2:
                    System.out.println("Welcome to Login");
                    System.out.print("Enter your username: ");
                    String loginUsername = sc.nextLine();
                    System.out.print("Enter your password: ");
                    String loginPassword = sc.nextLine();
                    User loggedInUser = User.login(loginUsername, loginPassword);
                    if(loggedInUser != null) {
                       System.out.println("Login successful!");
                       MenuAfterRegister(loggedInUser);
                    } 
                    else{
                       System.out.println("Invalid username or password. Please try again.");
                    }
                    
                    break;
                case 3:
                    System.out.println("Thank you for using our booking system");
                    
                    return;
            }
        }
    }

    public static void main(String[] args) {
        new Main().MenuBeforeLogin();
    }
}
