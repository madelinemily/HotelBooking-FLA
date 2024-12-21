package main;

import model.BookingService;
import model.Room;
import model.User;
import singleton.Database;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
		Database database = Database.getDatabaseInstances();
        User user1 = new User("A");
        User user2 = new User("B");

        database.showAvailableRooms();

        user1.register();
        user2.register();

        BookingService bookingSystem = new BookingService();

        Room roomToBook = database.getAvailableRooms().get(0); 
        bookingSystem.bookRoom(user1, roomToBook);

        user1.showBookings();
        user2.showBookings();

        database.showAvailableRooms();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
