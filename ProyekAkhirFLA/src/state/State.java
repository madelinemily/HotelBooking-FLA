package state;

import model.Room;

public interface State {
	void book(Room room); 
    void unlock(Room room); 
    void lock(Room room); 
}

