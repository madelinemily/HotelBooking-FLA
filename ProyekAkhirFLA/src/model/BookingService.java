package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import adapter.CreditCardToCashAdapter;
import adapter.DigitalWalletToCashAdapter;
import factory.RoomFactory;
import payment.Cash;
import payment.CreditCard;
import payment.DigitalWallet;
import payment.Payment;
import payment.PaymentProxy;

public class BookingService {
	Scanner scan = new Scanner(System.in);

	private List<BookingEntry> bookings = new ArrayList<>();
	private List<BookingEntry> historyBookings = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private static List<Room> rooms = new ArrayList<>();

    public void bookRoom(User user, Room room) {
        if(user == null || room == null) {
           System.out.println("Booking failed. User or room information is missing...");
           return;
        }

        if(!room.isAvailable()) {
           System.out.println("Booking failed. Room is not available");
           return;
        }
        
        System.out.println("Do you want to add extra services? [Yes/No]");
        System.out.print(">> ");
        String extraChoice = scan.nextLine().toLowerCase();
        while(extraChoice.equals("yes")) {
           System.out.println("Available add-ons:");
           System.out.println("1. Extra Bed - Rp 50.000,00");
           System.out.println("2. Breakfast Service - Rp 30.000,00");
           System.out.println("3. Airport Pickup - Rp 70.000,00");
           System.out.println("0. Done");
           System.out.print(">> ");
           int addOnChoice = scan.nextInt();
           scan.nextLine();
           switch(addOnChoice) {
               case 1:
                   room.addAddOn("Extra Bed", 50.0);
                   break;
               case 2:
                   room.addAddOn("Breakfast Service", 30.0);
                   break;
               case 3:
                   room.addAddOn("Airport Pickup", 70.0);
                   break;
               case 0:
                   extraChoice = "no";
                   break;
               default:
                   System.out.println("Invalid choice.");
            }
        }
        
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkInDate = LocalDate.parse(scan.nextLine());
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOutDate = LocalDate.parse(scan.nextLine());

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
            System.out.println("Invalid dates. Booking failed.");
            return;
        }

        BookingEntry booking = new BookingEntry(user, room, "Pending", checkInDate, checkOutDate);
        bookings.add(booking);
//        user.addBooking(booking);
        room.bookRoom();
        room.setAvailable(false);
        updateRoomStatus(room);
//        saveBookingHistory();
        saveCurrentBooking(booking);

        System.out.println("Booking successful for user: " + user.getName() + " in room: " + room.getType());

        selectPaymentMethod(user, room.getPrice(), booking);
    }
    
    private void saveCurrentBooking(BookingEntry booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("current_booking.txt", true))) {
            writer.write(booking.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving current booking: " + e.getMessage());
        }
    }
    
    public void showCurrentBookings(User user) {
        List<BookingEntry> userBookings = loadUserBookings(user.getName(), "current_booking.txt");
        if (userBookings.isEmpty()) {
            System.out.println("No current bookings for user: " + user.getName());
            return;
        }

        System.out.println("Current bookings for " + user.getName() + ":");
        for (BookingEntry booking : userBookings) {
            System.out.println(booking);
        }
    }

    public void showBookingHistory(User user) {
        List<BookingEntry> userHistory = loadUserBookings(user.getName(), "booking_history.txt");
        if (userHistory.isEmpty()) {
            System.out.println("No booking history found for user: " + user.getName());
            return;
        }

        System.out.println("Booking history for " + user.getName() + ":");
        for (BookingEntry booking : userHistory) {
            System.out.println(booking);
        }
    }
    
    public void readRoomsFromFile() {
        rooms.clear(); 
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String type = parts[0].split(": ")[1];
                String name = parts[1].split(": ")[1];
                String bedType = parts[2].split(": ")[1];
                int maxOccupancy = Integer.parseInt(parts[3].split(": ")[1]);
                double price = Double.parseDouble(parts[4].split(": ")[1]);
                String paymentType = parts[5].split(": ")[1];
                boolean available = Boolean.parseBoolean(parts[6].split(": ")[1]);
                
                RoomFactory factory = RoomFactory.createFactory(type);
                if (factory != null) {
                    Room room = factory.createRoom(type, name, bedType, maxOccupancy, new Cash(price), paymentType);
                    rooms.add(room);
                } else {
                    System.out.println("Unknown room type: " + type);
                }
                
            }
        } catch (IOException e) {
            System.out.println("Error reading rooms data: " + e.getMessage());
        }
    }


    private static void updateRoomStatus(Room room) {
        for (Room r : rooms) {
            if (r.getName().equals(room.getName())) {
                r.setAvailable(false); 
                r.bookRoom(); 
                break; 
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room r : rooms) {
                writer.write("RoomType: " + r.getType() + ", RoomName: " + r.getName() +
                             ", BedType: " + r.getBedType() + ", MaxOccupancy: " + r.getMaxOccupancy() +
                             ", Price: " + r.getPrice() + ", PaymentType: " + r.getPaymentType() +
                             ", Available: " + r.isAvailable() + ", State: " + r.getState() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating room status: " + e.getMessage());
        }
    }
    
    private static void updateRoomStatusToLocked(Room room) {
        for (Room r : rooms) {
            if (r.getName().equals(room.getName())) {
                r.setAvailable(false); 
                r.lockRoom(); 
                break; 
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room r : rooms) {
                writer.write("RoomType: " + r.getType() + ", RoomName: " + r.getName() +
                             ", BedType: " + r.getBedType() + ", MaxOccupancy: " + r.getMaxOccupancy() +
                             ", Price: " + r.getPrice() + ", PaymentType: " + r.getPaymentType() +
                             ", Available: " + r.isAvailable() + ", State: " + r.getState() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating room status: " + e.getMessage());
        }
    }
    
    private static void updateRoomStatusToAvailable(Room room) {
        for (Room r : rooms) {
            if (r.getName().equals(room.getName())) {
                r.setAvailable(true); 
                r.unlockRoom();
                break; 
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room r : rooms) {
                writer.write("RoomType: " + r.getType() + ", RoomName: " + r.getName() +
                             ", BedType: " + r.getBedType() + ", MaxOccupancy: " + r.getMaxOccupancy() +
                             ", Price: " + r.getPrice() + ", PaymentType: " + r.getPaymentType() +
                             ", Available: " + r.isAvailable() + ", State: " + r.getState() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating room status: " + e.getMessage());
        }
    }


    public void selectPaymentMethod(User user, double amount, BookingEntry booking) {
        if(!user.getRole().equals("Registrant")) {
           System.out.println("Payment failed. Only registrant can make payments");
           return;
        }

        System.out.println("Select a payment method: ");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Digital Wallet");
        System.out.print(">> ");

        int choice = scan.nextInt();

        Payment payment = null;

        switch(choice) {
            case 1:
                payment = new PaymentProxy(new Cash(amount), user.getRole());
                break;
            case 2:
            	payment = new PaymentProxy(new CreditCardToCashAdapter(new CreditCard(amount)), user.getRole());
                break;
            case 3:
            	payment = new PaymentProxy(new DigitalWalletToCashAdapter(new DigitalWallet(amount)), user.getRole());
                break;
            default:
                System.out.println("Invalid choice and payment failed");
                return;
        }

        if(payment.processPayment(amount)) {
           System.out.println("Payment successful for user: " + user.getName() + " with amount: " + amount);
           booking.setPaymentStatus("Paid");
           booking.getRoom().lockRoom();
           updateRoomStatusToLocked(booking.getRoom());
        } 
        else{
           System.out.println("Payment failed");
           booking.setPaymentStatus("Failed");
        }
    }

    public void showBookings() {
        if(bookings.isEmpty()) {
            System.out.println("No bookings available");
            return;
        }

        System.out.println("Current bookings:");
        for(BookingEntry booking : bookings) {
            System.out.println(booking);
        }
    }
    
    private static void updateRoomStatusWhenCheckout(Room room) {
        for (Room r : rooms) {
            if (r.getName().equals(room.getName())) {
                r.setAvailable(true);
                break; 
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
        	for (Room r : rooms) {
                writer.write("RoomType: " + r.getType() + ", RoomName: " + r.getName() +
                             ", BedType: " + r.getBedType() + ", MaxOccupancy: " + r.getMaxOccupancy() +
                             ", Price: " + r.getPrice() + ", PaymentType: " + r.getPaymentType() +
                             ", Available: " + r.isAvailable() + ", State: " + r.getState() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating room status: " + e.getMessage());
        }
    }
    
    public void checkout(User user) {
        List<BookingEntry> userBookings = loadUserBookings(user.getName(), "current_booking.txt");
        if (userBookings.isEmpty()) {
            System.out.println("No bookings found for user.");
            return;
        }

        System.out.println("Your bookings:");
        for (int i = 0; i < userBookings.size(); i++) {
            System.out.println((i + 1) + ". " + userBookings.get(i));
        }

        System.out.print("Select a booking to checkout: ");
        int bookingIndex = scan.nextInt() - 1;
        scan.nextLine();

        if (bookingIndex < 0 || bookingIndex >= userBookings.size()) {
            System.out.println("Invalid booking selection.");
            return;
        }

        BookingEntry booking = userBookings.get(bookingIndex);
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(booking.getCheckOutDate())) {
            long overdueDays = ChronoUnit.DAYS.between(booking.getCheckOutDate(), currentDate);
            double penalty = overdueDays * 300.0; 
            System.out.println("You are late by " + overdueDays + " days. Penalty: Rp." + penalty);
        } else {
            System.out.println("Checked out successfully.");
        }

        booking.getRoom().setAvailable(true);
        booking.setCheckedOut(true);
        historyBookings.add(booking);
        updateRoomStatusWhenCheckout(booking.getRoom());
        booking.getRoom().unlockRoom();
        updateRoomStatusToAvailable(booking.getRoom());

        moveBookingToHistory(booking);
        removeFromCurrentBooking(booking);
//        removeBookingFromFile(user.getName(), booking);

        System.out.println("Booking has been checked out and room is now available.");
    }
    
    private void moveBookingToHistory(BookingEntry booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("booking_history.txt", true))) {
            writer.write(booking.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking to history: " + e.getMessage());
        }
    }

    private void removeFromCurrentBooking(BookingEntry booking) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("current_booking.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(booking.getUser().getName())) {
                    lines.add(line);
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter("current_booking.txt"));
            for (String l : lines) {
                writer.write(l + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error removing booking from current_booking.txt: " + e.getMessage());
        }
    }

    private List<BookingEntry> loadUserBookings(String username, String filename) {
        List<BookingEntry> bookings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String userNameFromFile = parts[0].split(": ")[1].trim();

                if (userNameFromFile.equals(username)) {
                    String roomType = parts[1].split(": ")[1].trim();
                    String roomName = parts[2].split(": ")[1].trim();
                    String bedType = parts[3].split(": ")[1].trim();

                    int maxOccupancy;
                    try {
                        maxOccupancy = Integer.parseInt(parts[4].split(": ")[1].trim());
                    } catch (NumberFormatException e) {
                        maxOccupancy = 1; 
                    }

                    double price;
                    try {
                        price = Double.parseDouble(parts[5].split(": ")[1].trim());
                    } catch (NumberFormatException e) {
                        price = 0.0;
                    }

                    String paymentType = parts[6].split(": ")[1].trim();
                    boolean available = Boolean.parseBoolean(parts[7].split(": ")[1].trim());
                    String state = parts[8].split(": ")[1].trim();
                    List<String> addOns = Arrays.asList(parts[9].split(": ")[1].replace("[", "").replace("]", "").split(", "));

                    double addOnPrice;
                    try {
                        addOnPrice = Double.parseDouble(parts[10].split(": ")[1].trim());
                    } catch (NumberFormatException e) {
                        addOnPrice = 0.0; 
                    }

                    String paymentStatus = parts[11].split(": ")[1].trim();
                    LocalDate checkInDate = LocalDate.parse(parts[12].split(": ")[1].trim());
                    LocalDate checkOutDate = LocalDate.parse(parts[13].split(": ")[1].trim());
                    boolean isCheckedOut = Boolean.parseBoolean(parts[14].split(": ")[1].trim());

                    RoomFactory factory = RoomFactory.createFactory(roomType);
                    Room room = null;
                    if (factory != null) {
                        room = factory.createRoom(roomType, roomName, bedType, maxOccupancy, new Cash(price), paymentType);
                    } else {
                        System.out.println("Unknown room type: " + roomType);
                    }
                    
                    User user = new User(userNameFromFile);
                    BookingEntry booking = new BookingEntry(user, room, paymentStatus, checkInDate, checkOutDate);
                    booking.setCheckedOut(isCheckedOut);

                    bookings.add(booking);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading booking data: " + e.getMessage());
        }


        return bookings;
    }

//    private void removeBookingFromFile(String username, BookingEntry bookingToRemove) {
//        List<String> allBookings = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader("booking_history.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(", ");
//                String userNameFromFile = parts[0].split(": ")[1].trim();
//                String roomType = parts[1].split(": ")[1].trim();
//                if (userNameFromFile.equals(username) && roomType.equals(bookingToRemove.getRoom().getType())) {
//                    continue; // Skip the booking to remove
//                }
//                allBookings.add(line);
//            }
//        } catch (IOException e) {
//            System.out.println("Error reading booking data: " + e.getMessage());
//        }
//
//        // Rewrite the file with updated bookings
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("booking_history.txt"))) {
//            for (String booking : allBookings) {
//                writer.write(booking);
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            System.out.println("Error saving updated booking data: " + e.getMessage());
//        }
//    }

    
//    private void saveBookingHistory() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("current_booking.txt", true))) {
//            for (BookingEntry booking : bookings) {
//                writer.write(booking.toString());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            System.out.println("Error while saving booking history: " + e.getMessage());
//        }
//    }
//    
//    private String bookingToString(BookingEntry booking) {
//        return booking.getUser().getName() + "," + 
//               booking.getRoom().getType() + "," + 
//               booking.getPaymentStatus() + "," + 
//               booking.getCheckInDate() + "," + 
//               booking.getCheckOutDate() + "," + 
//               booking.isCheckedOut();
//    }
    
    public void loadBookingsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingData = line.split(",");
                String userName = bookingData[0];
                String roomType = bookingData[1];
                String paymentStatus = bookingData[2];
                LocalDate checkInDate = LocalDate.parse(bookingData[3]);
                LocalDate checkOutDate = LocalDate.parse(bookingData[4]);
                boolean isCheckedOut = Boolean.parseBoolean(bookingData[5]);

                User user = findUserByName(userName);
                Room room = findRoomByType(roomType);

                if (user != null && room != null) {
                    BookingEntry booking = new BookingEntry(user, room, paymentStatus, checkInDate, checkOutDate);
                    booking.setCheckedOut(isCheckedOut);
                    bookings.add(booking);
                    user.addBooking(booking);
                }
            }
            System.out.println("Bookings have been loaded from bookings.txt.");
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
    
    private User findUserByName(String userName) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                return user; 
            }
        }
        return null; 
    }
    
    private Room findRoomByType(String roomType) {
        for (Room room : rooms) {
            if (room.getType().equalsIgnoreCase(roomType)) {
                return room; 
            }
        }
        return null;
    }

}
