package singleton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Room;
import model.RoomIterator;
import state.AvailableState;

public class DatabaseProxy implements IDatabase {
    private Database realDatabase;
    private List<Room> cachedAvailableRooms;

    public DatabaseProxy() {
        this.realDatabase = Database.getDatabaseInstances(); 
        this.cachedAvailableRooms = new ArrayList<>();
    }

    @Override
    public List<Room> getAvailableRooms() {
    	cachedAvailableRooms.clear();
        if (cachedAvailableRooms.isEmpty()) {
            System.out.println("Fetching data from rooms.txt...");
            loadRoomsFromFile();
        } else {
            System.out.println("Fetching data from Cache...");
        }
        return cachedAvailableRooms;
    }

    private void loadRoomsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Available: true")) {
                    Room room = parseRoomData(line);
                    if (room != null) {
                        cachedAvailableRooms.add(room);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading rooms.txt file: " + e.getMessage());
        }
    }

    private Room parseRoomData(String line) {
        try {
            String[] roomDetails = line.split(", ");
            if (roomDetails.length != 8) {
                System.err.println("Invalid room data format: " + line);
                return null;
            }
            String roomType = roomDetails[0].split(": ")[1];
            String roomName = roomDetails[1].split(": ")[1];
            String bedType = roomDetails[2].split(": ")[1];
            int maxOccupancy = Integer.parseInt(roomDetails[3].split(": ")[1]);
            double price = Double.parseDouble(roomDetails[4].split(": ")[1]);
            String paymentType = roomDetails[5].split(": ")[1];
            boolean available = Boolean.parseBoolean(roomDetails[6].split(": ")[1]);
            String state = roomDetails[7].split(": ")[1];
            
            Room room = new Room(roomType, roomName, bedType, maxOccupancy, price, paymentType);
            if(state.equals("Room is available for booking")) {
            	room.setState(new AvailableState());
            }
            room.setAvailable(available);

            return room;
        } catch (Exception e) {
            System.err.println("Error parsing room data: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void showAvailableRooms() {
        List<Room> availableRooms = getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms");
        } else {
            System.out.println("Available rooms (from rooms.txt):");
            RoomIterator roomIterator = new RoomIterator(availableRooms);
            while (roomIterator.hasNext()) {
                Room room = roomIterator.next();
                System.out.println(room); // Display the room details
            }
        }
    }

    public void clearCache() {
        cachedAvailableRooms.clear();
        System.out.println("Cache cleared.");
    }
}
