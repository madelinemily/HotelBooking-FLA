package singleton;

import java.util.ArrayList;

import model.Room;

public class Database {
	private static Database dbInstance = new Database();
	private ArrayList<Room> listBooking = new ArrayList<>();
	public Database() {
		// TODO Auto-generated constructor stub
		listBooking = new ArrayList<>();
	}

	public ArrayList<Room> getListBooking() {
		return listBooking;
	}

	public void addListBooking(Room room) {
		this.listBooking.add(room);
	}

	public static Database getDatabaseInstances() {
		if(dbInstance == null) dbInstance = new Database();
		return dbInstance;
	}
}
