package factory;

import model.Room;
import payment.Cash;

public abstract class RoomFactory {

	public static RoomFactory createFactory(String type) {
		if(type.equals("Suite")) {
			return new SuiteFactory();
		}else if(type.equals("Deluxe")) {
			return new DeluxeFactory();
		}else {
			return new StudioFactory();
		}
	}
	public abstract Room createRoom(String type, String name, String bedType, int maxOccupancy, Cash cash, String paymentTpe);

}
