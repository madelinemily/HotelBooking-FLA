package model;

import payment.Cash;

public class DeluxeRoom extends Room{

	public DeluxeRoom(String type, String name, String bedType, int maxOccupancy, Cash cash, String paymentType) {
		super(type, name, bedType, maxOccupancy, cash.getPrice(), paymentType);
		// TODO Auto-generated constructor stub
	}


}
