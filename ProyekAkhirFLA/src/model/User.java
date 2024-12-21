package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String role;
    private List<BookingEntry> bookings;

    public User(String name) {
        this.name = name;
        this.role = "Guest";
        this.bookings = new ArrayList<>();
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
    
    public void register() {
        if(!role.equals("Registrant")) {
           this.role = "Registrant";
           System.out.println("Registration successful");
        } 
        else{
           System.out.println("You're already register");
        }
    }

	public List<BookingEntry> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingEntry> bookings) {
		this.bookings = bookings;
	}
	
	public void addBooking(BookingEntry booking) {
        bookings.add(booking);
    }

    public void showBookings() {
        if(bookings.isEmpty()) {
           System.out.println(name + " has no bookings");
           return;
        }

        System.out.println(name + "'s bookings:");
        for(BookingEntry booking : bookings) {
           System.out.println(booking);
        }
    }
}

