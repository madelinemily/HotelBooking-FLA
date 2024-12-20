package state;

import model.Room;

public class AvailableState implements State{

	@Override
	public void book(Room room) {
		// TODO Auto-generated method stub
		room.setState(new BookedState());
		System.out.println("Booking successful!");
	}

	@Override
	public void unlock(Room room) {
		// TODO Auto-generated method stub
		System.out.println("Room already unlocked");
	}

	@Override
	public void lock(Room room) {
		// TODO Auto-generated method stub
		System.out.println("Room can't be locked before you doing the payment...");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Room is available for booking";
	}

}
