package model;

public class BookingEntry {
    private User user;
    private Room room;
    private String paymentStatus; 

    public BookingEntry(User user, Room room, String paymentStatus) {
        this.user = user;
        this.room = room;
        this.paymentStatus = paymentStatus;
    }

    public User getUser() {
        return user;
    }
    public Room getRoom() {
        return room;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Booking entry for user: " + user.getName() + ", room: " + room.getType() + ", paymentStatus: " + paymentStatus;
    }
}
