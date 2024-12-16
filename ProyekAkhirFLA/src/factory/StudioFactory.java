package factory;

import model.Room;
import model.StudioRoom;
import payment.Cash;

public class StudioFactory extends RoomFactory{

	public StudioFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Room createRoom(String type, String name, String bedType, int maxOccupancy, Cash cash, String paymentTpe) {
		// TODO Auto-generated method stub
		return new StudioRoom(type, name, bedType, maxOccupancy, cash, paymentTpe);
	}



}
