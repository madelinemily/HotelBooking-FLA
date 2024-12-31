package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String role;
    private String username;
    private String password;
    private List<BookingEntry> bookings;

    private static List<User> users = new ArrayList<>(); 

    public User(String name) {
        this.name = name;
        this.role = "Registrant";
        this.bookings = new ArrayList<>();
    }

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void addBooking(BookingEntry booking) {
        bookings.add(booking);
    }

    public void removeBooking(BookingEntry booking) {
        bookings.remove(booking);
    }

//    public static boolean register(String username, String password, String name) {
//        for (User user : users) {
//            if (user.getUsername().equals(username)) {
//                return false; 
//            }
//        }
//        
//        User newUser = new User(name);
//        newUser.setUsername(username);
//        newUser.setPassword(password);
//        users.add(newUser);
//        saveUsers(); 
//        return true;
//    }
//
//    public static User login(String username, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                return user; 
//            }
//        }
//        return null; 
//    }

    public void showBookings() {
        try (BufferedReader reader = new BufferedReader(new FileReader("booking_history.txt"))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("user: " + this.name)) { 
                    found = true;
                    System.out.println(line); 
                }
            }
            if (!found) {
                System.out.println("No bookings found for " + this.name);
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings file: " + e.getMessage());
        }
    }

    public static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String username = userData[0];
                String password = userData[1];
                String name = userData[2];
                
                User user = new User(name);
                user.setUsername(username);
                user.setPassword(password);
                
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }

    public static boolean register(String username, String password, String name) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; 
            }
        }

        User newUser = new User(name);
        newUser.setUsername(username);  
        newUser.setPassword(password); 
        users.add(newUser);
        saveUsers(); 
        return true;
    }

    public static User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; 
            }
        }
        return null;  
    }


    public static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users data.");
        }
    }
}
