package model;

import payment.Cash;

public class SuiteRoom extends Room{

	public SuiteRoom(String type, String name, String bedType, int maxOccupancy, Cash cash, String paymentType) {
		super(type, name, bedType, maxOccupancy, cash.getPrice(), paymentType);
		// TODO Auto-generated constructor stub
	}


}
