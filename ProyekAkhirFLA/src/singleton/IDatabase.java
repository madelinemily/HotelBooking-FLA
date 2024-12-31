package singleton;

import java.util.List;
import model.Room;

public interface IDatabase {
    List<Room> getAvailableRooms();
    void showAvailableRooms();
}
