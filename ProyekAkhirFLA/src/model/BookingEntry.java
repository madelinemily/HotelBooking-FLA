package model;

import java.time.LocalDate;

public class BookingEntry {
    private User user;
    private Room room;
    private String paymentStatus; 
    private LocalDate checkInDate;
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

	private LocalDate checkOutDate;
    private boolean isCheckedOut;

    public BookingEntry(User user, Room room, String paymentStatus, LocalDate checkInDate, LocalDate checkOutDate) {
        this.user = user;
        this.room = room;
        this.paymentStatus = paymentStatus;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isCheckedOut = false; 
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
    
    public void checkOut() {
        this.isCheckedOut = true;
        this.room.setAvailable(true);
    }

    public double calculateFine(LocalDate currentDate) {
        if(currentDate.isAfter(checkOutDate) && !isCheckedOut) {
           long daysOverdue = currentDate.toEpochDay() - checkOutDate.toEpochDay();
           return daysOverdue * 50.0; 
        }
        
        return 0.0;
    }

    public BookingEntry() {
		
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
               ", addOns: " + room.getAddOns() +
               ", addOnPrice: " + room.getAddOnPrice() +
               "}" +
               ", paymentStatus: " + paymentStatus +
               ", check-in: " + checkInDate +
               ", check-out: " + checkOutDate +
               ", is check-out: " + isCheckedOut;
    }

}
