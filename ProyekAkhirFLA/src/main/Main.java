package main;

import java.util.Scanner;

import model.BookingService;
import model.Room;
import model.User;
import singleton.Database;

public class Main {
	Database database = Database.getDatabaseInstances();
	Scanner sc = new Scanner(System.in);
	public void MenuAfterRegister(User user) {
		while(true) {
			System.out.println("Hotel Booking System");
			System.out.println("1. Book Available Room");
			System.out.println("2. See History Booking");
			System.out.println("3. Back to register page");
			System.out.print(">> ");
			int choice = sc.nextInt();sc.nextLine();
			switch (choice) {
			case 1:
				database.showAvailableRooms();
				BookingService bookingSytem = new BookingService();
				System.out.print("Enter your number hotel that you want to book: ");
				int option = sc.nextInt()-1;sc.nextLine();
				Room roomToBook = database.getAvailableRooms().get(option);
				bookingSytem.bookRoom(user, roomToBook);
				break;
			case 2:
				System.out.println("List Booking: ");
				user.showBookings();
				break;
			case 3:
				return;
			}
		}
		
	}
	public Main() {
		// TODO Auto-generated constructor stub
        
        while(true) {
        	System.out.println("Hotel Booking System");
        	System.out.println("1. Register");
        	System.out.println("2. Exit");
        	System.out.print(">> ");
        	int choice = sc.nextInt();sc.nextLine();
        	switch (choice) {
			case 1:
				System.out.println("Welcome to Register");
				System.out.print("Enter your name to register: ");
				String name = sc.nextLine();
				System.out.print("Are you sure to register with this "+name+" Y/N ?: ");
				String confirmation = sc.nextLine();
				if(confirmation.equals("Y")) {
					User newUser = new User(name);
					newUser.register();
					MenuAfterRegister(newUser);
				}else {
					System.out.println("Back to register page");
				}
				break;
			case 2:
				System.out.println("Thank you for using our booking system");
				return;
			}
        }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
