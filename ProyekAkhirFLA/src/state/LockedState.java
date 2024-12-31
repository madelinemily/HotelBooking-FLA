package state;

import model.Room;

public class LockedState implements State{

	@Override
	public void book(Room room) {
		// TODO Auto-generated method stub
		System.out.println("You already booked this room");
	}

	@Override
	public void unlock(Room room) {
		// TODO Auto-generated method stub
		System.out.println("Unlocking...");
        room.setState(new AvailableState());
	}

	@Override
	public void lock(Room room) {
		// TODO Auto-generated method stub
		System.out.println("Room is already locked");
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "This room is now locked";
	}

}
