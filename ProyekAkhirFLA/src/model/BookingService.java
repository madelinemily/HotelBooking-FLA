package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import payment.Cash;
import payment.CreditCard;
import payment.DigitalWallet;
import payment.Payment;
import payment.PaymentProxy;

public class BookingService {
	Scanner scan = new Scanner(System.in);

	private List<BookingEntry> bookings = new ArrayList<>();

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
        user.addBooking(booking);
        room.bookRoom();
        room.setAvailable(false);
        System.out.println("Booking successful for user: " + user.getName() + " in room: " + room.getType());

        selectPaymentMethod(user, room.getPrice(), booking);
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
                payment = new PaymentProxy(new CreditCard(amount), user.getRole());
                break;
            case 3:
                payment = new PaymentProxy(new DigitalWallet(amount), user.getRole());
                break;
            default:
                System.out.println("Invalid choice and payment failed");
                return;
        }

        if(payment.processPayment(amount)) {
           System.out.println("Payment successful for user: " + user.getName() + " with amount: " + amount);
           booking.setPaymentStatus("Paid");
           booking.getRoom().lockRoom();
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
    
    public void checkout(User user) {
        List<BookingEntry> userBookings = user.getBookings();
        if(userBookings.isEmpty()) {
           System.out.println("No bookings found for user.");
           return;
        }

        System.out.println("Your bookings:");
        for(int i = 0; i < userBookings.size(); i++) {
           System.out.println((i + 1) + ". " + userBookings.get(i));
        }

        System.out.print("Select a booking to checkout: ");
        int bookingIndex = scan.nextInt() - 1; 
        scan.nextLine();

        if(bookingIndex < 0 || bookingIndex >= userBookings.size()) {
           System.out.println("Invalid booking selection.");
           return;
        }

        BookingEntry booking = userBookings.get(bookingIndex);
        LocalDate currentDate = LocalDate.now();
        if(currentDate.isAfter(booking.getCheckOutDate())) {
           long overdueDays = ChronoUnit.DAYS.between(booking.getCheckOutDate(), currentDate);
           double penalty = overdueDays * 300.0; 
           System.out.println("You are late by " + overdueDays + " days. Penalty: Rp." + penalty);
        } 
        else{
           System.out.println("Checked out successfully.");
        }

        booking.getRoom().setAvailable(true);
        user.removeBooking(booking);
        bookings.remove(booking);
    }

}
