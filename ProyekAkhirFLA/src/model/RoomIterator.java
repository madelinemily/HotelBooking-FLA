package model;

import java.util.Iterator;
import java.util.List;

public class RoomIterator implements Iterator<Room>{

	private List<Room> rooms;
    private int position;

    public RoomIterator(List<Room> rooms) {
        this.rooms = rooms;
        this.position = 0;
    }

    @Override
    public boolean hasNext(){
        return position < rooms.size();
    }

    @Override
    public Room next(){
        if(!hasNext()) {
           return null;
        }

        return rooms.get(position++);
    }
}
