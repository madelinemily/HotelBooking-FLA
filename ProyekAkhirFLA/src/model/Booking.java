package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import payment.Cash;
import payment.CreditCard;
import payment.DigitalWallet;
import payment.Payment;
import payment.PaymentProxy;

public class Booking {
	Scanner scan = new Scanner(System.in);

	private List<String> bookings = new ArrayList<>();

    public void bookRoom(User user, Room room) {
        if(user == null || room == null) {
           System.out.println("Booking failed. User or room information is missing...");
           return;
        }

        if(!room.isAvailable()) {
           System.out.println("Booking failed. Room is not available");
           return;
        }

        bookings.add("Booking: User : " + user.getName() + ", Room : " + room.getType());
        room.setAvailable(false);
        System.out.println("Booking successful for user: " + user.getName() + " in room: " + room.getType());

        selectPaymentMethod(user, room.getPrice());
    }

    public void selectPaymentMethod(User user, double amount) {
        if(!user.getRole().equals("Registrant")) {
           System.out.println("Payment failed. Only registrant can make payments");
           return;
        }

        System.out.println("Select a payment method: ");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Digital Wallet");

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

        payment.processPayment(amount);
        System.out.println("Payment successful for user: " + user.getName() + " with amount: " + amount);
    }

    public void showBookings() {
        if(bookings.isEmpty()) {
            System.out.println("No bookings available");
            return;
        }

        System.out.println("Current bookings:");
        for(String booking : bookings) {
            System.out.println(booking);
        }
    }

}
