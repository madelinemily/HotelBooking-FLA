package singleton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Room;
import state.AvailableState;

public class Database implements IDatabase {
    private static Database dbInstance = null;
    private ArrayList<Room> listRoom = new ArrayList<>();

    public Database() {
        loadRoomsFromFile("rooms.txt");
    }

    public static Database getDatabaseInstances() {
        if (dbInstance == null) {
            dbInstance = new Database();
        }
        return dbInstance;
    }

    @Override
    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : listRoom) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    @Override
    public void showAvailableRooms() {
        List<Room> availableRooms = getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms");
        } else {
            System.out.println("Available rooms:");
            for (Room room : availableRooms) {
                System.out.println(room);
            }
        }
    }

    private void loadRoomsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Available: true")) {
                    Room room = parseRoomData(line);
                    if (room != null) {
                        listRoom.add(room);  
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
}
