package state;

import model.Room;

public class BookedState implements State{

	@Override
	public void book(Room room) {
		// TODO Auto-generated method stub
		System.out.println("Room is already booked. Can't book again");
	}

	@Override
	public void unlock(Room room) {
		// TODO Auto-generated method stub
		room.setState(new AvailableState());
		System.out.println("Room is already booked. Unlocking...");
	}

	@Override
	public void lock(Room room) {
		// TODO Auto-generated method stub
		room.setState(new LockedState());
		System.out.println("Locking the room...");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "This room is booked";
	}

}
