package model;

import java.time.LocalDate;
import java.util.List;

public class BookingEntry {
    private User user;
    private Room room;
    private String paymentStatus; 
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean isCheckedOut;
    private List<String> addOns;
    private double addOnPrice;

    public BookingEntry(User user, Room room, String paymentStatus, LocalDate checkInDate, LocalDate checkOutDate) {
        this.user = user;
        this.room = room;
        this.paymentStatus = paymentStatus;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isCheckedOut = false; 
    }

    public BookingEntry() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean isCheckedOut) {
        this.isCheckedOut = isCheckedOut;
    }

    public List<String> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<String> addOns) {
        this.addOns = addOns;
    }

    public double getAddOnPrice() {
        return addOnPrice;
    }

    public void setAddOnPrice(double addOnPrice) {
        this.addOnPrice = addOnPrice;
    }

    // Check out logic
    public void checkOut() {
        this.isCheckedOut = true;
        if (room != null) {
            this.room.setAvailable(true);
        }
    }

    public double calculateFine(LocalDate currentDate) {
        if (currentDate.isAfter(checkOutDate) && !isCheckedOut) {
            long daysOverdue = currentDate.toEpochDay() - checkOutDate.toEpochDay();
            return daysOverdue * 50.0; // Fine rate per day
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Booking entry for user: " + user.getName() +
               ", room: {" +
               "type: " + room.getType() +
               ", name: " + room.getName() +
               ", bedType: " + room.getBedType() +
               ", maxOccupancy: " + room.getMaxOccupancy() +
               ", price: " + room.getPrice() +
               ", paymentType: " + room.getPaymentType() +
               ", available: " + room.isAvailable() +
               ", state: " + room.getState() +
               ", addOns: " + addOns +
               ", addOnPrice: " + addOnPrice +
               "}" +
               ", paymentStatus: " + paymentStatus +
               ", check-in: " + checkInDate +
               ", check-out: " + checkOutDate +
               ", is check-out: " + isCheckedOut;
    }
}
