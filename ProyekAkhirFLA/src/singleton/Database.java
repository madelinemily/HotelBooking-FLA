package singleton;

import java.util.ArrayList;
import java.util.List;

import model.Room;

public class Database {
	private static Database dbInstance = new Database();
	private ArrayList<Room> listRoom = new ArrayList<>();
	
	public Database() {
		// TODO Auto-generated constructor stub
		listRoom.add(new Room("Deluxe", "Deluxe Room", "King", 2, 500.0, "Credit Card"));
		listRoom.add(new Room("Suite", "Suite Room", "Queen", 4, 1000.0, "Digital Wallet"));
		listRoom.add(new Room("Standard", "Standard Room", "Single", 1, 300.0, "Cash"));
	}

//	public ArrayList<Room> getListBooking() {
//		return listBooking;
//	}
//
//	public void addListBooking(Room room) {
//		this.listBooking.add(room);
//	}

	public static Database getDatabaseInstances() {
		if(dbInstance == null) dbInstance = new Database();
		return dbInstance;
	}

	public ArrayList<Room> getListRoom() {
		return listRoom;
	}

	public void setListRoom(ArrayList<Room> listRoom) {
		this.listRoom = listRoom;
	}
	
	public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for(Room room : listRoom) {
           if(room.isAvailable()) {
              availableRooms.add(room);
           }
        }
        return availableRooms;
    }

    public void showAvailableRooms() {
        List<Room> availableRooms = getAvailableRooms();
        if(availableRooms.isEmpty()) {
           System.out.println("No available rooms");
        } 
        else{
            System.out.println("Available rooms:");
            for(Room room : availableRooms) {
               System.out.println(room);
            }
        }
    }
}
