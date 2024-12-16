package factory;

import model.DeluxeRoom;
import model.Room;
import payment.Cash;

public class DeluxeFactory extends RoomFactory{

	public DeluxeFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Room createRoom(String type, String name, String bedType, int maxOccupancy, Cash cash, String paymentTpe) {
		// TODO Auto-generated method stub
		return new DeluxeRoom(type, name, bedType, maxOccupancy, cash, paymentTpe);
	}



}
