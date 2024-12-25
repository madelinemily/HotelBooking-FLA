package model;

import java.util.ArrayList;
import java.util.List;

import state.AvailableState;
import state.State;

public class Room {
	private String type;
	private String name;
	private String bedType;
	private int maxOccupancy;
	private double price;
	private String paymentType;
	private boolean available;
	private State state;
	private List<String> addOns;
    private double addOnPrice;

    public Room(String type, String name, String bedType, int maxOccupancy, double price, String paymentType) {
        super();
        this.type = type;
        this.name = name;
        this.bedType = bedType;
        this.maxOccupancy = maxOccupancy;
        this.price = price;
        this.paymentType = paymentType;
        this.available = true;
        this.state = new AvailableState();
        this.addOns = new ArrayList<>();
        this.addOnPrice = 0.0;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBedType() {
		return bedType;
	}
	public void setBedType(String bedType) {
		this.bedType = bedType;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public double getPrice() {
        return price + addOnPrice; 
    }
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public void bookRoom() {
        state.book(this);
    }
    public void unlockRoom() {
        state.unlock(this);
    }
    public void lockRoom() {
        state.lock(this);
    }
    
    public void addAddOn(String addOn, double price) {
        addOns.add(addOn);
        this.addOnPrice += price;
    }
    
    @Override
    public String toString() {
        return "Room with type: " + type + ", name: " + name + ", bedType: " + bedType + ", maxOccupancy: " + maxOccupancy + ", price: " + price + " and " + state.toString();
    }
}
